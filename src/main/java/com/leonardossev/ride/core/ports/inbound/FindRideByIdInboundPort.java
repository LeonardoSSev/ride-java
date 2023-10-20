package com.leonardossev.ride.core.ports.inbound;

import java.util.UUID;

public interface FindRideByIdInboundPort {

    String execute(UUID rideId);

}
