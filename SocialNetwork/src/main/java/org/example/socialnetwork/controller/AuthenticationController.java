package org.example.socialnetwork.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.socialnetwork.dto.auth.AuthenticationDto;
import org.example.socialnetwork.dto.auth.JwtResponseDto;
import org.example.socialnetwork.dto.auth.RefreshJwtRequestDto;
import org.example.socialnetwork.service.AuthenticationService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/api/v1/auth")
public class AuthenticationController {
    private final AuthenticationService authenticationService;


    @PostMapping("/login")
    public JwtResponseDto authenticate(@Valid @RequestBody AuthenticationDto authenticationDto) {
        return authenticationService.authenticate(authenticationDto);
    }

    @PostMapping("/token")
    public JwtResponseDto recreateToken(@Valid @RequestBody RefreshJwtRequestDto refreshJwtRequestDto) {
        return authenticationService.recreateToken(refreshJwtRequestDto);
    }

    @PostMapping("/logout")
    @SecurityRequirement(name = "Bearer Authentication")
    public void logout() {
        authenticationService.logout();
    }

}
