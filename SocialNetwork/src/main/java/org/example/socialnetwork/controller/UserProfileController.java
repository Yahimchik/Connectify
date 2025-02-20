package org.example.socialnetwork.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.socialnetwork.dto.profile.ProfileCreateRequestDto;
import org.example.socialnetwork.dto.profile.ProfileResponseDto;
import org.example.socialnetwork.dto.profile.ProfileUpdateRequestDto;
import org.example.socialnetwork.service.UserProfileService;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/api/v1/profiles")
public class UserProfileController {

    private final UserProfileService userProfileService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @SecurityRequirement(name = "Bearer Authentication")
    public ProfileResponseDto saveProfile(@Valid @RequestBody ProfileCreateRequestDto profileCreateRequestDto) {
        return userProfileService.saveUserProfile(profileCreateRequestDto);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @SecurityRequirement(name = "Bearer Authentication")
    public ProfileResponseDto getUserProfile() {
        return userProfileService.getUserProfile();
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    @SecurityRequirement(name = "Bearer Authentication")
    public ProfileResponseDto updateProfile(@Valid @RequestBody ProfileUpdateRequestDto profileUpdateRequestDto) {
        return userProfileService.updateCurrentProfile(profileUpdateRequestDto);
    }
}
