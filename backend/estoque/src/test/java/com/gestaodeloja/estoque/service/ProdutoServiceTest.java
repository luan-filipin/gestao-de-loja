package com.gestaodeloja.estoque.service;

import com.gestaodeloja.estoque.domain.Categoria;
import com.gestaodeloja.estoque.domain.Produto;
import com.gestaodeloja.estoque.dto.request.ProdutoRequestDto;
import com.gestaodeloja.estoque.dto.response.ProdutoResponseDto;
import com.gestaodeloja.estoque.exception.CategoriaNaoExistePeloIdException;
import com.gestaodeloja.estoque.exception.ProdutoJaExistePeloNomeException;
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

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
}
