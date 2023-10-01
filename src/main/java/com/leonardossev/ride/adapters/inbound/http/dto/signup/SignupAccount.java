package com.leonardossev.ride.adapters.inbound.http.dto.signup;

public record SignupAccount (
        String name,
        String email,
        String cpf,
        String carPlate,
        boolean isPassenger,
        boolean isDriver,
        boolean isVerified
){
}
