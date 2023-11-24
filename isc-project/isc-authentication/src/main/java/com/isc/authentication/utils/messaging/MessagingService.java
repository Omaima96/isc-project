package com.isc.authentication.utils.messaging;

import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Service;

@Service
@Log
public class MessagingService {

    @Autowired
    private StreamBridge streamBridge;

    public void sendEmail(MailMessage message) {
        streamBridge.send("mail-out-0", message);
    }

    public void finalizeInvitation(String email) {
        streamBridge.send("invito-out-0", email);
    }

}
