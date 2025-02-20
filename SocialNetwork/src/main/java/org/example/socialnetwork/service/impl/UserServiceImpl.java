package org.example.socialnetwork.service.impl;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.example.socialnetwork.dto.user.*;
import org.example.socialnetwork.entities.User;
import org.example.socialnetwork.entities.VerificationToken;
import org.example.socialnetwork.entities.enums.Role;
import org.example.socialnetwork.entities.enums.UserStatus;
import org.example.socialnetwork.mapper.UserMapper;
import org.example.socialnetwork.repo.UserRepository;
import org.example.socialnetwork.service.EmailSenderService;
import org.example.socialnetwork.service.UserService;
import org.example.socialnetwork.service.VerificationTokenService;
import org.example.socialnetwork.service.exception.UpdatePasswordException;
import org.example.socialnetwork.util.UuidGenerator;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private static final String USER_NOT_FOUND_EXCEPTION = "User not found";
    private static final String USER_ALREADY_EXIST_EXCEPTION = "User exist";
    private static final String WRONG_PASSWORD_EXCEPTION = "Wrong password";
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final UuidGenerator uuidGenerator;
    private final PasswordEncoder passwordEncoder;
    private final VerificationTokenService verificationTokenService;
    private final EmailSenderService emailSenderService;

    @Override
    @Transactional
    public UserResponseDto saveUser(UserRequestDto userRequestDto) {
        checkEmailDuplicates(userRequestDto);
        User user = fillUser(userRequestDto);
        System.out.println(userRepository.save(user));
        VerificationToken verificationToken = verificationTokenService.saveToken(user);
        emailSenderService.sendRegistrationConfirmationEmail(user.getEmail(), verificationToken.getToken());
        System.out.println(userMapper.convertToUserResponse(user));
        return userMapper.convertToUserResponse(user);
    }

    @Override
    @Transactional
    public User getUserByEmail(String email) {
        System.out.println(userRepository.findUserByEmail(email));
        return userRepository.findUserByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException(USER_NOT_FOUND_EXCEPTION));
    }

    @Override
    @Transactional
    public void verifyUserAccount(String token) {
        verificationTokenService.validateVerificationToken(token);
        User user = verificationTokenService.findUserByToken(token);
        user.setStatus(UserStatus.ACTIVE);
        userRepository.save(user);
        verificationTokenService.deleteVerificationToken(token);
    }

    @Override
    @Transactional
    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder
                .getContext()
                .getAuthentication();
        String email = authentication.getName();
        return getUserByEmail(email);
    }

    @Override
    @Transactional
    public void forgotPassword(UserForgotPasswordDto userForgotPasswordDto) {
        String email = userForgotPasswordDto.getEmail();
        User user = getUserByEmail(email);
        VerificationToken verificationToken = verificationTokenService.saveToken(user);
        emailSenderService.sendPasswordResetEmail(user.getEmail(), verificationToken.getToken());
    }

    @Override
    @Transactional
    public void creatPasswordResetToken(UserCreatePasswordDto userCreatePasswordDto, String token) {
        verificationTokenService.validateVerificationToken(token);

    }

    @Override
    @Transactional
    public void updatePassword(UserUpdatePasswordDto userUpdatePasswordDto) {
        User currentUser = getCurrentUser();
        if (!isCurrentPasswordMatches(userUpdatePasswordDto.getPasswordBefore(), currentUser.getPassword())) {
            throw new UpdatePasswordException(WRONG_PASSWORD_EXCEPTION);
        }
        String encryptionPassword = passwordEncoder.encode(userUpdatePasswordDto.getPasswordAfter());
        currentUser.setPassword(encryptionPassword);
        userRepository.save(currentUser);
    }

    private User fillUser(UserRequestDto userRequestDto) {
        return User.builder()
                .addId(uuidGenerator.generateUuid())
                .addUsername(userRequestDto.getUsername())
                .addEmail(userRequestDto.getEmail())
                .addPassword(passwordEncoder.encode(userRequestDto.getPassword()))
                .addRole(Role.USER)
                .addStatus(UserStatus.WAITING_ACTIVATION)
                .build();

    }

    private void checkEmailDuplicates(UserRequestDto userRequestDto) {
        String email = userRequestDto.getEmail();
        if (isUserExist(email)) {
            throw new EntityExistsException(USER_ALREADY_EXIST_EXCEPTION + email);
        }
    }

    private boolean isUserExist(String email) {
        return userRepository.findUserByEmail(email).isPresent();
    }

    private boolean isCurrentPasswordMatches(String matchingPassword, String currentPassword) {
        return passwordEncoder.matches(currentPassword, matchingPassword);
    }
}
