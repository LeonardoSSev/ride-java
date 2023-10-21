package integration;

import com.leonardossev.ride.adapters.inbound.http.dto.signup.SignupAccount;
import com.leonardossev.ride.adapters.outbound.persistence.account.InMemoryAccountClientAdapter;
import com.leonardossev.ride.core.model.Account.Account;
import com.leonardossev.ride.core.ports.inbound.FindAccountByIdInboundPort;
import com.leonardossev.ride.core.ports.outbound.AccountPersistenceOutboundPort;
import com.leonardossev.ride.core.ports.outbound.SendEmailOutboundPort;
import com.leonardossev.ride.core.services.FindAccountByIdService;
import com.leonardossev.ride.core.services.SignupService;
import com.leonardossev.ride.core.services.helper.SignupAccountTestsHelper;
import com.leonardossev.ride.core.validators.CpfValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;

public class AccountTest {

    private SignupService signupService;
    private FindAccountByIdInboundPort findAccountByIdService;

    @BeforeEach
    public void setUp() {
        AccountPersistenceOutboundPort accountPersistenceOutboundPort = new InMemoryAccountClientAdapter();
        SendEmailOutboundPort sendEmailOutboundPort = mock(SendEmailOutboundPort.class);

        signupService = new SignupService(new CpfValidator(), accountPersistenceOutboundPort, sendEmailOutboundPort);
        findAccountByIdService = new FindAccountByIdService(accountPersistenceOutboundPort);
    }

    @Test
    @DisplayName("Should create a passenger")
    public void shouldCreatePassenger() {
        SignupAccount input = SignupAccountTestsHelper.buildSimplePassenger();

        String accountId = signupService.execute(input);
        Optional<Account> accountOptional = findAccountByIdService.execute(accountId);

        assertTrue(accountOptional.isPresent());
        Account output = accountOptional.get();
        assertEquals(input.name(), output.name());
        assertEquals(input.email(), output.email());
        assertEquals(input.cpf(), output.cpf());
        assertEquals(input.carPlate(), output.carPlate());
        assertEquals(input.isPassenger(), output.isPassenger());
        assertEquals(input.isDriver(), output.isDriver());
        assertEquals(input.isVerified(), output.isVerified());
    }

    @Test
    @DisplayName("Should create a driver")
    public void shouldCreateDriver() {
        SignupAccount input = SignupAccountTestsHelper.buildSimpleDriver();

        String accountId = signupService.execute(input);
        Optional<Account> accountOptional = findAccountByIdService.execute(accountId);

        assertTrue(accountOptional.isPresent());
        Account output = accountOptional.get();
        assertEquals(input.name(), output.name());
        assertEquals(input.email(), output.email());
        assertEquals(input.cpf(), output.cpf());
        assertEquals(input.carPlate(), output.carPlate());
        assertEquals(input.isPassenger(), output.isPassenger());
        assertEquals(input.isDriver(), output.isDriver());
        assertEquals(input.isVerified(), output.isVerified());
    }

    @Test
    @DisplayName("Should not create an account with an invalid name")
    public void shouldNotCreatePassengerWithInvalidName() {
        SignupAccount input = SignupAccountTestsHelper.buildPassengerWithInvalidName();

        assertThrows(Exception.class, () -> signupService.execute(input), "Invalid name");
    }

    @Test
    @DisplayName("Should not create an account with an invalid CPF")
    public void shouldNotCreatePassengerWithInvalidCpf() {
        SignupAccount input = SignupAccountTestsHelper.buildPassengerWithInvalidCpf();

        assertThrows(Exception.class, () -> signupService.execute(input), "Invalid CPF");
    }

    @Test
    @DisplayName("Should not create an account with an invalid email")
    public void shouldNotCreatePassengerWithInvalidEmail() {
        SignupAccount input = SignupAccountTestsHelper.buildPassengerWithInvalidEmail();

        assertThrows(Exception.class, () -> signupService.execute(input), "Invalid email");
    }

    @Test
    @DisplayName("Should not create a driver account with an invalid car plate")
    public void shouldNotCreateDriverWithInvalidCarPlate() {
        SignupAccount input = SignupAccountTestsHelper.buildDriverWithInvalidCarPlate();

        assertThrows(Exception.class, () -> signupService.execute(input), "Invalid plate");
    }

    @Test
    @DisplayName("Should not create duplicate accounts")
    public void shouldNotCreateDuplicatePassenger() {
        SignupAccount input = SignupAccountTestsHelper.buildSimplePassenger();

        signupService.execute(input);
        assertThrows(Exception.class, () -> signupService.execute(input), "Account already exists");
    }

}
