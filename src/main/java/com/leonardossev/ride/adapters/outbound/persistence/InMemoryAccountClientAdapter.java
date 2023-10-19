package com.leonardossev.ride.adapters.outbound.persistence;

import com.leonardossev.ride.adapters.outbound.persistence.model.AccountEntity;
import com.leonardossev.ride.core.model.Account;
import com.leonardossev.ride.core.ports.outbound.AccountPersistenceOutboundPort;
import com.leonardossev.ride.shared.exceptions.PersistenceException;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class InMemoryAccountClientAdapter implements AccountPersistenceOutboundPort {
    
    private static final Map<UUID, Account> accounts = new ConcurrentHashMap<>();

    @Override
    public boolean save(Account account) {
        InMemoryAccountClientAdapter.accounts.put(account.id(), account);
        return true;
    }

    @Override
    public Optional<Account> findById(UUID id) {
        return Optional.ofNullable(InMemoryAccountClientAdapter.accounts.get(id));
    }

    @Override
    public Optional<Account> findByEmail(String email) {
        return InMemoryAccountClientAdapter.accounts
                .values()
                .stream()
                .filter(account -> account.email().equalsIgnoreCase(email))
                .findFirst();
    }

}
