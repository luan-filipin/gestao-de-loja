package com.gestaodeloja.estoque.service.validator;

import com.gestaodeloja.estoque.exception.ProdutoJaExistePeloNomeException;
import com.gestaodeloja.estoque.repository.ProdutoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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

        assertThatCode(() -> produtoValidator.validaSeProdutoJaExiste(nome))
                .isInstanceOf(ProdutoJaExistePeloNomeException.class)
                .hasMessage("Ja existe um produto cadastrado com esse nome.");

        verify(produtoRepository).existsByNome(nome);
    }

    @Test
    void deveRetornarFalseSeProdutoNaoExistirPeloNome() {
        String nome = "Coca-cola";
        when(produtoRepository.existsByNome(nome)).thenReturn(false);

        assertThatCode(() -> produtoValidator.validaSeProdutoJaExiste(nome))
                .doesNotThrowAnyException();

        verify(produtoRepository).existsByNome(nome);
    }
}
