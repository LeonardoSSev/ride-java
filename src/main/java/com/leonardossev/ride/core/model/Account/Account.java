package com.leonardossev.ride.core.model.Account;

import com.leonardossev.ride.adapters.inbound.http.dto.signup.SignupAccount;
import com.leonardossev.ride.core.model.ride.Ride;
import lombok.Builder;

import java.sql.Date;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Builder
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
        UUID verificationCode,
        List<Ride> requestedRides,
        List<Ride> rides
){
    public static Account fromSignupAccount(SignupAccount signupAccount, String accountId, String verificationcode, Date createdAt) {
        return new Account(
                UUID.fromString(accountId),
                signupAccount.name(),
                signupAccount.email(),
                signupAccount.cpf(),
                signupAccount.carPlate(),
                signupAccount.isPassenger(),
                signupAccount.isDriver(),
                createdAt,
                false,
                UUID.fromString(verificationcode),
                Collections.emptyList(),
                Collections.emptyList()
        );
    }
}
