package org.example.socialnetwork.service;

import org.example.socialnetwork.entities.User;
import org.example.socialnetwork.entities.VerificationToken;

public interface VerificationTokenService {

    VerificationToken saveToken(User user);

    VerificationToken findByToken(String token);

    User findUserByToken(String token);

    boolean validateVerificationToken(String token);

    boolean validateResetPasswordToken(String token);

    void deleteVerificationToken(String token);
}
