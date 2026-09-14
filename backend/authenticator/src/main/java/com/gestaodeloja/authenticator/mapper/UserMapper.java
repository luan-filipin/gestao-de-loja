package com.gestaodeloja.authenticator.mapper;

import com.gestaodeloja.authenticator.domain.User;
import com.gestaodeloja.authenticator.dto.request.CreateUserRequestDto;
import com.gestaodeloja.authenticator.dto.response.CreateUserResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "enabled", ignore = true)
    @Mapping(target = "created", ignore = true)
    @Mapping(target = "updated", ignore = true)
    User toRequestEntity(CreateUserRequestDto dto);

    @Mapping(target = "userName", source = "username")
    CreateUserResponseDto toResponseDto(User entity);

}
