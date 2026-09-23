package com.gestaodeloja.estoque.service.validator;

import com.gestaodeloja.estoque.domain.Produto;
import com.gestaodeloja.estoque.exception.ProdutoJaExistePeloNomeException;
import com.gestaodeloja.estoque.exception.ProdutoNaoExisteException;
import com.gestaodeloja.estoque.repository.ProdutoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProdutoValidatorTest {

    @Mock
    private ProdutoRepository produtoRepository;

    @InjectMocks
    private ProdutoValidator produtoValidator;

    @Test
    void deveRetornarTrueSeProdutoJaExistirPeloNome() {
        String nome = "Coca-cola";
        when(produtoRepository.existsByNome(nome)).thenReturn(true);

        assertThatCode(() -> produtoValidator.validaSeProdutoJaExistePeloNome(nome))
                .isInstanceOf(ProdutoJaExistePeloNomeException.class)
                .hasMessage("Ja existe um produto cadastrado com esse nome.");

        verify(produtoRepository).existsByNome(nome);
    }

    @Test
    void deveRetornarFalseSeProdutoNaoExistirPeloNome() {
        String nome = "Coca-cola";
        when(produtoRepository.existsByNome(nome)).thenReturn(false);

        assertThatCode(() -> produtoValidator.validaSeProdutoJaExistePeloNome(nome))
                .doesNotThrowAnyException();

        verify(produtoRepository).existsByNome(nome);
    }

    @Test
    void deveRetornarProdutoSeExistirPeloId() {
        Long id = 1L;
        when(produtoRepository.findById(id)).thenReturn(java.util.Optional.of(new Produto()));

        Optional<Produto> result = produtoRepository.findById(id);

        assertThat(result).isPresent();
    }

    @Test
    void deveLancarExceptionSeProdutoNaoExistirPeloId() {
        Long id = 99L;
        when(produtoRepository.findById(id)).thenReturn(java.util.Optional.empty());

        assertThatCode(() -> produtoValidator.validaSeProdutoExistePeloId(id))
                .isInstanceOf(ProdutoNaoExisteException.class)
                .hasMessage("O produto não existe.");
    }
}
