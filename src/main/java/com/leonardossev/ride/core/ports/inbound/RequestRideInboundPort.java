package com.leonardossev.ride.core.ports.inbound;

import com.leonardossev.ride.adapters.inbound.http.dto.ride.RequestRide;

public interface RequestRideInboundPort {

    String execute(RequestRide requestRide);

}
