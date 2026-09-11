package com.gestaodeloja.estoque.dto.response;

public record ErroCampoDto(
        String campo,
        String mensagem
) {
}
