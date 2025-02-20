package org.example.socialnetwork.dto.user;


import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.socialnetwork.converter.ToLowerCaseConverter;
import org.example.socialnetwork.validation.email.ValidEmail;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserForgotPasswordDto {
    @NotBlank
    @ValidEmail
    @JsonDeserialize(converter = ToLowerCaseConverter.class)
    @Size(min = 5, max = 256)
    private String email;
}
