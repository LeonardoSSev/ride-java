package integration;

import com.leonardossev.ride.adapters.inbound.http.dto.signup.SignupAccount;
import com.leonardossev.ride.adapters.outbound.persistence.InMemoryAccountClientAdapter;
import com.leonardossev.ride.core.model.Account;
import com.leonardossev.ride.core.ports.outbound.AccountPersistenceOutboundPort;
import com.leonardossev.ride.core.ports.outbound.SendEmailOutboundPort;
import com.leonardossev.ride.core.services.GetAccountByIdService;
import com.leonardossev.ride.core.services.SignupService;
import com.leonardossev.ride.core.validators.CpfValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;

public class AccountTest {

//    private SignupService signupService;
//    private GetAccountByIdService getAccountByIdService;
//
//    @BeforeEach
//    public void setUp() {
//        AccountPersistenceOutboundPort accountPersistenceOutboundPort = new InMemoryAccountClientAdapter();
//        SendEmailOutboundPort sendEmailOutboundPort = mock(SendEmailOutboundPort.class);
//
//        signupService = new SignupService(new CpfValidator(), accountPersistenceOutboundPort, sendEmailOutboundPort);
//        getAccountByIdService = new GetAccountByIdService(accountPersistenceOutboundPort);
//    }
//
//    @Test
//    public void shouldCreatePassenger() {
//        SignupAccount input = buildPassengerAccount();
//
//        String accountId = signupService.execute(input);
//        Optional<Account> accountOptional = getAccountByIdService.execute(accountId);
//
//        assertTrue(accountOptional.isPresent());
//        Account account = accountOptional.get();
//        assertEquals(input.get("name"), account.name());
//        assertEquals(input.get("email"), account.email());
//        assertEquals(input.get("cpf"), account.cpf());
//    }
//
//    private SignupAccount buildPassengerAccount() {
//        return SignupAccount.builder()
//                .name("John Doe")
//                .email("john.doe" + Math.random() + "@gmail.com")
//                .cpf("95818705552")
//                .isPassenger(true)
//                .build();
//    }
//
//    @Test
//    public void shouldNotCreatePassengerWithInvalidCpf() {
//        Map<String, Object> input = new HashMap<>();
//        input.put("name", "John Doe");
//        input.put("email", "john.doe" + Math.random() + "@gmail.com");
//        input.put("cpf", "95818705500");
//        input.put("isPassenger", true);
//
//        assertThrows(Exception.class, () -> accountService.signup(input), "Invalid cpf");
//    }
//
//    @Test
//    public void shouldNotCreatePassengerWithInvalidName() {
//        Map<String, Object> input = new HashMap<>();
//        input.put("name", "John");
//        input.put("email", "john.doe" + Math.random() + "@gmail.com");
//        input.put("cpf", "95818705552");
//        input.put("isPassenger", true);
//
//        assertThrows(Exception.class, () -> accountService.signup(input), "Invalid name");
//    }
//
//    @Test
//    public void shouldNotCreatePassengerWithInvalidEmail() {
//        Map<String, Object> input = new HashMap<>();
//        input.put("name", "John Doe");
//        input.put("email", "john.doe" + Math.random() + "@");
//        input.put("cpf", "95818705552");
//        input.put("isPassenger", true);
//
//        assertThrows(Exception.class, () -> accountService.signup(input), "Invalid email");
//    }
//
//    @Test
//    public void shouldNotCreateDuplicatePassenger() throws Exception {
//        Map<String, Object> input = new HashMap<>();
//        input.put("name", "John Doe");
//        input.put("email", "john.doe" + Math.random() + "@gmail.com");
//        input.put("cpf", "95818705552");
//        input.put("isPassenger", true);
//
//        accountService.signup(input);
//        assertThrows(Exception.class, () -> accountService.signup(input), "Account already exists");
//    }
//
//    @Test
//    public void shouldCreateDriver() throws Exception {
//        Map<String, Object> input = new HashMap<>();
//        input.put("name", "John Doe");
//        input.put("email", "john.doe" + Math.random() + "@gmail.com");
//        input.put("cpf", "95818705552");
//        input.put("carPlate", "AAA9999");
//        input.put("isDriver", true);
//
//        Map<String, Object> output = accountService.signup(input);
//        assertNotNull(output.get("accountId"));
//    }
//
//    @Test
//    public void shouldNotCreateDriverWithInvalidCarPlate() {
//        Map<String, Object> input = new HashMap<>();
//        input.put("name", "John Doe");
//        input.put("email", "john.doe" + Math.random() + "@gmail.com");
//        input.put("cpf", "95818705552");
//        input.put("carPlate", "AAA999");
//        input.put("isDriver", true);
//
//        assertThrows(Exception.class, () -> accountService.signup(input), "Invalid plate");
//    }
}
