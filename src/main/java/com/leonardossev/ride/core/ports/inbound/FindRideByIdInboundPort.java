package com.leonardossev.ride.core.ports.inbound;

import com.leonardossev.ride.core.model.ride.Ride;

import java.util.Optional;

public interface FindRideByIdInboundPort {

    Optional<Ride> execute(String rideId);

}
