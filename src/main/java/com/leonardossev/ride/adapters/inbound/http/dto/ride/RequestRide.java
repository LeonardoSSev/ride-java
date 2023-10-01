package com.leonardossev.ride.adapters.inbound.http.dto.ride;

public record RequestRide(
        String passengerId,
        Localization from,
        Localization to
) {
}
