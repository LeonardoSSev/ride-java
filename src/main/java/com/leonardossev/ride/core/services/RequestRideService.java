package com.leonardossev.ride.core.services;

import com.leonardossev.ride.adapters.inbound.http.dto.ride.RequestRide;
import com.leonardossev.ride.core.model.Account.Account;
import com.leonardossev.ride.core.model.ride.Ride;
import com.leonardossev.ride.core.ports.inbound.RequestRideInboundPort;
import com.leonardossev.ride.core.ports.outbound.AccountPersistenceOutboundPort;
import com.leonardossev.ride.core.ports.outbound.RidePersistenceOutboundPort;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class RequestRideService implements RequestRideInboundPort {

    AccountPersistenceOutboundPort accountPersistenceOutboundPort;
    RidePersistenceOutboundPort ridePersistenceOutboundPort;

    public RequestRideService(AccountPersistenceOutboundPort accountPersistenceOutboundPort, RidePersistenceOutboundPort ridePersistenceOutboundPort) {
        this.accountPersistenceOutboundPort = accountPersistenceOutboundPort;
        this.ridePersistenceOutboundPort = ridePersistenceOutboundPort;
    }

    @Override
    public String execute(RequestRide requestRide) {
        try {
            Optional<Account> accountOptional = this.accountPersistenceOutboundPort.findById(UUID.fromString(requestRide.passengerId()));
            this.validateAccount(accountOptional);

            UUID rideId = UUID.randomUUID();
            LocalDateTime now = LocalDateTime.now();

            Ride ride = Ride.fromRequestRide(requestRide, rideId, Date.valueOf(String.valueOf(now.toLocalDate())));
            this.ridePersistenceOutboundPort.save(ride);

            return rideId.toString();
        } catch (Exception e) {
            System.err.printf("Error: %s", e.getMessage());
            throw e;
        }
    }

    private void validateAccount(Optional<Account> accountOptional) {
        if (accountOptional.isEmpty()) {
            throw new RuntimeException("Passenger not found");
        }

        Account account = accountOptional.get();
        if (!account.isPassenger()) {
            throw new RuntimeException("Ride can only be request by a passenger");
        }

        List<Ride> ridesInProgress = this.ridePersistenceOutboundPort.findAllInProgressFromPassenger(account.id());
        if (!ridesInProgress.isEmpty()) {
            throw new RuntimeException("There is a ride in action for this passenger");
        }
    }
}