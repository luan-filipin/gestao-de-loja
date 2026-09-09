package com.gestaodeloja.estoque.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record ProdutoRequestDto(

        @NotBlank(message = "O campo nome é obrigatorio.")
        String nome,

        @NotNull(message = "O campo categoria é obrigatorio.")
        Long idCategoria,

        @NotNull(message = "O campo quantidade é obrigatorio.")
        int quantidade,

        @NotNull(message = "O campo preco pago é obrigatorio.")
        BigDecimal valorNotaFiscal,

        @NotNull(message = "O campo preco venda é obrigatorio")
        BigDecimal precoVenda,

        @NotNull(message = "O campo estoque minimo é obrigatorio.")
        int estoqueMinimo,

        @NotNull(message = "o campo estoque maximo é obrigatorio.")
        int estoqueMaximo,

        String descricao
) {
}
