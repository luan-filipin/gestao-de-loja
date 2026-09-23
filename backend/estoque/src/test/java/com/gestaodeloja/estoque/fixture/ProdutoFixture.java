package com.gestaodeloja.estoque.fixture;

import com.gestaodeloja.estoque.domain.Categoria;
import com.gestaodeloja.estoque.domain.Produto;
import com.gestaodeloja.estoque.dto.request.ProdutoAtualizadoRequestDto;
import com.gestaodeloja.estoque.dto.request.ProdutoRequestDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class ProdutoFixture {

    public static Produto criaProduto(
            Long id,
            String nome,
            int quantidade,
            BigDecimal valorNotaFiscal,
            BigDecimal precoVenda,
            int estoqueMinimo,
            int estoqueMaximo,
            String descricao,
            boolean ativo,
            LocalDateTime dataCadastro,
            LocalDateTime dataAtualizacao) {
        Categoria categoria = Categoria.builder()
                .id(1L)
                .nome("Geral")
                .build();

        return Produto.builder()
                .id(id)
                .nome(nome)
                .categoria(categoria)
                .quantidade(quantidade)
                .valorNotaFiscal(valorNotaFiscal)
                .precoVenda(precoVenda)
                .estoqueMinimo(estoqueMinimo)
                .estoqueMaximo(estoqueMaximo)
                .descricao(descricao)
                .ativo(ativo)
                .dataCadastro(dataCadastro)
                .dataAtualizacao(dataAtualizacao)
                .build();
    }

    public static ProdutoRequestDto criaProdutoRequestDto(
            String nome,
            Long idCategoria,
            Integer quantidade,
            BigDecimal valorNotaFiscal,
            BigDecimal precoVenda,
            Integer estoqueMinimo,
            Integer estoqueMaximom,
            String descricao) {

        return new ProdutoRequestDto(
                nome,
                idCategoria,
                quantidade,
                valorNotaFiscal,
                precoVenda,
                estoqueMinimo,
                estoqueMaximom,
                descricao);
    }

    public static Page<Produto> criaPageDeProdutos(
            Pageable pageable,
            List<Produto> produtos) {

        return new PageImpl<>(
                produtos,
                pageable,
                produtos.size()
        );
    }

    public static ProdutoAtualizadoRequestDto criaProdutoAtualizadoRequestDto(
            String nome,
            Long idCategoria,
            Integer quantidade,
            BigDecimal valorNotaFiscal,
            BigDecimal precoVenda,
            Integer estoqueMinimo,
            Integer estoqueMaximom,
            String descricao) {
        return new ProdutoAtualizadoRequestDto(
                nome,
                idCategoria,
                quantidade,
                valorNotaFiscal,
                precoVenda,
                estoqueMinimo,
                estoqueMaximom,
                descricao);
    }
}
