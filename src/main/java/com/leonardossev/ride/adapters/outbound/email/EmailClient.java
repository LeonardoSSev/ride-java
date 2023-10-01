package com.leonardossev.ride.adapters.outbound.email;

import com.leonardossev.ride.core.ports.outbound.SendEmailOutboundPort;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class EmailClient implements SendEmailOutboundPort {

    @Override
    public boolean send(String subject, List<String> receivers, String message) {
        for (String receiver : receivers) {
            this.send(subject, receiver, message);
        }

        return true;
    }

    @Override
    public boolean send(String subject, String receiver, String message) {
        System.out.printf("%s %s %s", receiver, subject, message);

        return true;
    }
}
