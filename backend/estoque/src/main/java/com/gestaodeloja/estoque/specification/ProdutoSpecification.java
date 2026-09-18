package com.gestaodeloja.estoque.specification;

import com.gestaodeloja.estoque.domain.Produto;
import com.gestaodeloja.estoque.dto.request.ProdutoFiltrosRequestDto;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ProdutoSpecification {

    public static Specification<Produto> comId(Long id) {
        return quandoPresente(id, (root, query, cb) -> cb.equal(root.get("id"), id));
    }

    public static Specification<Produto> comNome(String nome) {
        return quandoPresente(nome, (root, query, cb) -> cb.like(cb.lower(root.get("nome")), "%" + nome.toLowerCase() + "%"));
    }

    public static Specification<Produto> comCategoria(Long id) {
        return quandoPresente(id, (root, query, cb) -> cb.equal(root.get("categoria").get("id"), id));
    }

    public static Specification<Produto> comQuantidade(Integer quantidade) {
        return quandoPresente(quantidade, (root, query, cb) -> cb.equal(root.get("quantidade"), quantidade));
    }

    public static Specification<Produto> comValorNotaFiscal(BigDecimal valorNotaFiscal) {
        return quandoPresente(valorNotaFiscal, (root, query, cb) -> cb.equal(root.get("valorNotaFiscal"), valorNotaFiscal));
    }

    public static Specification<Produto> comPrecoVenda(BigDecimal precoVenda) {
        return quandoPresente(precoVenda, (root, query, cb) -> cb.equal(root.get("precoVenda"), precoVenda));
    }

    public static Specification<Produto> comEstoqueMinimo(Integer estoqueMinimo) {
        return quandoPresente(estoqueMinimo, (root, query, cb) -> cb.equal(root.get("estoqueMinimo"), estoqueMinimo));
    }

    public static Specification<Produto> comEstoqueMaximo(Integer estoqueMaximo) {
        return quandoPresente(estoqueMaximo, (root, query, cb) -> cb.equal(root.get("estoqueMaximo"), estoqueMaximo));
    }

    public static Specification<Produto> comAtivo(Boolean ativo) {
        return quandoPresente(ativo, (root, query, cb) -> cb.equal(root.get("ativo"), ativo));
    }

    public static Specification<Produto> comDataCadastro(LocalDateTime dataCadastro) {
        return quandoPresente(dataCadastro, (root, query, cb) -> cb.equal(root.get("dataCadastro"), dataCadastro));
    }

    public static Specification<Produto> comDataAtualizacao(LocalDateTime dataAtualizacao) {
        return quandoPresente(dataAtualizacao, (root, query, cb) -> cb.equal(root.get("dataAtualizacao"), dataAtualizacao));
    }

    public static Specification<Produto> comFiltros(ProdutoFiltrosRequestDto dto) {
        return Specification
                .where(comId(dto.id()))
                .and(comNome(dto.nome()))
                .and(comCategoria(dto.categoriaId()))
                .and(comQuantidade(dto.quantidade()))
                .and(comValorNotaFiscal(dto.valorNotafiscal()))
                .and(comPrecoVenda(dto.precoVenda()))
                .and(comEstoqueMinimo(dto.estoqueMinimo()))
                .and(comEstoqueMaximo(dto.estoqueMaximo()))
                .and(comAtivo(dto.ativo()))
                .and(comDataCadastro(dto.dataCadastro()))
                .and(comDataAtualizacao(dto.dataAtualizacao()));
    }

    private static <T> Specification<Produto> quandoPresente(
            T valor,
            Specification<Produto> specification) {
        return valor == null
                ? Specification.unrestricted()
                : specification;
    }

}
