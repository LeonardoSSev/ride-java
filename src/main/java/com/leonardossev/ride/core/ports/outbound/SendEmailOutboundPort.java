package com.leonardossev.ride.core.ports.outbound;

import java.util.List;

public interface SendEmailOutboundPort {

    boolean send(String subject, List<String> receivers, String message);
    boolean send(String subject, String receiver, String message);

}
