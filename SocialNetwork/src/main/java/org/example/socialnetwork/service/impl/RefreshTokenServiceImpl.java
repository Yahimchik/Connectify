package org.example.socialnetwork.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.example.socialnetwork.entities.RefreshToken;
import org.example.socialnetwork.entities.User;
import org.example.socialnetwork.repo.RefreshTokenRepository;
import org.example.socialnetwork.security.jwt.JwtTokenProvider;
import org.example.socialnetwork.service.RefreshTokenService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RefreshTokenServiceImpl implements RefreshTokenService {
    private static final String TOKEN_NOT_FOUND_EXCEPTION = "Specified token is not found";
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public RefreshToken save(RefreshToken token) {
        return refreshTokenRepository.save(token);
    }

    @Override
    public RefreshToken findByUserAndIp(User user, String ip) {
        return refreshTokenRepository.findByUserAndIp(user, ip)
                .orElseThrow(() -> new EntityNotFoundException(TOKEN_NOT_FOUND_EXCEPTION));
    }

    @Override
    public RefreshToken updateRefreshToken(RefreshToken token, String newToken) {
        token.setToken(newToken);
        token.setExpires(jwtTokenProvider.getExpirationDate(newToken));
        return refreshTokenRepository.save(token);
    }

    @Override
    public void deleteByUserAndIp(User user, String ip) {
        refreshTokenRepository.deleteByUserAndIp(user, ip);
    }
}
