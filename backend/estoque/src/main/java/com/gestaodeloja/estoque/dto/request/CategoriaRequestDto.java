package com.gestaodeloja.estoque.dto.request;

import jakarta.validation.constraints.NotBlank;

public record CategoriaRequestDto(
        @NotBlank(message = "O nome categoria é obrigatorio.")
        String nome
) {
}
