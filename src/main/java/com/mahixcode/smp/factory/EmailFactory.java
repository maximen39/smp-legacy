/*
 * Copyright (C) 2019 maximen39
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.mahixcode.smp.factory;

import com.mahixcode.smp.config.json.Config;
import org.springframework.stereotype.Component;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author maximen39
 */
@Component
public class EmailFactory {
    private ExecutorService executorService = Executors.newSingleThreadExecutor();
    private Properties sessionProperties = new Properties();

    private final Config config;

    public EmailFactory(Config config) {
        this.config = config;
        sessionProperties.put("mail.smtp.host", config.emailHost);
        sessionProperties.put("mail.smtp.socketFactory.port", config.emailPort);
        sessionProperties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        sessionProperties.put("mail.smtp.auth", "true");
        sessionProperties.put("mail.smtp.port", config.emailPort);
    }

    public void sendMessage(String recipientEmail, String subject, String text) {
        Session session = Session.getInstance(sessionProperties, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(config.emailUsername, config.emailPassword);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setHeader("Content-Type", "text/plain; charset=UTF-8");
            message.setFrom(new InternetAddress(config.emailUsername, config.emailName));
            message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(recipientEmail)
            );
            message.setSubject(subject);
            message.setSentDate(new Date());
            message.setContent(text, "text/plain; charset=UTF-8");
            sendMessage(message);
        } catch (MessagingException | UnsupportedEncodingException ex) {
            ex.printStackTrace();
        }
    }

    private void sendMessage(Message message) {
        executorService.execute(() -> {
            try {
                Transport.send(message);
            } catch (MessagingException e) {
                e.printStackTrace();
            }
        });
    }
}
