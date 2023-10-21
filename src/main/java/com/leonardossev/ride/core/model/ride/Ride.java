package com.leonardossev.ride.core.model.ride;

import com.leonardossev.ride.adapters.inbound.http.dto.ride.RequestRide;
import com.leonardossev.ride.core.model.Account.Account;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.sql.Date;
import java.time.LocalDateTime;
import java.util.UUID;

@AllArgsConstructor
@Builder
public class Ride {

    @Getter
    private UUID id;

    @Getter
    private Account passenger;

    @Getter
    private Account driver;

    @Getter
    private RideLocalization from;

    @Getter
    private RideLocalization to;

    @Getter
    private RideStatus status;

    @Getter
    private Date createdAt;

    @Getter
    private Date updatedAt;

    public static Ride fromRequestRide(RequestRide requestRide, UUID rideId, Date createdAt) {
        return new Ride(
                rideId,
                Account.builder().id(UUID.fromString(requestRide.passengerId())).build(),
                null,
                new RideLocalization(requestRide.from().latitude(), requestRide.from().longitude()),
                new RideLocalization(requestRide.to().latitude(), requestRide.to().longitude()),
                RideStatus.REQUESTED,
                createdAt,
                null
        );
    }

    public void acceptRide(Account driver) {
        this.status = RideStatus.ACCEPTED;
        this.driver = driver;
        this.updatedAt = Date.valueOf(String.valueOf(LocalDateTime.now().toLocalDate()));
    }

}
