package com.leonardossev.ride.adapters.persistence.model;

import com.leonardossev.ride.core.model.Account;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
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
        UUID verificationCode
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
                account.verificationCode()
        );
    }

    public static AccountEntity fromResultSet(ResultSet resultSet) throws SQLException {
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
                UUID.fromString(resultSet.getString("verification_code"))
        );
    }

    public static Account toAccount(AccountEntity entity) {
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
                entity.verificationCode()
        );
    }
}
