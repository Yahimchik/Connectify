package org.example.socialnetwork.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.socialnetwork.dto.user.*;
import org.example.socialnetwork.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@Validated
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/registration")
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponseDto registerUser(@Valid @RequestBody UserRequestDto userRequestDto) {
        System.out.println(userRequestDto);
        return userService.saveUser(userRequestDto);
    }

    @PostMapping("/verify")
    @ResponseStatus(HttpStatus.OK)
    public void verifyUserAccount(@RequestParam(value = "token") String token) {
        userService.verifyUserAccount(token);
    }

    @PostMapping("/password/forgot")
    @ResponseStatus(HttpStatus.OK)
    public void forgotPassword(@Valid @RequestBody UserForgotPasswordDto userForgotPasswordDto) {
        userService.forgotPassword(userForgotPasswordDto);
    }

    @PatchMapping("/password/recovery")
    @ResponseStatus(HttpStatus.OK)
    public void resetPasswordByEmail(@Valid @RequestBody UserCreatePasswordDto userCreatePasswordDto,
                                     @RequestParam(value = "token") String token) {
        userService.creatPasswordResetToken(userCreatePasswordDto, token);
    }

    @PatchMapping("/password/new")
    @ResponseStatus(HttpStatus.OK)
    @SecurityRequirement(name = "Bearer Authentication")
    public void updatePassword(@Valid @RequestBody UserUpdatePasswordDto userUpdatePasswordDto) {
        userService.updatePassword(userUpdatePasswordDto);
    }
}
