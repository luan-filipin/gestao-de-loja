package com.gestaodeloja.authenticator.dto.request;

import jakarta.validation.constraints.NotBlank;

public record LoginRequestDto(
        @NotBlank(message = "O campo userName é obrigatorio.")
        String userName,
        @NotBlank(message = "O campo password é obrigatorio.")
        String password
) {
}
