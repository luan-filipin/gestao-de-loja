package com.gestaodeloja.estoque.dto.request;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ProdutoFiltrosRequestDto(
        Long id,
        String nome,
        Long categoriaId,
        Integer quantidade,
        BigDecimal valorNotafiscal,
        BigDecimal precoVenda,
        Integer estoqueMinimo,
        Integer estoqueMaximo,
        Boolean ativo,
        LocalDateTime dataCadastro,
        LocalDateTime dataAtualizacao
) {
}
