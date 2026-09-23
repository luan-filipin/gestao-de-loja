package com.gestaodeloja.estoque.dto.response;

public record ProdutoInativoResponseDto(
        Long id,
        String nome,
        boolean ativo
) {
}
