package com.leonardossev.ride.core.services;

import com.leonardossev.ride.core.model.ride.Ride;
import com.leonardossev.ride.core.ports.inbound.FindRideByIdInboundPort;
import com.leonardossev.ride.core.ports.outbound.RidePersistenceOutboundPort;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class FindRideByIdService implements FindRideByIdInboundPort {

    private final RidePersistenceOutboundPort ridePersistenceOutboundPort;

    public FindRideByIdService(RidePersistenceOutboundPort ridePersistenceOutboundPort) {
        this.ridePersistenceOutboundPort = ridePersistenceOutboundPort;
    }

    @Override
    public Optional<Ride> execute(String rideId) {
        return this.ridePersistenceOutboundPort.findById(UUID.fromString(rideId));
    }
}
