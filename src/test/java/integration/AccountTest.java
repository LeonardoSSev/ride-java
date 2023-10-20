package integration;

import com.leonardossev.ride.adapters.inbound.http.dto.signup.SignupAccount;
import com.leonardossev.ride.adapters.outbound.persistence.InMemoryAccountClientAdapter;
import com.leonardossev.ride.core.model.Account;
import com.leonardossev.ride.core.ports.inbound.FindAccountByIdInboundPort;
import com.leonardossev.ride.core.ports.outbound.AccountPersistenceOutboundPort;
import com.leonardossev.ride.core.ports.outbound.SendEmailOutboundPort;
import com.leonardossev.ride.core.services.FindAccountByIdService;
import com.leonardossev.ride.core.services.SignupService;
import com.leonardossev.ride.core.services.helper.SignupAccountTestsHelper;
import com.leonardossev.ride.core.validators.CpfValidator;
import org.junit.jupiter.api.BeforeEach;
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
    public void shouldNotCreatePassengerWithInvalidName() {
        SignupAccount input = SignupAccountTestsHelper.buildPassengerWithInvalidName();

        assertThrows(Exception.class, () -> signupService.execute(input), "Invalid name");
    }

    @Test
    public void shouldNotCreatePassengerWithInvalidCpf() {
        SignupAccount input = SignupAccountTestsHelper.buildPassengerWithInvalidCpf();

        assertThrows(Exception.class, () -> signupService.execute(input), "Invalid cpf");
    }

    @Test
    public void shouldNotCreatePassengerWithInvalidEmail() {
        SignupAccount input = SignupAccountTestsHelper.buildPassengerWithInvalidEmail();

        assertThrows(Exception.class, () -> signupService.execute(input), "Invalid email");
    }

    @Test
    public void shouldNotCreateDriverWithInvalidCarPlate() {
        SignupAccount input = SignupAccountTestsHelper.buildDriverWithInvalidCarPlate();

        assertThrows(Exception.class, () -> signupService.execute(input), "Invalid plate");
    }

    @Test
    public void shouldNotCreateDuplicatePassenger() {
        SignupAccount input = SignupAccountTestsHelper.buildSimplePassenger();

        signupService.execute(input);
        assertThrows(Exception.class, () -> signupService.execute(input), "Account already exists");
    }

}
