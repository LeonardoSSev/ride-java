package com.leonardossev.ride.adapters.outbound.persistence;

import com.leonardossev.ride.adapters.outbound.persistence.model.AccountEntity;
import com.leonardossev.ride.core.model.Account;
import com.leonardossev.ride.core.ports.outbound.AccountPersistenceOutboundPort;
import com.leonardossev.ride.shared.exceptions.PersistenceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import java.sql.*;
import java.util.Optional;
import java.util.UUID;

@Component
@Primary
public class PostgreAccountClientAdapter implements AccountPersistenceOutboundPort {

    @Autowired
    Connection connection;

    @Override
    public boolean save(Account account) throws PersistenceException {
        PreparedStatement insertStatement = null;
        try {
            insertStatement = this.connection.prepareStatement("INSERT INTO cccat13.account (account_id, name, email, cpf, car_plate, is_passenger, is_driver, date, is_verified, verification_code) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
            insertStatement.setObject(1, UUID.fromString(account.id().toString()));
            insertStatement.setString(2, account.name());
            insertStatement.setString(3, account.email());
            insertStatement.setString(4, account.cpf());
            insertStatement.setString(5, account.carPlate());
            insertStatement.setBoolean(6, account.isPassenger());
            insertStatement.setBoolean(7, account.isDriver());
            insertStatement.setDate(8, account.createdAt());
            insertStatement.setBoolean(9, account.isVerified());
            insertStatement.setObject(10, UUID.fromString(account.verificationCode().toString()));
            insertStatement.executeUpdate();

            return true;
        } catch (SQLException e) {
            throw new PersistenceException(e.getMessage());
        }
    }

    @Override
    public Optional<Account> findById(UUID id) throws PersistenceException {
        PreparedStatement findByIdPreparedStatement = null;
        try {
            findByIdPreparedStatement = this.connection.prepareStatement("SELECT * FROM cccat13.account WHERE account_id = ?");
            findByIdPreparedStatement.setObject(1, UUID.fromString(id.toString()));
            ResultSet resultSet = findByIdPreparedStatement.executeQuery();

            if (resultSet.next()) {
                return Optional.of(AccountEntity.toAccount(AccountEntity.fromResultSet(resultSet)));
            }

            return Optional.empty();
        } catch (SQLException e) {
            throw new PersistenceException(e.getMessage());
        }
    }

    @Override
    public Optional<Account> findByEmail(String email) throws PersistenceException {
        PreparedStatement findByEmailPreparedStatement = null;
        try {
            findByEmailPreparedStatement = this.connection.prepareStatement("SELECT * FROM cccat13.account WHERE email = ?");
            findByEmailPreparedStatement.setString(1, email);
            ResultSet resultSet = findByEmailPreparedStatement.executeQuery();

            if (resultSet.next()) {
                return Optional.of(AccountEntity.toAccount(AccountEntity.fromResultSet(resultSet)));
            }

            return Optional.empty();
        } catch (SQLException e) {
            throw new PersistenceException(e.getMessage());
        }
    }

}
