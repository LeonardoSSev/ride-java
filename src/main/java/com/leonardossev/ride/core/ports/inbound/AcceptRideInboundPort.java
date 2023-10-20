package com.leonardossev.ride.core.ports.inbound;

import java.util.UUID;

public interface AcceptRideInboundPort {

    String execute(UUID rideId);

}
