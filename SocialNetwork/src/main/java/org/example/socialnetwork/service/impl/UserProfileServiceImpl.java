package org.example.socialnetwork.service.impl;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.example.socialnetwork.dto.profile.ProfileCreateRequestDto;
import org.example.socialnetwork.dto.profile.ProfileResponseDto;
import org.example.socialnetwork.dto.profile.ProfileUpdateRequestDto;
import org.example.socialnetwork.entities.User;
import org.example.socialnetwork.entities.UserProfile;
import org.example.socialnetwork.mapper.UserProfileMapper;
import org.example.socialnetwork.repo.UserProfileRepository;
import org.example.socialnetwork.service.UserProfileService;
import org.example.socialnetwork.service.UserService;
import org.example.socialnetwork.util.UuidGenerator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserProfileServiceImpl implements UserProfileService {

    private final UserProfileRepository userProfileRepository;
    private final UserProfileMapper userProfileMapper;
    private final UuidGenerator uuidGenerator;
    private final UserService userService;

    @Override
    @Transactional
    public ProfileResponseDto saveUserProfile(ProfileCreateRequestDto profileCreateRequestDto) {
        User currentUser = userService.getCurrentUser();
        if (isProfilePresent(currentUser)) {
            throw new EntityExistsException("User already exists");
        }
        UserProfile userProfile = userProfileMapper.convertToProfile(profileCreateRequestDto);
        userProfile.setId(uuidGenerator.generateUuid());
        userProfile.setUser(currentUser);
        userProfile.setAvatarUrl(profileCreateRequestDto.getAvatarUrl());
        userProfile.setDescription(profileCreateRequestDto.getDescription());
        userProfile.setCountry(profileCreateRequestDto.getCountry());
        userProfile.setCity(profileCreateRequestDto.getCity());
        userProfile.setJobTitle(profileCreateRequestDto.getJobTitle());
        userProfile.setContactInformation(profileCreateRequestDto.getContactInformation());
        userProfile.setPrivacyStatus(profileCreateRequestDto.getPrivacyStatus());
        UserProfile savedUserProfile = userProfileRepository.save(userProfile);
        return userProfileMapper.convertToProfileResponse(savedUserProfile);
    }


    @Override
    public ProfileResponseDto getUserProfile() {
        UserProfile userProfile = findProfileByCurrentUser();
//        return userProfileMapper.convertToProfileResponse(userProfile);
        return new ProfileResponseDto();
    }

    @Override
    @Transactional
    public ProfileResponseDto updateCurrentProfile(ProfileUpdateRequestDto profileUpdateRequestDto) {
        UserProfile currentProfile = findProfileByCurrentUser();
        currentProfile.setAvatarUrl(profileUpdateRequestDto.getAvatarUrl());
        currentProfile.setDescription(profileUpdateRequestDto.getDescription());
        currentProfile.setCountry(profileUpdateRequestDto.getCountry());
        currentProfile.setCity(profileUpdateRequestDto.getCity());
        currentProfile.setJobTitle(profileUpdateRequestDto.getJobTitle());
        currentProfile.setContactInformation(profileUpdateRequestDto.getContactInformation());
        currentProfile.setPrivacyStatus(profileUpdateRequestDto.getPrivacyStatus());
        UserProfile userProfile = userProfileRepository.save(currentProfile);
//        return userProfileMapper.convertToProfileResponse(userProfile);
        return new ProfileResponseDto();
    }

    private boolean isProfilePresent(User user) {
        return userProfileRepository.findByUser(user).isPresent();
    }

    private UserProfile findProfileByCurrentUser() {
        User currentUser = userService.getCurrentUser();
        return userProfileRepository.findByUser(currentUser)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
    }
}
