package com.gestaodeloja.estoque.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

public record ProdutoRequestDto(

        @NotBlank(message = "O campo nome é obrigatorio.")
        String nome,

        @NotNull(message = "O campo categoria é obrigatorio.")
        Long idCategoria,

        @PositiveOrZero(message = "O campo quantidade deve ser maior ou igual a zero.")
        @NotNull(message = "O campo quantidade é obrigatorio.")
        Integer quantidade,

        @Positive(message = "O campo valor nota fiscal deve ser maior que zero.")
        @NotNull(message = "O campo preco pago é obrigatorio.")
        BigDecimal valorNotaFiscal,

        @Positive(message = "O campo preco venda deve ser maior que zero.")
        @NotNull(message = "O campo preco venda é obrigatorio.")
        BigDecimal precoVenda,

        @PositiveOrZero(message = "O campo estoque minimo deve ser maior ou igual a zero.")
        @NotNull(message = "O campo estoque minimo é obrigatorio.")
        Integer estoqueMinimo,

        @Positive(message = "O campo estoque maximo deve ser maior que zero.")
        @NotNull(message = "O campo estoque maximo é obrigatorio.")
        Integer estoqueMaximo,

        String descricao
) {
}
