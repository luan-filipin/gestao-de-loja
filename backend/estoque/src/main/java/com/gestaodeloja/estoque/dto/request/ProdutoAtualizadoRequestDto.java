package com.gestaodeloja.estoque.dto.request;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

public record ProdutoAtualizadoRequestDto(
        String nome,

        Long categoriaId,

        @PositiveOrZero(message = "O campo quantidade deve ser maior ou igual a zero.")
        Integer quantidade,

        @Positive(message = "O campo valor nota fiscal deve ser maior que zero.")
        BigDecimal valorNotaFiscal,

        @Positive(message = "O campo preco venda deve ser maior que zero.")
        BigDecimal precoVenda,

        @PositiveOrZero(message = "O campo estoque minimo deve ser maior ou igual a zero.")
        Integer estoqueMinimo,

        @PositiveOrZero(message = "O campo estoque maximo deve ser maior ou igual a zero.")
        Integer estoqueMaximo,

        String descricao) {
}
