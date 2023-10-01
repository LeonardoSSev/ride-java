package com.leonardossev.ride.adapters.inbound.http;

import com.leonardossev.ride.adapters.inbound.http.dto.ride.RequestRide;
import com.leonardossev.ride.adapters.inbound.http.dto.signup.SignupAccount;
import com.leonardossev.ride.core.model.Account;
import com.leonardossev.ride.core.ports.inbound.GetAccountByIdInboundPort;
import com.leonardossev.ride.core.ports.inbound.RequestRideInboundPort;
import com.leonardossev.ride.core.ports.inbound.SignupInboundPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController(value = "/ride")
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
public class RideController {

    private final RequestRideInboundPort requestRideInboundPort;


    public RideController(RequestRideInboundPort requestRideInboundPort) {
        this.requestRideInboundPort = requestRideInboundPort;
    }

    @PostMapping(value = "/request")
    public ResponseEntity<String> request(@RequestBody RequestRide requestRide) {
        return new ResponseEntity<String>(
                this.requestRideInboundPort.execute(requestRide),
                HttpStatus.CREATED
        );
    }

}
