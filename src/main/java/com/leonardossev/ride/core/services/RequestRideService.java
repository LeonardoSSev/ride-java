package com.leonardossev.ride.core.services;

import com.leonardossev.ride.adapters.inbound.http.dto.ride.RequestRide;
import com.leonardossev.ride.core.ports.inbound.RequestRideInboundPort;
import com.leonardossev.ride.core.ports.outbound.AccountPersistenceOutboundPort;
import com.leonardossev.ride.core.ports.outbound.RidePersistenceOutboundPort;
import org.springframework.stereotype.Service;

@Service
public class RequestRideService implements RequestRideInboundPort {

    AccountPersistenceOutboundPort accountPersistenceOutboundPort;
    RidePersistenceOutboundPort ridePersistenceOutboundPort;

    public RequestRideService(AccountPersistenceOutboundPort accountPersistenceOutboundPort) {
        this.accountPersistenceOutboundPort = accountPersistenceOutboundPort;
    }

    @Override
    public String execute(RequestRide requestRide) {
        try {
            // TODO: implement

            return "";
        } catch (Exception e) {
            System.err.printf("Error: %s", e.getMessage());
            throw e;
        }
    }
}