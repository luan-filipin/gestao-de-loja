package com.gestaodeloja.estoque.dto.request;

import java.math.BigDecimal;

public record ProdutoAtualizadoRequestDto(
        String nome,
        Long categoriaId,
        Integer quantidade,
        BigDecimal valorNotaFiscal,
        BigDecimal precoVenda,
        Integer estoqueMinimo,
        Integer estoqueMaximo,
        String descricao) {
}
