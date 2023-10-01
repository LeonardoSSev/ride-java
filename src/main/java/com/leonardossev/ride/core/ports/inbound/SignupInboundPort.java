package com.leonardossev.ride.core.ports.inbound;

import com.leonardossev.ride.adapters.inbound.http.dto.SignupAccount;

public interface SignupInboundPort {

    String execute(SignupAccount signupAccount);
}
