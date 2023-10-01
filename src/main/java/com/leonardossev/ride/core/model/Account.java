package com.leonardossev.ride.core.model;

import java.sql.Date;
import java.util.Map;
import java.util.UUID;

public record Account (
        UUID id,
        String name,
        String email,
        String cpf,
        String carPlate,
        boolean isPassenger,
        boolean isDriver,
        Date createdAt,
        boolean isVerified,
        UUID verificationCode
){
    public static Account fromMap(Map<String, Object> map, String accountId, String verificationcode, Date createdAt) {
        return new Account(
                UUID.fromString(accountId),
                map.get("name").toString(),
                map.get("email").toString(),
                map.get("cpf").toString(),
                map.get("carPlate") != null ? map.get("carPlate").toString() : "",
                map.get("isPassenger") != null && Boolean.getBoolean(map.get("isPassenger").toString()),
                map.get("isDriver") != null && Boolean.getBoolean(map.get("isDriver").toString()),
                createdAt,
                map.get("isVerified") != null && Boolean.getBoolean(map.get("isVerified").toString()),
                UUID.fromString(verificationcode)
        );
    }
}
