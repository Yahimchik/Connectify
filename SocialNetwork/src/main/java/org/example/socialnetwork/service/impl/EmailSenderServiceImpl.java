package org.example.socialnetwork.service.impl;

import org.example.socialnetwork.service.EmailSenderService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailSenderServiceImpl implements EmailSenderService {

    private static final String CONFIRMATION_REGISTRATION_URL = "http://%s/api/v1/users/verify?token=%s";
    private static final String CONFIRMATION_PASSWORD_UPDATING_URL = "http://%s/api/v1/users/password/forgot?token=%s";
    private final JavaMailSender javaMailSender;
    @Value("${spring.mail.username}")
    private String sender;
    @Value("${spring.mail.host}")
    private String host;
    @Value("${spring.mail.port}")
    private int port;
    private final String hostname = host + port;

    public EmailSenderServiceImpl(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    @Override
    public void sendRegistrationConfirmationEmail(String recipient, String token) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(sender);
        message.setTo(recipient);
        message.setSubject("Registration Confirmation");
        message.setText(String.format(CONFIRMATION_REGISTRATION_URL, hostname, token));
        javaMailSender.send(message);
    }

    @Override
    public void sendPasswordResetEmail(String recipient, String token) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(sender);
        message.setTo(recipient);
        message.setSubject("Password Reset");
        message.setText(CONFIRMATION_PASSWORD_UPDATING_URL + token);
        javaMailSender.send(message);
    }
}
