package org.example.socialnetwork.dto.profile;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.socialnetwork.entities.enums.PrivacyStatus;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(setterPrefix = "add")
public class ProfileUpdateRequestDto {
    private String name;
    private String avatarUrl;
    private String description;
    private String country;
    private String city;
    private String jobTitle;
    private String contactInformation;
    private PrivacyStatus privacyStatus;
}
