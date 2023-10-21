package com.leonardossev.ride.core.ports.outbound;

import com.leonardossev.ride.core.model.Account.Account;
import com.leonardossev.ride.shared.exceptions.PersistenceException;

import java.util.Optional;
import java.util.UUID;

public interface AccountPersistenceOutboundPort {

    boolean save(Account account) throws PersistenceException;

    Optional<Account> findById(UUID id) throws PersistenceException;

    Optional<Account> findByEmail(String email) throws PersistenceException;
}
