package com.leonardossev.ride.adapters.inbound.http;

import com.leonardossev.ride.adapters.inbound.http.dto.signup.SignupAccount;
import com.leonardossev.ride.core.model.Account;
import com.leonardossev.ride.core.ports.inbound.FindAccountByIdInboundPort;
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

    private final FindAccountByIdInboundPort findAccountByIdInboundPort;

    public AccountController(SignupInboundPort signupInboundPort, FindAccountByIdInboundPort findAccountByIdInboundPort) {
        this.signupInboundPort = signupInboundPort;
        this.findAccountByIdInboundPort = findAccountByIdInboundPort;
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
        Optional<Account> optionalAccount = this.findAccountByIdInboundPort.execute(uuid);
        return optionalAccount
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

}
