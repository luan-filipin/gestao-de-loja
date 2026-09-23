package com.gestaodeloja.estoque.service;

import com.gestaodeloja.estoque.domain.Categoria;
import com.gestaodeloja.estoque.domain.Produto;
import com.gestaodeloja.estoque.dto.request.ProdutoAtualizadoRequestDto;
import com.gestaodeloja.estoque.dto.request.ProdutoFiltrosRequestDto;
import com.gestaodeloja.estoque.dto.request.ProdutoRequestDto;
import com.gestaodeloja.estoque.dto.response.ProdutoInativoResponseDto;
import com.gestaodeloja.estoque.dto.response.ProdutoResponseDto;
import com.gestaodeloja.estoque.exception.CategoriaNaoExistePeloIdException;
import com.gestaodeloja.estoque.exception.ProdutoJaExistePeloNomeException;
import com.gestaodeloja.estoque.exception.ProdutoNaoExisteException;
import com.gestaodeloja.estoque.fixture.CategoriaFixture;
import com.gestaodeloja.estoque.fixture.ProdutoFixture;
import com.gestaodeloja.estoque.mapper.ProdutoMapper;
import com.gestaodeloja.estoque.repository.CategoriaRepository;
import com.gestaodeloja.estoque.repository.ProdutoRepository;
import com.gestaodeloja.estoque.service.impl.ProdutoServiceImpl;
import com.gestaodeloja.estoque.service.validator.CategoriaValidator;
import com.gestaodeloja.estoque.service.validator.ProdutoValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProdutoServiceTest {

    @Mock
    private ProdutoRepository produtoRepository;

    @Mock
    private CategoriaRepository categoriaRepository;

    private ProdutoService produtoService;

    @BeforeEach
    void setUp() {
        ProdutoValidator produtoValidator = new ProdutoValidator(produtoRepository);
        CategoriaValidator categoriaValidator = new CategoriaValidator(categoriaRepository);
        ProdutoMapper produtoMapper = Mappers.getMapper(ProdutoMapper.class);
        produtoService = new ProdutoServiceImpl(produtoValidator, produtoRepository, categoriaValidator, produtoMapper);
    }

    @Test
    void deveCriarProdutoComSucesso() {

        Produto domain = ProdutoFixture.criaProduto(
                1L,
                "Coca-Cola",
                10,
                new BigDecimal("1000"),
                new BigDecimal("8.00"),
                10,
                20,
                "",
                true,
                LocalDateTime.of(2026, 9, 9, 1, 1, 1),
                null);

        ProdutoRequestDto dto = ProdutoFixture.criaProdutoRequestDto(
                "Coca-Cola",
                1L,
                10,
                new BigDecimal("1000"),
                new BigDecimal("8.00"),
                10,
                20,
                "Coca-Cola 2L");

        Categoria categoria = CategoriaFixture.criaCategoria(1L, "Geral");

        when(produtoRepository.save(any(Produto.class))).thenReturn(domain);
        when(categoriaRepository.findById(1L)).thenReturn(Optional.of(categoria));
        ProdutoResponseDto resultado = produtoService.criaProduto(dto);

        assertThat(resultado).isNotNull();
        assertThat(resultado.nome()).isEqualTo("Coca-Cola");
        assertThat(resultado.categoriaId()).isEqualTo(1L);
        assertThat(resultado.ativo()).isTrue();

        verify(produtoRepository).save(any(Produto.class));
        verify(produtoRepository).existsByNome(dto.nome());
        verify(categoriaRepository).findById(1L);
    }

    @Test
    void deveFalharAoCadastrarProdutoComNomeJaExistente() {

        ProdutoRequestDto dto = ProdutoFixture.criaProdutoRequestDto(
                "Coca-Cola",
                1L,
                10,
                new BigDecimal("1000"),
                new BigDecimal("8.00"),
                10,
                20,
                "Coca-Cola 2L");

        when(produtoRepository.existsByNome(dto.nome())).thenReturn(true);

        assertThatThrownBy(() -> produtoService.criaProduto(dto))
                .isInstanceOf(ProdutoJaExistePeloNomeException.class)
                .hasMessage("Ja existe um produto cadastrado com esse nome.");

        verify(produtoRepository).existsByNome(dto.nome());
    }

    @Test
    void deveFalharAoCadastrarProdutoComCategoriaInexistente() {
        ProdutoRequestDto dto = ProdutoFixture.criaProdutoRequestDto(
                "Coca-Cola",
                1L,
                10,
                new BigDecimal("1000"),
                new BigDecimal("8.00"),
                10,
                20,
                "Coca-Cola 2L");

        when(categoriaRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> produtoService.criaProduto(dto))
                .isInstanceOf(CategoriaNaoExistePeloIdException.class)
                .hasMessage("A categoria não existe.");

        verify(categoriaRepository).findById(1L);
    }

    @Test
    void deveBuscarProdutosComSucesso() {

        Pageable pageable = PageRequest.of(0, 10);
        List<Produto> produtos = List.of(
                ProdutoFixture.criaProduto(1L, "Coca-Cola", 10, new BigDecimal("1000"), new BigDecimal("8.00"), 10, 20, "", true, LocalDateTime.now(), null),
                ProdutoFixture.criaProduto(2L, "Fanta", 10, new BigDecimal("1000"), new BigDecimal("8.00"), 10, 20, "", true, LocalDateTime.now(), null)
        );

        Page<Produto> page = ProdutoFixture.criaPageDeProdutos(pageable, produtos);

        when(produtoRepository.findAll(any(Specification.class), eq(pageable))).thenReturn(page);

        ProdutoFiltrosRequestDto filtros = new ProdutoFiltrosRequestDto(
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null
        );

        Page<ProdutoResponseDto> result = produtoService.buscaProdutos(pageable, filtros);

        assertThat(result).isNotNull();
        assertThat(result.getTotalElements()).isEqualTo(2);
        assertThat(result.getContent()).hasSize(2);

    }

    @Test
    void deveBuscarPageDeProdutoPorId() {
        Pageable pageable = PageRequest.of(0, 10);
        List<Produto> produtos = List.of(
                ProdutoFixture.criaProduto(1L, "Coca-Cola", 10, new BigDecimal("1000"), new BigDecimal("8.00"), 10, 20, "", true, LocalDateTime.now(), null)
        );

        Page<Produto> page = ProdutoFixture.criaPageDeProdutos(pageable, produtos);

        when(produtoRepository.findAll(any(Specification.class), eq(pageable))).thenReturn(page);

        ProdutoFiltrosRequestDto filtros = new ProdutoFiltrosRequestDto(
                1L,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null
        );

        Page<ProdutoResponseDto> result = produtoService.buscaProdutos(pageable, filtros);

        assertThat(result).isNotNull();
        assertThat(result.getTotalElements()).isEqualTo(1);
        assertThat(result.getContent()).hasSize(1);
    }

    @Test
    void deveInativarProduto() {

        Produto produto = ProdutoFixture.criaProduto(1L, "Coca-Cola", 10, new BigDecimal("1000"), new BigDecimal("8.00"), 10, 20, "", true, LocalDateTime.now(), null);

        when(produtoRepository.findById(any(Long.class))).thenReturn(Optional.of(produto));

        ProdutoInativoResponseDto result = produtoService.desativaProduto(1L);

        assertThat(result.id()).isEqualTo(1L);
        assertThat(result.ativo()).isFalse();

        verify(produtoRepository).findById(1L);
    }

    @Test
    void deveLancarExceptionAoBuscarProdutoPeloIdInexistente() {
        when(produtoRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        assertThatThrownBy(() -> produtoService.desativaProduto(1L))
                .isInstanceOf(ProdutoNaoExisteException.class)
                .hasMessage("Não existe um produto com esse id.");

        verify(produtoRepository).findById(1L);
    }

    @Test
    void deveAtualizaProdutoComSucesso() {

        Long id = 1L;
        ProdutoAtualizadoRequestDto dtoEntrada = ProdutoFixture.criaProdutoAtualizadoRequestDto(
                "Ouro verde",
                1L,
                10,
                new BigDecimal("1000.0"),
                new BigDecimal("8.0"),
                5,
                50,
                "Ouro verde 2L");

        Produto produto = ProdutoFixture.criaProduto(id, "Coca-Cola", 10, new BigDecimal("1000"), new BigDecimal("8.00"), 10, 20, "", true, LocalDateTime.now(), null);

        when(produtoRepository.findById(id)).thenReturn(Optional.of(produto));
        when(produtoRepository.existsByNome(dtoEntrada.nome())).thenReturn(false);
        when(categoriaRepository.existsById(dtoEntrada.categoriaId())).thenReturn(true);

        ProdutoResponseDto resultado = produtoService.atualizaProduto(id, dtoEntrada);

        assertThat(resultado).isNotNull();
        assertThat(resultado.nome()).isEqualTo("Ouro verde");
        assertThat(resultado.categoriaId()).isEqualTo(1L);
        assertThat(resultado.ativo()).isTrue();

        verify(produtoRepository).findById(id);
        verify(produtoRepository).existsByNome(dtoEntrada.nome());
        verify(categoriaRepository).existsById(dtoEntrada.categoriaId());
    }

    @Test
    void deveLancarExceptionSeProdutoNaoExistirPeloIdParaAtualizar() {
        Long id = 99L;
        ProdutoAtualizadoRequestDto dtoEntrada = ProdutoFixture.criaProdutoAtualizadoRequestDto(
                "Ouro verde",
                1L,
                10,
                new BigDecimal("1000.0"),
                new BigDecimal("8.0"),
                5,
                50,
                "Ouro verde 2L");

        when(produtoRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> produtoService.atualizaProduto(id, dtoEntrada))
                .isInstanceOf(ProdutoNaoExisteException.class)
                .hasMessage("Não existe um produto com esse id.");

        verify(produtoRepository).findById(id);
        verify(produtoRepository, never()).existsByNome(dtoEntrada.nome());
        verify(categoriaRepository, never()).findById(1L);
    }

    @Test
    void deveLancarExceptionSeProdutoJaExistePeloNomeParaAtualizar() {
        Long id = 1L;
        ProdutoAtualizadoRequestDto dtoEntrada = ProdutoFixture.criaProdutoAtualizadoRequestDto(
                "Coca-Cola",
                1L,
                10,
                new BigDecimal("1000.0"),
                new BigDecimal("8.0"),
                5,
                50,
                "Coca-Cola 2L");

        Produto produto = ProdutoFixture.criaProduto(id, "Coca-Cola", 10, new BigDecimal("1000"), new BigDecimal("8.00"), 10, 20, "", true, LocalDateTime.now(), null);

        when(produtoRepository.findById(id)).thenReturn(Optional.of(produto));
        when(produtoRepository.existsByNome(dtoEntrada.nome())).thenReturn(true);

        assertThatThrownBy(() -> produtoService.atualizaProduto(id, dtoEntrada))
                .isInstanceOf(ProdutoJaExistePeloNomeException.class)
                .hasMessage("Ja existe um produto cadastrado com esse nome.");

        verify(produtoRepository).findById(id);
        verify(produtoRepository).existsByNome(dtoEntrada.nome());
        verify(categoriaRepository, never()).findById(1L);
    }

    @Test
    void deveLancarExceptionSeCategoriaNaoExistirParaAtualizar() {

        Long id = 1L;
        ProdutoAtualizadoRequestDto dtoEntrada = ProdutoFixture.criaProdutoAtualizadoRequestDto(
                "Ouro verde",
                1L,
                10,
                new BigDecimal("1000.0"),
                new BigDecimal("8.0"),
                5,
                50,
                "Ouro verde 2L");
        Produto produto = ProdutoFixture.criaProduto(id, "Coca-Cola", 10, new BigDecimal("1000"), new BigDecimal("8.00"), 10, 20, "", true, LocalDateTime.now(), null);

        when(produtoRepository.findById(id)).thenReturn(Optional.of(produto));
        when(produtoRepository.existsByNome(dtoEntrada.nome())).thenReturn(false);
        when(categoriaRepository.existsById(dtoEntrada.categoriaId())).thenReturn(false);

        assertThatThrownBy(() -> produtoService.atualizaProduto(id, dtoEntrada))
                .isInstanceOf(CategoriaNaoExistePeloIdException.class)
                .hasMessage("A categoria não existe.");

        verify(produtoRepository).findById(id);
        verify(produtoRepository).existsByNome(dtoEntrada.nome());
        verify(categoriaRepository).existsById(dtoEntrada.categoriaId());
    }
}
