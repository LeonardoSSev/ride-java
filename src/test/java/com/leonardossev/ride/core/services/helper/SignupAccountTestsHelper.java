package com.leonardossev.ride.core.services.helper;

import com.leonardossev.ride.adapters.inbound.http.dto.signup.SignupAccount;

public class SignupAccountTestsHelper {

    public static SignupAccount buildSimplePassenger() {
        return SignupAccount.builder()
                .name("John Doe")
                .email("john.doe" + Math.random() + "@gmail.com")
                .cpf("95818705552")
                .isPassenger(true)
                .build();
    }

    public static SignupAccount buildPassengerWithInvalidCpf() {
        return SignupAccount.builder()
                .name("John Doe")
                .email("john.doe" + Math.random() + "@gmail.com")
                .cpf("95818705500")
                .isPassenger(true)
                .build();
    }

    public static SignupAccount buildPassengerWithInvalidName() {
        return SignupAccount.builder()
                .name("John")
                .email("john.doe" + Math.random() + "@gmail.com")
                .cpf("95818705552")
                .isPassenger(true)
                .build();
    }

    public static SignupAccount buildPassengerWithInvalidEmail() {
        return SignupAccount.builder()
                .name("John Doe")
                .email("john.doe" + Math.random() + "@")
                .cpf("95818705552")
                .isPassenger(true)
                .build();
    }

    public static SignupAccount buildSimpleDriver() {
        return SignupAccount.builder()
                .name("John Doe")
                .email("john.doe" + Math.random() + "@gmail.com")
                .cpf("95818705552")
                .carPlate("AAA9999")
                .isDriver(true)
                .build();
    }

    public static SignupAccount buildDriverWithInvalidCarPlate() {
        return SignupAccount.builder()
                .name("John Doe")
                .email("john.doe" + Math.random() + "@")
                .cpf("95818705552")
                .carPlate("AAA999")
                .isDriver(true)
                .build();
    }
}
