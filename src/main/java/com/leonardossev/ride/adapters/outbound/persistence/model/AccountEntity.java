package com.leonardossev.ride.adapters.outbound.persistence.model;

import com.leonardossev.ride.core.model.Account.Account;
import com.leonardossev.ride.core.model.ride.Ride;
import com.leonardossev.ride.core.model.ride.RideStatus;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public record AccountEntity(
        UUID accountId,
        String name,
        String email,
        String cpf,
        String carPlate,
        boolean isPassenger,
        boolean isDriver,
        Date createdAt,
        boolean isVerified,
        UUID verificationCode,
        List<Ride> requestedRides,
        List<Ride> rides
) {
    public static AccountEntity fromAccount(Account account) {
        return new AccountEntity(
                account.id(),
                account.name(),
                account.email(),
                account.cpf(),
                account.carPlate(),
                account.isPassenger(),
                account.isDriver(),
                account.createdAt(),
                account.isVerified(),
                account.verificationCode(),
                account.requestedRides(),
                account.rides()
        );
    }

    public static AccountEntity fromResultSet(ResultSet resultSet, ResultSet requestedRidesResultSet, ResultSet ridesResultSet) throws SQLException {
        return new AccountEntity(
                UUID.fromString(resultSet.getString("account_id")),
                resultSet.getString("name"),
                resultSet.getString("email"),
                resultSet.getString("cpf"),
                resultSet.getString("car_plate"),
                resultSet.getBoolean("is_passenger"),
                resultSet.getBoolean("is_driver"),
                resultSet.getDate("date"),
                resultSet.getBoolean("is_verified"),
                UUID.fromString(resultSet.getString("verification_code")),
                AccountEntity.buildFromRidesResultSet(requestedRidesResultSet, true),
                AccountEntity.buildFromRidesResultSet(ridesResultSet, false)
        );
    }

    public static Account toAccount(AccountEntity entity) {
        if (entity == null) {
            return null;
        }

        return new Account(
                entity.accountId(),
                entity.name(),
                entity.email(),
                entity.cpf(),
                entity.carPlate(),
                entity.isPassenger(),
                entity.isDriver(),
                entity.createdAt(),
                entity.isVerified(),
                entity.verificationCode(),
                entity.requestedRides(),
                entity.rides()
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
