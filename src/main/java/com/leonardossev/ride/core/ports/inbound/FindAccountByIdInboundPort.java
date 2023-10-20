package com.leonardossev.ride.core.ports.inbound;

import com.leonardossev.ride.core.model.Account;
import java.util.Optional;

public interface FindAccountByIdInboundPort {

    Optional<Account> execute(String uuid);

}
