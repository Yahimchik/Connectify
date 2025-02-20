package org.example.socialnetwork.mapper;

import org.example.socialnetwork.dto.profile.ProfileCreateRequestDto;
import org.example.socialnetwork.dto.profile.ProfileResponseDto;
import org.example.socialnetwork.entities.UserProfile;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring",
        builder = @Builder(disableBuilder = true))
public interface UserProfileMapper {
    UserProfile convertToProfile(ProfileCreateRequestDto profileCreateRequestDto);

    @Mapping(source = "user", target = "userResponseDto")
    ProfileResponseDto convertToProfileResponse(UserProfile userProfile);
}