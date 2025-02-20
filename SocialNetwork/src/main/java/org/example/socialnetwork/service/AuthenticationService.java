package org.example.socialnetwork.service;

import org.example.socialnetwork.dto.auth.AuthenticationDto;
import org.example.socialnetwork.dto.auth.JwtResponseDto;
import org.example.socialnetwork.dto.auth.RefreshJwtRequestDto;

public interface AuthenticationService {

    JwtResponseDto authenticate(AuthenticationDto authenticationDto);

    JwtResponseDto recreateToken(RefreshJwtRequestDto refreshJwtRequestDto);

    void logout();
}
