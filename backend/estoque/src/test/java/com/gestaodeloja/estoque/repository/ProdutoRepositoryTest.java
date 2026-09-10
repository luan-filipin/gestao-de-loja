package com.gestaodeloja.estoque.repository;

import com.gestaodeloja.estoque.config.PostgresTestContainer;
import com.github.database.rider.core.api.configuration.DBUnit;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.junit5.api.DBRider;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(PostgresTestContainer.class)
@DBRider
@DBUnit(
        cacheConnection = false,
        schema = "public",
        disableSequenceFiltering = true
)
@DataSet(
        value = {"datasets/categoria.xml",
                "datasets/produto.xml"},
        cleanBefore = true,
        cleanAfter = true
)
class ProdutoRepositoryTest {

    @Autowired
    private ProdutoRepository produtoRepository;

    @Test
    void deveVerificarSeExisteProdutoComNome() {
        boolean existe = produtoRepository.existsByNome("Coca-cola");
        assertThat(existe).isTrue();
    }
}
