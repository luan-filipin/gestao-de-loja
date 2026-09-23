package com.gestaodeloja.estoque.service.validator;

import com.gestaodeloja.estoque.domain.Categoria;
import com.gestaodeloja.estoque.exception.CategoriaJaExistePeloNome;
import com.gestaodeloja.estoque.exception.CategoriaNaoExistePeloIdException;
import com.gestaodeloja.estoque.fixture.CategoriaFixture;
import com.gestaodeloja.estoque.repository.CategoriaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CategoriaValidatorTest {

    @Mock
    private CategoriaRepository categoriaRepository;

    @InjectMocks
    private CategoriaValidator categoriaValidator;

    @Test
    void deveRetornarCategoriaPeloId() {
        Long id = 1L;
        Categoria categoria = CategoriaFixture.criaCategoria(id, "Cerveja");

        when(categoriaRepository.findById(id)).thenReturn(Optional.of(categoria));

        Categoria result = categoriaValidator.validaCategoriaExistentePeloId(id);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);

        verify(categoriaRepository).findById(id);
    }

    @Test
    void deveLancarExceptionAoBuscarCategoriaPeloIdInexistente() {
        Long id = 99L;
        when(categoriaRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> categoriaValidator.validaCategoriaExistentePeloId(id))
                .isInstanceOf(CategoriaNaoExistePeloIdException.class)
                .hasMessage("A categoria não existe.");

        verify(categoriaRepository).findById(id);
    }

    @Test
    void deveRetornarTrueSeCategoriaJaExistirPeloNome() {
        String nome = "Cerveja";
        when(categoriaRepository.existsByNome(nome)).thenReturn(true);

        assertThatCode(() -> categoriaValidator.validaSeCategoriaJaExistePeloNome(nome))
                .isInstanceOf(CategoriaJaExistePeloNome.class)
                .hasMessage("Ja existe uma categoria cadastrada com esse nome.");

        verify(categoriaRepository).existsByNome(nome);
    }

    @Test
    void deveRetornarFalseSeCategoriaNaoExistirPeloNome() {
        String nome = "Cerveja";
        when(categoriaRepository.existsByNome(nome)).thenReturn(false);

        assertThatCode(() -> categoriaValidator.validaSeCategoriaJaExistePeloNome(nome))
                .doesNotThrowAnyException();

        verify(categoriaRepository).existsByNome(nome);
    }

    @Test
    void deveRetornarTrueSeCategoriaJaExistirSemRetorno() {
        Long id = 1L;
        when(categoriaRepository.existsById(id)).thenReturn(false);

        assertThatCode(() -> categoriaValidator.validaSeCateogriaExisteSemRetorno(id))
                .isInstanceOf(CategoriaNaoExistePeloIdException.class)
                .hasMessage("A categoria não existe.");

        verify(categoriaRepository).existsById(id);
    }

    @Test
    void deveRetornarFalseSeCategoriaNaoExistirSemRetorno() {
        Long id = 1L;
        when(categoriaRepository.existsById(id)).thenReturn(true);

        assertThatCode(() -> categoriaValidator.validaSeCateogriaExisteSemRetorno(id))
                .doesNotThrowAnyException();

        verify(categoriaRepository).existsById(id);
    }
}
