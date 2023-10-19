package com.leonardossev.ride.core.services;

import com.leonardossev.ride.core.model.Account;
import com.leonardossev.ride.core.ports.inbound.GetAccountByIdInboundPort;
import com.leonardossev.ride.core.ports.outbound.AccountPersistenceOutboundPort;
import org.springframework.stereotype.Service;
import java.util.Optional;
import java.util.UUID;

@Service
public class GetAccountByIdService implements GetAccountByIdInboundPort {

    private final AccountPersistenceOutboundPort accountPersistenceOutboundPort;

    public GetAccountByIdService(AccountPersistenceOutboundPort accountPersistenceOutboundPort) {
        this.accountPersistenceOutboundPort = accountPersistenceOutboundPort;
    }

    @Override
    public Optional<Account> execute(String uuid) {
        return this.accountPersistenceOutboundPort.findById(UUID.fromString(uuid));
    }
}
