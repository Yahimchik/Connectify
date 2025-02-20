package org.example.socialnetwork.service;

import org.example.socialnetwork.dto.profile.ProfileCreateRequestDto;
import org.example.socialnetwork.dto.profile.ProfileResponseDto;
import org.example.socialnetwork.dto.profile.ProfileUpdateRequestDto;

public interface UserProfileService {

    ProfileResponseDto saveUserProfile(ProfileCreateRequestDto profileCreateRequestDto);

    ProfileResponseDto getUserProfile();

    ProfileResponseDto updateCurrentProfile(ProfileUpdateRequestDto profileUpdateRequestDto);

}
