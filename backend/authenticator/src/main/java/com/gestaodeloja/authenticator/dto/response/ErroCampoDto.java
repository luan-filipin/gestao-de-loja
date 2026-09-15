package com.gestaodeloja.authenticator.dto.response;

public record ErroCampoDto(
        String campo,
        String mensagem
) {
}
