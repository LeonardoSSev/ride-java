package com.leonardossev.ride.core.services;

import com.leonardossev.ride.core.validators.CpfValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class AccountServiceTest {

    private AccountService accountService;

    @BeforeEach
    public void setUp() {
        accountService = new AccountService(new CpfValidator());
    }

    @Test
    public void shouldCreatePassenger() throws Exception {
        Map<String, Object> input = new HashMap<>();
        input.put("name", "John Doe");
        input.put("email", "john.doe" + Math.random() + "@gmail.com");
        input.put("cpf", "95818705552");
        input.put("isPassenger", true);

        Map<String, Object> output = accountService.signup(input);
        Map<String, Object> account = accountService.getAccount(output.get("accountId").toString());

        assertNotNull(account.get("accountId"));
        assertEquals(input.get("name"), account.get("name"));
        assertEquals(input.get("email"), account.get("email"));
        assertEquals(input.get("cpf"), account.get("cpf"));
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
