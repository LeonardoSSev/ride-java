package com.leonardossev.ride.adapters.outbound.persistence.account;

import com.leonardossev.ride.adapters.outbound.persistence.InMemoryPersistenceBase;
import com.leonardossev.ride.adapters.outbound.persistence.model.AccountEntity;
import com.leonardossev.ride.core.model.Account.Account;
import com.leonardossev.ride.core.ports.outbound.AccountPersistenceOutboundPort;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class InMemoryAccountClientAdapter extends InMemoryPersistenceBase implements AccountPersistenceOutboundPort {

    @Override
    public boolean save(Account account) {
        AccountEntity accountEntity = AccountEntity.fromAccount(account);
        InMemoryAccountClientAdapter.accounts.put(account.id(), accountEntity);
        return true;
    }

    @Override
    public Optional<Account> findById(UUID id) {
        AccountEntity accountEntity = InMemoryAccountClientAdapter.accounts.get(id);

        return Optional.ofNullable(AccountEntity.toAccount(accountEntity));
    }

    @Override
    public Optional<Account> findByEmail(String email) {
        return InMemoryAccountClientAdapter.accounts
                .values()
                .stream()
                .filter(account -> account.email().equalsIgnoreCase(email))
                .findFirst()
                .map(AccountEntity::toAccount);
    }

}
