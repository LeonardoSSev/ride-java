package com.leonardossev.ride.core.model;

import com.leonardossev.ride.adapters.inbound.http.dto.signup.SignupAccount;

import java.sql.Date;
import java.util.UUID;

public record Ride(
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
    public static Ride fromSignupAccount(SignupAccount signupAccount, String accountId, String verificationcode, Date createdAt) {
        return new Ride(
                UUID.fromString(accountId),
                signupAccount.name(),
                signupAccount.email(),
                signupAccount.cpf(),
                signupAccount.carPlate(),
                signupAccount.isPassenger(),
                signupAccount.isDriver(),
                createdAt,
                false,
                UUID.fromString(verificationcode)
        );
    }
}