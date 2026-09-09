package com.gestaodeloja.estoque.dto.response;

import java.math.BigDecimal;

public record ProdutoResponseDto(
        String nome,
        Long categoriaId,
        int quantidade,
        BigDecimal valorNotaFiscal,
        BigDecimal precoVenda,
        boolean ativo
) {
}
