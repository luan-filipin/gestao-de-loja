package com.gestaodeloja.estoque.dto.response;

import java.math.BigDecimal;

public record ProdutoResponseDto(
        Long id,
        String nome,
        Long categoriaId,
        int quantidade,
        BigDecimal valorNotaFiscal,
        BigDecimal precoVenda,
        boolean ativo
) {
}
