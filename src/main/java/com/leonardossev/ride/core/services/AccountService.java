package com.leonardossev.ride.core.services;

import com.leonardossev.ride.core.model.Account;
import com.leonardossev.ride.core.ports.outbound.AccountPersistenceOutboundPort;
import com.leonardossev.ride.core.validators.ICpfValidator;
import java.sql.Date;
import java.time.LocalDateTime;
import java.util.*;

public class AccountService {

    ICpfValidator cpfValidator;
    AccountPersistenceOutboundPort accountPersistenceOutboundPort;

    public AccountService(ICpfValidator cpfValidator, AccountPersistenceOutboundPort accountPersistenceOutboundPort) {
        this.cpfValidator = cpfValidator;
        this.accountPersistenceOutboundPort = accountPersistenceOutboundPort;
    }

    public Map<String, Object> signup(Map<String, Object> input) throws Exception {
        try {
            String accountId = UUID.randomUUID().toString();
            String verificationCode = UUID.randomUUID().toString();
            LocalDateTime date = LocalDateTime.now();

            this.validateExistingAccount(input.get("email").toString());
            this.validateName(input.get("name").toString());
            this.validateEmail(input.get("email").toString());
            this.cpfValidator.validate(input.get("cpf").toString());

            if (this.isDriver(input.get("isDriver"))) {
                this.validatePlate(input.get("carPlate").toString());
            }

            Account account = Account.fromMap(input, accountId, verificationCode, Date.valueOf(String.valueOf(date.toLocalDate())));
            this.accountPersistenceOutboundPort.save(account);

            this.sendEmail(input.get("email").toString(), "Verification", "Please verify your code at first login " + verificationCode);
            Map<String, Object> response = new HashMap<>();

            response.put("accountId", accountId);
            return response;
        } catch (Exception e) {
            System.err.printf("Error: %s", e.getMessage());
            throw e;
        }
    }

    public Optional<Account> getAccount(String accountId) {
        return this.accountPersistenceOutboundPort.findById(UUID.fromString(accountId));
    }

    private void validateExistingAccount(String email) {
        Optional<Account> accountOptional = this.findByEmail(email);

        if (accountOptional.isEmpty()) {
            throw new RuntimeException("Account already exists");
        }
    }

    private Optional<Account> findByEmail(String email) {
        return this.accountPersistenceOutboundPort.findByEmail(email);
    }

    private void validateName(String name) {
        if (name == null || !name.matches("[a-zA-Z]+ [a-zA-Z]+")) {
            throw new RuntimeException("Invalid email");
        }

    }

    private void validateEmail(String email) {
        if (email == null || !email.matches("^(.+)@(.+)$")) {
            throw new RuntimeException("Invalid email");
        }
    }

    private boolean isDriver(Object isDriver) {
        return isDriver != null && isDriver.equals(true);
    }

    private void validatePlate(String plate) {
        if (plate == null || !plate.matches("[A-Z]{3}[0-9]{4}")) {
            throw new RuntimeException("Invalid plate");
        }
    }

    private void sendEmail(String email, String subject, String message) {
        System.out.println(email + " " + subject + " " + message);
    }
}