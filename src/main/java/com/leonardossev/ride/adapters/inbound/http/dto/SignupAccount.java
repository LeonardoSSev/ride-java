package com.leonardossev.ride.adapters.inbound.http.dto;

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
