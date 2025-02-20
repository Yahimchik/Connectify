package org.example.socialnetwork.service;

public interface EmailSenderService {

    void sendRegistrationConfirmationEmail(String recipient, String token);

    void sendPasswordResetEmail(String recipient, String token);
}
