package com.leonardossev.ride.adapters.inbound.http;

import com.leonardossev.ride.adapters.inbound.http.dto.ride.RequestRide;
import com.leonardossev.ride.core.ports.inbound.RequestRideInboundPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
