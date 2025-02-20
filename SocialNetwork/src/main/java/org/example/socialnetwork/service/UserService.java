package org.example.socialnetwork.service;

import org.example.socialnetwork.dto.user.*;
import org.example.socialnetwork.entities.User;

public interface UserService {

    UserResponseDto saveUser(UserRequestDto userRequestDto);

    User getUserByEmail(String email);

    void verifyUserAccount(String token);

    User getCurrentUser();

    void forgotPassword(UserForgotPasswordDto userForgotPasswordDto);

    void creatPasswordResetToken(UserCreatePasswordDto userCreatePasswordDto, String token);

    void updatePassword(UserUpdatePasswordDto userUpdatePasswordDto);
}
