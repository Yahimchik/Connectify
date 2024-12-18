package org.example.socialnetwork.service;

import org.example.socialnetwork.entities.RefreshToken;
import org.example.socialnetwork.entities.User;

public interface RefreshTokenService {

    RefreshToken save(RefreshToken token);

    RefreshToken findByUserAndIp(User user, String ip);

    RefreshToken updateRefreshToken(RefreshToken token, String newToken);

    void deleteByUserAndIp(User user, String ip);

}
