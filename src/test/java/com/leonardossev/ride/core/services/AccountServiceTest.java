package com.leonardossev.ride.core.services;

import com.leonardossev.ride.core.model.Account;
import com.leonardossev.ride.core.ports.outbound.AccountPersistenceOutboundPort;
import com.leonardossev.ride.core.validators.CpfValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

public class AccountServiceTest {

    private AccountService accountService;

    @BeforeEach
    public void setUp() {
        AccountPersistenceOutboundPort accountPersistenceOutboundPort = mock(AccountPersistenceOutboundPort.class);

        accountService = new AccountService(new CpfValidator(), accountPersistenceOutboundPort);
    }

    @Test
    public void shouldCreatePassenger() throws Exception {
        Map<String, Object> input = new HashMap<>();
        input.put("name", "John Doe");
        input.put("email", "john.doe" + Math.random() + "@gmail.com");
        input.put("cpf", "95818705552");
        input.put("isPassenger", true);

        Map<String, Object> output = accountService.signup(input);
        Optional<Account> accountOptional = accountService.getAccount(output.get("accountId").toString());

        assertTrue(accountOptional.isPresent());
        Account account = accountOptional.get();
        assertEquals(input.get("name"), account.name());
        assertEquals(input.get("email"), account.email());
        assertEquals(input.get("cpf"), account.cpf());
    }

    @Test
    public void shouldNotCreatePassengerWithInvalidCpf() {
        Map<String, Object> input = new HashMap<>();
        input.put("name", "John Doe");
        input.put("email", "john.doe" + Math.random() + "@gmail.com");
        input.put("cpf", "95818705500");
        input.put("isPassenger", true);

        assertThrows(Exception.class, () -> accountService.signup(input), "Invalid cpf");
    }

    @Test
    public void shouldNotCreatePassengerWithInvalidName() {
        Map<String, Object> input = new HashMap<>();
        input.put("name", "John");
        input.put("email", "john.doe" + Math.random() + "@gmail.com");
        input.put("cpf", "95818705552");
        input.put("isPassenger", true);

        assertThrows(Exception.class, () -> accountService.signup(input), "Invalid name");
    }

    @Test
    public void shouldNotCreatePassengerWithInvalidEmail() {
        Map<String, Object> input = new HashMap<>();
        input.put("name", "John Doe");
        input.put("email", "john.doe" + Math.random() + "@");
        input.put("cpf", "95818705552");
        input.put("isPassenger", true);

        assertThrows(Exception.class, () -> accountService.signup(input), "Invalid email");
    }

    @Test
    public void shouldNotCreateDuplicatePassenger() throws Exception {
        Map<String, Object> input = new HashMap<>();
        input.put("name", "John Doe");
        input.put("email", "john.doe" + Math.random() + "@gmail.com");
        input.put("cpf", "95818705552");
        input.put("isPassenger", true);

        accountService.signup(input);
        assertThrows(Exception.class, () -> accountService.signup(input), "Account already exists");
    }

    @Test
    public void shouldCreateDriver() throws Exception {
        Map<String, Object> input = new HashMap<>();
        input.put("name", "John Doe");
        input.put("email", "john.doe" + Math.random() + "@gmail.com");
        input.put("cpf", "95818705552");
        input.put("carPlate", "AAA9999");
        input.put("isDriver", true);

        Map<String, Object> output = accountService.signup(input);
        assertNotNull(output.get("accountId"));
    }

    @Test
    public void shouldNotCreateDriverWithInvalidCarPlate() {
        Map<String, Object> input = new HashMap<>();
        input.put("name", "John Doe");
        input.put("email", "john.doe" + Math.random() + "@gmail.com");
        input.put("cpf", "95818705552");
        input.put("carPlate", "AAA999");
        input.put("isDriver", true);

        assertThrows(Exception.class, () -> accountService.signup(input), "Invalid plate");
    }
}
