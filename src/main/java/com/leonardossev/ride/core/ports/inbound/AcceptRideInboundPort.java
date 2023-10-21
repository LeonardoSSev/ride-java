package com.leonardossev.ride.core.ports.inbound;


import com.leonardossev.ride.adapters.inbound.http.dto.ride.AcceptRide;

public interface AcceptRideInboundPort {

    void execute(AcceptRide acceptRide);

}
