package org.example.socialnetwork.mapper;

import org.example.socialnetwork.dto.user.UserRequestDto;
import org.example.socialnetwork.dto.user.UserResponseDto;
import org.example.socialnetwork.entities.User;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring",
        builder = @Builder(disableBuilder = true))
public interface UserMapper {
    User convertToUser(UserRequestDto userRequestDto);

    UserResponseDto convertToUserResponse(User user);
}
