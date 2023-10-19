package com.leonardossev.ride.core.services;

import com.leonardossev.ride.adapters.inbound.http.dto.signup.SignupAccount;
import com.leonardossev.ride.core.model.Account;
import com.leonardossev.ride.core.ports.outbound.AccountPersistenceOutboundPort;
import com.leonardossev.ride.core.ports.outbound.SendEmailOutboundPort;
import com.leonardossev.ride.core.services.helper.SignupAccountTestsHelper;
import com.leonardossev.ride.core.validators.CpfValidator;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SignupServiceTest {

    private SignupService signupService;

    @BeforeEach
    public void setUp() {
        AccountPersistenceOutboundPort accountPersistenceOutboundPort = mock(AccountPersistenceOutboundPort.class);
        SendEmailOutboundPort sendEmailOutboundPort = mock(SendEmailOutboundPort.class);

        signupService = new SignupService(new CpfValidator(), accountPersistenceOutboundPort, sendEmailOutboundPort);
    }

    @AfterEach
    public void tearDown() {

    }

    @Test
    public void shouldCreatePassenger() {
        SignupAccount input = SignupAccountTestsHelper.buildSimplePassenger();

        String accountId = signupService.execute(input);

        when(signupService.accountPersistenceOutboundPort.findByEmail(input.email())).thenReturn(Optional.empty());
        assertNotNull(accountId);
    }

    @Test
    public void shouldNotCreatePassengerWithInvalidCpf() {
        SignupAccount input = SignupAccountTestsHelper.buildPassengerWithInvalidCpf();

        assertThrows(Exception.class, () -> signupService.execute(input), "Invalid cpf");
    }

    @Test
    public void shouldNotCreatePassengerWithInvalidName() {
        SignupAccount input = SignupAccountTestsHelper.buildPassengerWithInvalidName();

        assertThrows(Exception.class, () -> signupService.execute(input), "Invalid name");
    }

    @Test
    public void shouldNotCreatePassengerWithInvalidEmail() {
        SignupAccount input = SignupAccountTestsHelper.buildPassengerWithInvalidEmail();

        assertThrows(Exception.class, () -> signupService.execute(input), "Invalid email");
    }

    @Test
    public void shouldNotCreateDuplicatePassenger() {
        SignupAccount input = SignupAccountTestsHelper.buildSimplePassenger();

        when(signupService.accountPersistenceOutboundPort.findByEmail(input.email())).thenReturn(Optional.empty());
        signupService.execute(input);

        when(signupService.accountPersistenceOutboundPort.findByEmail(input.email())).thenReturn(Optional.of(Account.builder().build()));
        assertThrows(Exception.class, () -> signupService.execute(input), "Account already exists");
    }

    @Test
    public void shouldCreateDriver() {
        SignupAccount input = SignupAccountTestsHelper.buildSimpleDriver();

        String accountId = signupService.execute(input);

        when(signupService.accountPersistenceOutboundPort.findByEmail(input.email())).thenReturn(Optional.empty());
        assertNotNull(accountId);
    }

    @Test
    public void shouldNotCreateDriverWithInvalidCarPlate() {
        SignupAccount input = SignupAccountTestsHelper.buildDriverWithInvalidCarPlate();

        assertThrows(Exception.class, () -> signupService.execute(input), "Invalid plate");
    }
    
}
