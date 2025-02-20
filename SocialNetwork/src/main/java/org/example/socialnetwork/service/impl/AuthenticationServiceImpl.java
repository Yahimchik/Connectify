package org.example.socialnetwork.service.impl;

import jakarta.persistence.EntityExistsException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.example.socialnetwork.dto.auth.AuthenticationDto;
import org.example.socialnetwork.dto.auth.JwtResponseDto;
import org.example.socialnetwork.dto.auth.RefreshJwtRequestDto;
import org.example.socialnetwork.entities.RefreshToken;
import org.example.socialnetwork.entities.User;
import org.example.socialnetwork.repo.RefreshTokenRepository;
import org.example.socialnetwork.security.jwt.JwtTokenProvider;
import org.example.socialnetwork.service.AuthenticationService;
import org.example.socialnetwork.service.RefreshTokenService;
import org.example.socialnetwork.service.UserService;
import org.example.socialnetwork.service.exception.UserAuthenticationProcessingException;
import org.example.socialnetwork.util.UuidGenerator;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthenticationServiceImpl implements AuthenticationService {

    private static final String AUTHENTICATION_EXCEPTION = "Invalid username/password supplied";
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final RefreshTokenRepository refreshTokenRepository;
    private final UuidGenerator uuidGenerator;
    private final RefreshTokenService refreshTokenService;

    @Override
    @Transactional
    public JwtResponseDto authenticate(AuthenticationDto authenticationDto) {
        try {
            System.out.println(new UsernamePasswordAuthenticationToken(
                    authenticationDto.getEmail(),
                    authenticationDto.getPassword()));
            Authentication authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(
                            authenticationDto.getEmail(),
                            authenticationDto.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authentication);
            User user = userService.getUserByEmail(authenticationDto.getEmail());
            String accessToken = jwtTokenProvider.generateAccessToken(user);
            String refreshToken = jwtTokenProvider.generateRefreshToken(user);
            String ip = getUserIp();

            RefreshToken newToken = buildRefreshToken(user, refreshToken);
            if (refreshTokenRepository.findByUserAndIp(user, ip).isPresent()) {
                refreshTokenService.deleteByUserAndIp(user, ip);
            }
            refreshTokenService.save(newToken);
            return new JwtResponseDto(accessToken, refreshToken);
        } catch (AuthenticationException e) {
            throw new UserAuthenticationProcessingException(AUTHENTICATION_EXCEPTION);
        }
    }

    @Override
    @Transactional
    public JwtResponseDto recreateToken(RefreshJwtRequestDto refreshJwtRequestDto) {
        String requestToken = refreshJwtRequestDto.getRefreshToken();
        if (jwtTokenProvider.validateRefreshToken(requestToken)) {
            String email = jwtTokenProvider.getLoginFromRefreshToken(requestToken);
            String ip = getUserIp();
            User user = userService.getUserByEmail(email);
            RefreshToken refreshToken = refreshTokenService.findByUserAndIp(user, ip);
            String tokenValue = refreshToken.getToken();

            if (Objects.nonNull(tokenValue) && tokenValue.equals(requestToken)) {
                String accessToken = jwtTokenProvider.generateAccessToken(user);
                String newRefreshToken = jwtTokenProvider.generateRefreshToken(user);
                refreshTokenService.updateRefreshToken(refreshToken, newRefreshToken);
                return new JwtResponseDto(accessToken, newRefreshToken);
            }
        }
        throw new EntityExistsException("JWT token is expired or invalid");
    }

    @Override
    @Transactional
    public void logout() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (Objects.nonNull(authentication)) {
            String ip = getUserIp();
            User user = userService.getUserByEmail(authentication.getName());
            refreshTokenService.deleteByUserAndIp(user, ip);
        }
    }

    private RefreshToken buildRefreshToken(User user, String refreshToken) {
        return RefreshToken.builder()
                .addId(uuidGenerator.generateUuid())
                .addUser(user)
                .addToken(refreshToken)
                .addIpAddress(getUserIp())
                .addExpires(jwtTokenProvider.getExpirationDate(refreshToken))
                .build();
    }

    private String getUserIp() {
        HttpServletRequest request =
                ((ServletRequestAttributes) RequestContextHolder
                        .currentRequestAttributes())
                        .getRequest();
        return request.getRemoteAddr();
    }
}
