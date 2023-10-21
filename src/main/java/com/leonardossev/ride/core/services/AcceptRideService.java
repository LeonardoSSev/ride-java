package com.leonardossev.ride.core.services;

import com.leonardossev.ride.adapters.inbound.http.dto.ride.AcceptRide;
import com.leonardossev.ride.core.model.Account.Account;
import com.leonardossev.ride.core.model.ride.Ride;
import com.leonardossev.ride.core.model.ride.RideStatus;
import com.leonardossev.ride.core.ports.inbound.AcceptRideInboundPort;
import com.leonardossev.ride.core.ports.outbound.AccountPersistenceOutboundPort;
import com.leonardossev.ride.core.ports.outbound.RidePersistenceOutboundPort;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class AcceptRideService implements AcceptRideInboundPort {

    AccountPersistenceOutboundPort accountPersistenceOutboundPort;
    RidePersistenceOutboundPort ridePersistenceOutboundPort;

    public AcceptRideService(AccountPersistenceOutboundPort accountPersistenceOutboundPort, RidePersistenceOutboundPort ridePersistenceOutboundPort) {
        this.accountPersistenceOutboundPort = accountPersistenceOutboundPort;
        this.ridePersistenceOutboundPort = ridePersistenceOutboundPort;
    }

    @Override
    public void execute(AcceptRide acceptRide) {
        Optional<Account> accountOptional = this.accountPersistenceOutboundPort.findById(UUID.fromString(acceptRide.driverId()));
        this.validateAccount(accountOptional);

        Optional<Ride> rideOptional = this.ridePersistenceOutboundPort.findById(UUID.fromString(acceptRide.rideId()));
        this.validateRide(rideOptional);

        Ride ride = rideOptional.get();
        Account driver = accountOptional.get();
        ride.acceptRide(driver);

        this.ridePersistenceOutboundPort.update(ride);
    }

    private void validateAccount(Optional<Account> accountOptional) {
        if (accountOptional.isEmpty()) {
            throw new RuntimeException("Driver not found");
        }

        Account driver = accountOptional.get();
        if (!driver.isDriver()) {
            throw new RuntimeException("Ride can only be accepted by a driver");
        }
    }

    private void validateRide(Optional<Ride> rideOptional) {
        if (rideOptional.isEmpty()) {
            throw new RuntimeException("Ride not found");
        }

        Ride ride = rideOptional.get();
        if (!ride.getStatus().equals(RideStatus.REQUESTED)) {
            throw new RuntimeException("Only requested rides can be accepted");
        }
    }
}