package com.gestaodeloja.estoque.service;

import com.gestaodeloja.estoque.domain.Categoria;
import com.gestaodeloja.estoque.dto.request.CategoriaRequestDto;
import com.gestaodeloja.estoque.dto.response.CategoriaResponseDto;
import com.gestaodeloja.estoque.exception.CategoriaJaExistePeloNome;
import com.gestaodeloja.estoque.fixture.CategoriaFixture;
import com.gestaodeloja.estoque.mapper.CategoriaMapper;
import com.gestaodeloja.estoque.repository.CategoriaRepository;
import com.gestaodeloja.estoque.service.impl.CategoriaServiceImpl;
import com.gestaodeloja.estoque.service.validator.CategoriaValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CategoriaServiceTest {

    @Mock
    private CategoriaRepository categoriaRepository;

    private CategoriaService categoriaService;

    @BeforeEach
    void setUp() {
        CategoriaValidator categoriaValidator = new CategoriaValidator(categoriaRepository);
        CategoriaMapper categoriaMapper = Mappers.getMapper(CategoriaMapper.class);
        categoriaService = new CategoriaServiceImpl(categoriaRepository, categoriaValidator, categoriaMapper);
    }

    @Test
    void deveCriarCategoriaComSucesso() {
        CategoriaRequestDto dto = CategoriaFixture.criaCategoriaRequestDto("Cerveja");
        Categoria domain = CategoriaFixture.criaCategoria(1L, "Cerveja");

        when(categoriaRepository.save(any(Categoria.class))).thenReturn(domain);

        CategoriaResponseDto resultado = categoriaService.criaCategoria(dto);

        assertThat(resultado).isNotNull();
        assertThat(resultado.nome()).isEqualTo("Cerveja");

        verify(categoriaRepository).existsByNome(dto.nome());
        verify(categoriaRepository).save(any(Categoria.class));
    }

    @Test
    void deveFalharAoCadastrarCategoriaComNomeJaExistente() {
        CategoriaRequestDto dto = CategoriaFixture.criaCategoriaRequestDto("Cerveja");

        when(categoriaRepository.existsByNome(dto.nome())).thenReturn(true);

        assertThatThrownBy(() -> categoriaService.criaCategoria(dto))
                .isInstanceOf(CategoriaJaExistePeloNome.class)
                .hasMessage("Ja existe uma categoria cadastrada com esse nome.");

        verify(categoriaRepository).existsByNome(dto.nome());
    }
}
