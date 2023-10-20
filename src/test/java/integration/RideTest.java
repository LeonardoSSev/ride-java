package integration;

import com.leonardossev.ride.adapters.inbound.http.dto.ride.AcceptRide;
import com.leonardossev.ride.adapters.inbound.http.dto.ride.Localization;
import com.leonardossev.ride.adapters.inbound.http.dto.ride.RequestRide;
import com.leonardossev.ride.adapters.inbound.http.dto.signup.SignupAccount;
import com.leonardossev.ride.adapters.outbound.persistence.InMemoryAccountClientAdapter;
import com.leonardossev.ride.core.model.ride.Ride;
import com.leonardossev.ride.core.model.ride.RideStatus;
import com.leonardossev.ride.core.ports.inbound.AcceptRideInboundPort;
import com.leonardossev.ride.core.ports.inbound.RequestRideInboundPort;
import com.leonardossev.ride.core.ports.outbound.AccountPersistenceOutboundPort;
import com.leonardossev.ride.core.ports.outbound.RidePersistenceOutboundPort;
import com.leonardossev.ride.core.ports.outbound.SendEmailOutboundPort;
import com.leonardossev.ride.core.services.RequestRideService;
import com.leonardossev.ride.core.services.SignupService;
import com.leonardossev.ride.core.services.helper.SignupAccountTestsHelper;
import com.leonardossev.ride.core.validators.CpfValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.mockito.Mockito.mock;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class RideTest {

    private SignupService signupService;
    private RequestRideInboundPort requestRideService;
    private AcceptRideInboundPort acceptRideService;
    private FindRideByIdInboundPort getRideByIdService;

    @BeforeEach
    public void setUp() {
        AccountPersistenceOutboundPort accountPersistenceOutboundPort = new InMemoryAccountClientAdapter();
        RidePersistenceOutboundPort ridePersistenceOutboundPort = new InMemoryRideClientAdapter();
        SendEmailOutboundPort sendEmailOutboundPort = mock(SendEmailOutboundPort.class);

        signupService = new SignupService(new CpfValidator(), accountPersistenceOutboundPort, sendEmailOutboundPort);
        requestRideService = new RequestRideService(ridePersistenceOutboundPort, accountPersistenceOutboundPort);
        acceptRideService = new AcceptRideService(ridePersistenceOutboundPort, accountPersistenceOutboundPort);
        findRideByIdService = new FindRideByIdService(ridePersistenceOutboundPort);
        findAccountByIdService = new FindAccountByIdService(accountPersistenceOutboundPort);
    }

    @Test
    public void shouldRequestARide() {
        SignupAccount passengerInput = SignupAccountTestsHelper.buildSimplePassenger();

        String passengerId = signupService.execute(passengerInput);

        RequestRide rideInput = new RequestRide(passengerId, new Localization(-8.118854408490204, -34.90426120762872), new Localization(-8.192285503124415, -34.92222109300217));
        String rideId = requestRideService.execute(rideInput);

        Optional<Ride> rideOptional = findRideByIdService.execute(rideId);

        assertTrue(rideOptional.isPresent());
        Ride ride = rideOptional.get();
        assertEquals(ride.passenger().id().toString(), passengerId);
        assertEquals(ride.from().latitude(), rideInput.from().latitude());
        assertEquals(ride.from().longitude(), rideInput.from().longitude());
        assertEquals(ride.to().latitude(), rideInput.to().latitude());
        assertEquals(ride.to().longitude(), rideInput.to().longitude());
        assertNotNull(ride.createdAt());
        assertEquals(ride.status(), RideStatus.REQUESTED);
    }

    @Test
    public void shouldNotRequestARideIfIsNotPassenger() {
        SignupAccount driverInput = SignupAccountTestsHelper.buildSimpleDriver();

        String driverId = signupService.execute(driverInput);

        RequestRide rideInput = new RequestRide(driverId, new Localization(-8.118854408490204, -34.90426120762872), new Localization(-8.192285503124415, -34.92222109300217));
        assertThrows(Exception.class, () -> requestRideService.execute(rideInput), "Ride must be request by a passenger");
    }

    @Test
    public void shouldNotRequestARideIfAnotherRequestedRideExist() {
        SignupAccount passengerInput = SignupAccountTestsHelper.buildSimplePassenger();

        String passengerId = signupService.execute(passengerInput);

        RequestRide rideInput = new RequestRide(passengerId, new Localization(-8.118854408490204, -34.90426120762872), new Localization(-8.192285503124415, -34.92222109300217));
        requestRideService.execute(rideInput);
        requestRideService.execute(rideInput);
        assertThrows(Exception.class, () -> requestRideService.execute(rideInput), "There is a requested ride in action for this passenger");
    }

    @Test
    public void shouldNotRequestARideIfAnotherInProgressRideExist() {
        SignupAccount passengerInput = SignupAccountTestsHelper.buildSimplePassenger();
        String passengerId = signupService.execute(passengerInput);

        RequestRide rideInput = new RequestRide(passengerId, new Localization(-8.118854408490204, -34.90426120762872), new Localization(-8.192285503124415, -34.92222109300217));
        String rideId = requestRideService.execute(rideInput);

        SignupAccount driverInput = SignupAccountTestsHelper.buildSimpleDriver();
        String driverId = signupService.execute(driverInput);

        AcceptRide acceptInput = new AcceptRide(driverId, rideId);
        acceptRideService.execute(acceptInput);

        assertThrows(Exception.class, () -> requestRideService.execute(rideInput), "There is ride in action for this passenger");
    }

    @Test
    public void shouldNotAcceptARideIfIsNotADriver() {
        SignupAccount passengerInput = SignupAccountTestsHelper.buildSimplePassenger();
        String passengerId = signupService.execute(passengerInput);

        RequestRide rideInput = new RequestRide(passengerId, new Localization(-8.118854408490204, -34.90426120762872), new Localization(-8.192285503124415, -34.92222109300217));
        String rideId = requestRideService.execute(rideInput);

        AcceptRide acceptInput = new AcceptRide(passengerId, rideId);
        acceptRideService.execute(acceptInput);

        assertThrows(Exception.class, () -> requestRideService.execute(rideInput), "Only drivers can accept a ride");
    }

}
