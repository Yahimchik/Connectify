package org.example.socialnetwork.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.socialnetwork.validation.password.ValidPassword;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserUpdatePasswordDto {
    @NotBlank
    @ValidPassword
    @Size(min = 5, max = 65)
    private String passwordBefore;

    @NotBlank
    @ValidPassword
    @Size(min = 5, max = 65)
    private String passwordAfter;
}
