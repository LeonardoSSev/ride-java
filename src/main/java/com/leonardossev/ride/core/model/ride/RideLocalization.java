package com.leonardossev.ride.core.model.ride;

import com.leonardossev.ride.adapters.inbound.http.dto.signup.SignupAccount;
import com.leonardossev.ride.core.model.Account;

import java.sql.Date;
import java.util.UUID;

public record RideLocalization(
        Double latitude,
        Double longitude
){
}
