package com.leonardossev.ride.core.services;

import com.leonardossev.ride.core.model.Account.Account;
import com.leonardossev.ride.core.ports.inbound.FindAccountByIdInboundPort;
import com.leonardossev.ride.core.ports.outbound.AccountPersistenceOutboundPort;
import org.springframework.stereotype.Service;
import java.util.Optional;
import java.util.UUID;

@Service
public class FindAccountByIdService implements FindAccountByIdInboundPort {

    private final AccountPersistenceOutboundPort accountPersistenceOutboundPort;

    public FindAccountByIdService(AccountPersistenceOutboundPort accountPersistenceOutboundPort) {
        this.accountPersistenceOutboundPort = accountPersistenceOutboundPort;
    }

    @Override
    public Optional<Account> execute(String uuid) {
        return this.accountPersistenceOutboundPort.findById(UUID.fromString(uuid));
    }
}
