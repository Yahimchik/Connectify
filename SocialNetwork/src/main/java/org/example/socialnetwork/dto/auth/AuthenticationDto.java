package org.example.socialnetwork.dto.auth;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.socialnetwork.converter.ToLowerCaseConverter;
import org.example.socialnetwork.validation.email.ValidEmail;
import org.example.socialnetwork.validation.password.ValidPassword;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(setterPrefix = "add")
public class AuthenticationDto {
    @NotBlank
    @ValidEmail
    @JsonDeserialize(converter = ToLowerCaseConverter.class)
    @Size(min = 5, max = 256)
    private String email;
    @NotBlank
    @ValidPassword
    @Size(min = 5, max = 65)
    private String password;
}
