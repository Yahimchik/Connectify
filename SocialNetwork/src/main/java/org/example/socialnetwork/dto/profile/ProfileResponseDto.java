package org.example.socialnetwork.dto.profile;

import lombok.*;
import org.example.socialnetwork.dto.user.UserResponseDto;
import org.example.socialnetwork.entities.enums.PrivacyStatus;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(setterPrefix = "add")
public class ProfileResponseDto {
    private String name;
    private String avatarUrl;
    private String description;
    private String country;
    private String city;
    private String jobTitle;
    private String contactInformation;
    private PrivacyStatus privacyStatus;
    private UserResponseDto userResponseDto;
}
