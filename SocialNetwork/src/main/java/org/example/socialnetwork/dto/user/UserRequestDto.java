package org.example.socialnetwork.dto.user;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.socialnetwork.converter.ToLowerCaseConverter;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserRequestDto {
    @NotBlank
    @JsonDeserialize(converter = ToLowerCaseConverter.class)
    @Size(min = 5, max = 256)
    private String username;
    @NotBlank
    @Size(min = 5, max = 65)
    private String email;
    @NotBlank
    @Size(min = 5, max = 256)
    private String password;
}
