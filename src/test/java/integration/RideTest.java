package integration;

import com.leonardossev.ride.adapters.inbound.http.dto.ride.AcceptRide;
import com.leonardossev.ride.adapters.inbound.http.dto.ride.Localization;
import com.leonardossev.ride.adapters.inbound.http.dto.ride.RequestRide;
import com.leonardossev.ride.adapters.inbound.http.dto.signup.SignupAccount;
import com.leonardossev.ride.adapters.outbound.persistence.account.InMemoryAccountClientAdapter;
import com.leonardossev.ride.adapters.outbound.persistence.ride.InMemoryRideClientAdapter;
import com.leonardossev.ride.core.model.ride.Ride;
import com.leonardossev.ride.core.model.ride.RideStatus;
import com.leonardossev.ride.core.ports.inbound.AcceptRideInboundPort;
import com.leonardossev.ride.core.ports.inbound.FindRideByIdInboundPort;
import com.leonardossev.ride.core.ports.inbound.RequestRideInboundPort;
import com.leonardossev.ride.core.ports.outbound.AccountPersistenceOutboundPort;
import com.leonardossev.ride.core.ports.outbound.RidePersistenceOutboundPort;
import com.leonardossev.ride.core.ports.outbound.SendEmailOutboundPort;
import com.leonardossev.ride.core.services.*;
import com.leonardossev.ride.core.services.helper.SignupAccountTestsHelper;
import com.leonardossev.ride.core.validators.CpfValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.mockito.Mockito.mock;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class RideTest {

    private SignupService signupService;
    private RequestRideInboundPort requestRideService;
    private AcceptRideInboundPort acceptRideService;
    private FindRideByIdInboundPort findRideByIdService;

    @BeforeEach
    public void setUp() {
        AccountPersistenceOutboundPort accountPersistenceOutboundPort = new InMemoryAccountClientAdapter();
        RidePersistenceOutboundPort ridePersistenceOutboundPort = new InMemoryRideClientAdapter();
        SendEmailOutboundPort sendEmailOutboundPort = mock(SendEmailOutboundPort.class);

        signupService = new SignupService(new CpfValidator(), accountPersistenceOutboundPort, sendEmailOutboundPort);
        requestRideService = new RequestRideService(accountPersistenceOutboundPort, ridePersistenceOutboundPort);
        acceptRideService = new AcceptRideService(accountPersistenceOutboundPort, ridePersistenceOutboundPort);
        findRideByIdService = new FindRideByIdService(ridePersistenceOutboundPort);
    }

    @Test
    @DisplayName("Should request a ride")
    public void shouldRequestARide() {
        SignupAccount passengerInput = SignupAccountTestsHelper.buildSimplePassenger();

        String passengerId = signupService.execute(passengerInput);

        RequestRide rideInput = new RequestRide(passengerId, new Localization(-8.118854408490204, -34.90426120762872), new Localization(-8.192285503124415, -34.92222109300217));
        String rideId = requestRideService.execute(rideInput);

        Optional<Ride> rideOptional = findRideByIdService.execute(rideId);

        assertTrue(rideOptional.isPresent());
        Ride ride = rideOptional.get();
        assertEquals(ride.getPassenger().id().toString(), passengerId);
        assertEquals(ride.getFrom().latitude(), rideInput.from().latitude());
        assertEquals(ride.getFrom().longitude(), rideInput.from().longitude());
        assertEquals(ride.getTo().latitude(), rideInput.to().latitude());
        assertEquals(ride.getTo().longitude(), rideInput.to().longitude());
        assertNotNull(ride.getCreatedAt());
        assertEquals(ride.getStatus(), RideStatus.REQUESTED);
    }

    @Test
    @DisplayName("Should not request a ride if the account is not from a passenger")
    public void shouldNotRequestARideIfIsNotPassenger() {
        SignupAccount driverInput = SignupAccountTestsHelper.buildSimpleDriver();

        String driverId = signupService.execute(driverInput);

        RequestRide rideInput = new RequestRide(driverId, new Localization(-8.118854408490204, -34.90426120762872), new Localization(-8.192285503124415, -34.92222109300217));
        assertThrows(Exception.class, () -> requestRideService.execute(rideInput), "Ride can only be request by a passenger");
    }

    @Test
    @DisplayName("Should not request a ride if the passenger does not exist")
    public void shouldNotRequestARideIfPassengerDoesNotExist() {
        RequestRide rideInput = new RequestRide("123", new Localization(-8.118854408490204, -34.90426120762872), new Localization(-8.192285503124415, -34.92222109300217));
        assertThrows(Exception.class, () -> requestRideService.execute(rideInput), "Passenger not found");
    }

    @Test
    @DisplayName("Should not request a ride if another requested ride exist for the same passenger")
    public void shouldNotRequestARideIfAnotherRequestedRideExist() {
        SignupAccount passengerInput = SignupAccountTestsHelper.buildSimplePassenger();
        String passengerId = signupService.execute(passengerInput);

        RequestRide rideInput = new RequestRide(passengerId, new Localization(-8.118854408490204, -34.90426120762872), new Localization(-8.192285503124415, -34.92222109300217));
        requestRideService.execute(rideInput);
        assertThrows(Exception.class, () -> requestRideService.execute(rideInput), "There is a requested ride in action for this passenger");
    }

    @Test
    @DisplayName("Should not request a ride if the passenger has another ride in progress")
    public void shouldNotRequestARideIfAnotherInProgressRideExist() {
        SignupAccount passengerInput = SignupAccountTestsHelper.buildSimplePassenger();
        String passengerId = signupService.execute(passengerInput);

        RequestRide rideInput = new RequestRide(passengerId, new Localization(-8.118854408490204, -34.90426120762872), new Localization(-8.192285503124415, -34.92222109300217));
        String rideId = requestRideService.execute(rideInput);

        SignupAccount driverInput = SignupAccountTestsHelper.buildSimpleDriver();
        String driverId = signupService.execute(driverInput);

        AcceptRide acceptInput = new AcceptRide(driverId, rideId);
        acceptRideService.execute(acceptInput);

        assertThrows(Exception.class, () -> requestRideService.execute(rideInput), "There is a ride in action for this passenger");
    }

    @Test
    @DisplayName("Should not accept a ride if the account is not from a driver")
    public void shouldNotAcceptARideIfIsNotADriver() {
        SignupAccount passengerInput = SignupAccountTestsHelper.buildSimplePassenger();
        String passengerId = signupService.execute(passengerInput);

        RequestRide rideInput = new RequestRide(passengerId, new Localization(-8.118854408490204, -34.90426120762872), new Localization(-8.192285503124415, -34.92222109300217));
        String rideId = requestRideService.execute(rideInput);

        AcceptRide acceptInput = new AcceptRide(passengerId, rideId);
        assertThrows(Exception.class, () -> acceptRideService.execute(acceptInput), "Ride can only be accepted by a driver");
    }

    @Test
    @DisplayName("Should not accept a ride if the driver does not exist")
    public void shouldNotAcceptARideIfDriverDoesNotExist() {
        SignupAccount passengerInput = SignupAccountTestsHelper.buildSimplePassenger();
        String passengerId = signupService.execute(passengerInput);

        RequestRide rideInput = new RequestRide(passengerId, new Localization(-8.118854408490204, -34.90426120762872), new Localization(-8.192285503124415, -34.92222109300217));
        String rideId = requestRideService.execute(rideInput);

        AcceptRide acceptInput = new AcceptRide("123", rideId);
        assertThrows(Exception.class, () -> acceptRideService.execute(acceptInput), "Driver not found");

    }

    @Test
    @DisplayName("Should not accept a ride if the ride does not exist")
    public void shouldNotAcceptARideIfTheRideDoesNotExist() {
        SignupAccount driverInput = SignupAccountTestsHelper.buildSimpleDriver();
        String driverId = signupService.execute(driverInput);

        AcceptRide acceptInput = new AcceptRide(driverId, "123");
        assertThrows(Exception.class, () -> acceptRideService.execute(acceptInput), "Ride not found");
    }

}
