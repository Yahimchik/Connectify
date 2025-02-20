package org.example.socialnetwork.service.impl;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.example.socialnetwork.entities.User;
import org.example.socialnetwork.entities.VerificationToken;
import org.example.socialnetwork.entities.enums.UserStatus;
import org.example.socialnetwork.repo.VerificationTokenRepository;
import org.example.socialnetwork.service.VerificationTokenService;
import org.example.socialnetwork.util.KeyGenerator;
import org.example.socialnetwork.util.UuidGenerator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class VerificationTokenServiceImpl implements VerificationTokenService {

    private static final String TOKEN_NOT_FOUND_EXCEPTION = "Specified token is not found";
    private static final String TOKEN_IS_EXPIRED_EXCEPTION = "Token is expired";
    private static final String USER_ACTIVE_EXCEPTION = "User is already active";
    private static final String USER_NOT_FOUND_EXCEPTION = "User is not found";
    private static final long EXPIRATION = 4;
    private static final int KEY_LENGTH = 15;
    private final VerificationTokenRepository verificationTokenRepository;
    private final UuidGenerator uuidGenerator;
    private final KeyGenerator keyGenerator;


    @Override
    @Transactional
    public VerificationToken saveToken(User user) {
        VerificationToken verificationToken = generateVerificationToken(user);
        return verificationTokenRepository.save(verificationToken);
    }

    @Override
    public VerificationToken findByToken(String token) {
        return verificationTokenRepository.findByToken(token)
                .orElseThrow(() -> new EntityNotFoundException(TOKEN_NOT_FOUND_EXCEPTION));
    }

    @Override
    public User findUserByToken(String token) {
        return verificationTokenRepository.findUserByToken(token)
                .orElseThrow(() -> new EntityNotFoundException(USER_NOT_FOUND_EXCEPTION));
    }

    @Override
    public boolean validateVerificationToken(String token) {
        VerificationToken verificationToken = findByToken(token);
        validateTokenByExpirationDate(verificationToken);
        validateTokenByUserStatus(verificationToken.getUser());
        return true;
    }

    @Override
    public boolean validateResetPasswordToken(String token) {
        return false;
    }

    @Override
    @Transactional
    public void deleteVerificationToken(String token) {
        if (verificationTokenRepository.findByToken(token).isEmpty()) {
            throw new EntityNotFoundException(TOKEN_NOT_FOUND_EXCEPTION);
        }
        verificationTokenRepository.deleteByToken(token);
    }

    private VerificationToken generateVerificationToken(User user) {
        String tokenValue = keyGenerator.generateKey(KEY_LENGTH);
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expiration = now.plusHours(EXPIRATION);
        return VerificationToken.builder()
                .addId(uuidGenerator.generateUuid())
                .addToken(tokenValue)
                .addUser(user)
                .addExpirationDate(expiration)
                .build();
    }

    private void validateTokenByExpirationDate(VerificationToken verificationToken) {
        if (verificationToken.getExpirationDate().isBefore(LocalDateTime.now())) {
            throw new EntityExistsException(TOKEN_IS_EXPIRED_EXCEPTION);
        }
    }

    private void validateTokenByUserStatus(User user) {
        if (user.getStatus().equals(UserStatus.ACTIVE)) {
            throw new EntityExistsException(USER_ACTIVE_EXCEPTION);
        }
    }
}
