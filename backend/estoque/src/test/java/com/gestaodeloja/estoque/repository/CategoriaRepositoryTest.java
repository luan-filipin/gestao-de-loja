package com.gestaodeloja.estoque.repository;

import com.gestaodeloja.estoque.config.PostgresTestContainer;
import com.gestaodeloja.estoque.domain.Categoria;
import com.github.database.rider.core.api.configuration.DBUnit;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.junit5.api.DBRider;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(PostgresTestContainer.class)
@DBRider
@DBUnit(
        cacheConnection = false,
        schema = "public",
        disableSequenceFiltering = true,
        alwaysCleanBefore = true,
        alwaysCleanAfter = true)
@DataSet(value = "datasets/categoria.xml")
class CategoriaRepositoryTest {

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Test
    void deveRetornarCategoriaComId() {
        Optional<Categoria> categoria = categoriaRepository.findById(1L);
        assertThat(categoria).isPresent();
        assertThat(categoria.get().getId()).isEqualTo(1L);
    }

    @Test
    void deveRetornarFalsoAoBuscarCategoriaComIdInexistente() {
        Optional<Categoria> categoria = categoriaRepository.findById(99L);
        assertThat(categoria).isEmpty();
    }

    @Test
    void deveVerificarSeExisteCategoriaComNome() {
        boolean existe = categoriaRepository.existsByNome("Bebidas");
        assertThat(existe).isTrue();
    }

    @Test
    void deveRetornarFalsoAoVerificarSeExisteCategoriaComNomeInexistente() {
        boolean existe = categoriaRepository.existsByNome("Cerveja");
        assertThat(existe).isFalse();
    }
}
