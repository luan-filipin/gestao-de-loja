package com.gestaodeloja.authenticator.dto.request;

import com.gestaodeloja.authenticator.domain.enums.UserRole;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateUserRequestDto(

        @NotBlank(message = "O campo userName é obrigatorio.")
        String userName,
        @NotBlank(message = "O campo password é obrigatorio.")
        String password,
        @NotNull(message = "O campo role é obrigatorio.")
        UserRole role
) {
}
