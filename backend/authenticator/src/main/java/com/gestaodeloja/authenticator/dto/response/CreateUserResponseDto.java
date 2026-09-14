package com.gestaodeloja.authenticator.dto.response;

import com.gestaodeloja.authenticator.domain.enums.UserRole;

import java.time.LocalDateTime;

public record CreateUserResponseDto(
        Long id,
        String userName,
        UserRole role,
        boolean enabled,
        LocalDateTime created
) {
}
