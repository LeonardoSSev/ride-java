package com.leonardossev.ride.core.services;

import com.leonardossev.ride.adapters.inbound.http.dto.SignupAccount;
import com.leonardossev.ride.core.model.Account;
import com.leonardossev.ride.core.ports.inbound.SignupInboundPort;
import com.leonardossev.ride.core.ports.outbound.AccountPersistenceOutboundPort;
import com.leonardossev.ride.core.validators.ICpfValidator;
import org.springframework.stereotype.Service;
import java.sql.Date;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class SignupService implements SignupInboundPort {

    ICpfValidator cpfValidator;
    AccountPersistenceOutboundPort accountPersistenceOutboundPort;

    public SignupService(ICpfValidator cpfValidator, AccountPersistenceOutboundPort accountPersistenceOutboundPort) {
        this.cpfValidator = cpfValidator;
        this.accountPersistenceOutboundPort = accountPersistenceOutboundPort;
    }

    @Override
    public String execute(SignupAccount signupAccount) {
        try {
            String accountId = UUID.randomUUID().toString();
            String verificationCode = UUID.randomUUID().toString();
            LocalDateTime date = LocalDateTime.now();

            this.validateSignupInfo(signupAccount);

            Account account = Account.fromSignupAccount(signupAccount, accountId, verificationCode, Date.valueOf(String.valueOf(date.toLocalDate())));
            this.accountPersistenceOutboundPort.save(account);

            this.sendEmail(signupAccount.email(), "Verification", "Please verify your code at first login " + verificationCode);

            return accountId;
        } catch (Exception e) {
            System.err.printf("Error: %s", e.getMessage());
            throw e;
        }
    }

    private void validateSignupInfo(SignupAccount signupAccount) {
        this.validateExistingAccount(signupAccount.email());
        this.validateName(signupAccount.name());
        this.validateEmail(signupAccount.email());
        this.cpfValidator.validate(signupAccount.cpf());

        if (this.isDriver(signupAccount.isDriver())) {
            this.validatePlate(signupAccount.carPlate());
        }
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