package com.leonardossev.ride.adapters.outbound.persistence.model;

import com.leonardossev.ride.core.model.Account.Account;
import com.leonardossev.ride.core.model.ride.Ride;
import com.leonardossev.ride.core.model.ride.RideLocalization;
import com.leonardossev.ride.core.model.ride.RideStatus;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public record RideEntity(
        UUID rideId,
        UUID passengerId,
        UUID driverId,
        String status,
        Double fromLat,
        Double fromLong,
        Double toLat,
        Double toLong,
        Date createdAt
) {
    public static RideEntity fromRide(Ride ride) {
        return new RideEntity(
                ride.getId(),
                ride.getPassenger() != null ? ride.getPassenger().id() : null,
                ride.getDriver() != null ? ride.getDriver().id() : null,
                ride.getStatus().toString(),
                ride.getFrom().latitude(),
                ride.getFrom().longitude(),
                ride.getTo().latitude(),
                ride.getTo().longitude(),
                ride.getCreatedAt()
        );
    }

    public static RideEntity fromResultSet(ResultSet resultSet) throws SQLException {
        return new RideEntity(
                UUID.fromString(resultSet.getString("ride_id")),
                UUID.fromString(resultSet.getString("passenger_id")),
                UUID.fromString(resultSet.getString("driver_id")),
                resultSet.getString("status"),
                resultSet.getDouble("from_lat"),
                resultSet.getDouble("from_long"),
                resultSet.getDouble("to_lat"),
                resultSet.getDouble("to_long"),
                resultSet.getDate("date")
        );
    }

    public static Ride toRide(RideEntity entity) {
        if (entity == null) {
            return null;
        }

        return new Ride(
                entity.rideId(),
                Account.builder().id(entity.passengerId).build(),
                Account.builder().id(entity.driverId).build(),
                new RideLocalization(entity.fromLat, entity.fromLong()),
                new RideLocalization(entity.toLat, entity.toLong()),
                RideStatus.valueOf(entity.status),
                entity.createdAt(),
                null
        );
    }

    private static List<Ride> buildFromRidesResultSet(ResultSet resultSet, boolean isRequested) throws SQLException {
        List<Ride> rides = new ArrayList<>();

        while (resultSet != null && resultSet.next()) {
            Ride ride = Ride.builder()
                    .id(UUID.fromString(resultSet.getString("ride_id")))
                    .status(RideStatus.valueOf(resultSet.getString("status")))
                    .createdAt(resultSet.getDate("date"))
                    .build();

            rides.add(ride);
        }

        return rides;
    }
}
