package com.leonardossev.ride.core.model.ride;

import com.leonardossev.ride.adapters.inbound.http.dto.ride.RequestRide;
import com.leonardossev.ride.adapters.inbound.http.dto.signup.SignupAccount;
import com.leonardossev.ride.core.model.Account;

import java.sql.Date;
import java.util.UUID;

public record Ride(
        UUID id,
        Account passenger,
        Account driver,
        RideLocalization from,
        RideLocalization to,
        RideStatus status,
        Date createdAt
){
    public static Ride fromRequestRide(RequestRide requestRide, UUID rideId, Date createdAt) {
        return new Ride(
                rideId,
                Account.builder().id(UUID.fromString(requestRide.passengerId())).build(),
                null,
                new RideLocalization(requestRide.from().latitude(), requestRide.from().longitude()),
                new RideLocalization(requestRide.to().latitude(), requestRide.to().longitude()),
                RideStatus.REQUESTED,
                createdAt
        );
    }
}
