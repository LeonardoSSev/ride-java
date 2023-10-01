package com.leonardossev.ride.adapters.inbound.http;

import com.leonardossev.ride.adapters.inbound.http.dto.signup.SignupAccount;
import com.leonardossev.ride.core.model.Account;
import com.leonardossev.ride.core.ports.inbound.GetAccountByIdInboundPort;
import com.leonardossev.ride.core.ports.inbound.SignupInboundPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;

@RestController(value = "/account")
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
public class AccountController {

    private final SignupInboundPort signupInboundPort;

    private final GetAccountByIdInboundPort getAccountByIdInboundPort;

    public AccountController(SignupInboundPort signupInboundPort, GetAccountByIdInboundPort getAccountByIdInboundPort) {
        this.signupInboundPort = signupInboundPort;
        this.getAccountByIdInboundPort = getAccountByIdInboundPort;
    }

    @PostMapping(value = "/signup")
    public ResponseEntity<String> signup(@RequestBody SignupAccount signupAccount) {
        return new ResponseEntity<String>(
                this.signupInboundPort.execute(signupAccount),
                HttpStatus.CREATED
        );
    }

    @PostMapping(value = "/{uuid}")
    public ResponseEntity<Account> findAccountById(@PathVariable String uuid) {
        Optional<Account> optionalAccount = this.getAccountByIdInboundPort.execute(uuid);
        return optionalAccount
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

}
