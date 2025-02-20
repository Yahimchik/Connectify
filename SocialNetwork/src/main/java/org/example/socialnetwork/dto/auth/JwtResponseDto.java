package org.example.socialnetwork.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(setterPrefix = "add")
public class JwtResponseDto {
    private String accessToken;
    private String refreshToken;
}
