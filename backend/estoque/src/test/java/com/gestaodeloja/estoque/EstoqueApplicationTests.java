package com.gestaodeloja.estoque;

import com.gestaodeloja.estoque.config.PostgresTestContainer;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@SpringBootTest
@Import(PostgresTestContainer.class)
class EstoqueApplicationTests {

    @Test
    void contextLoads() {
    }

}
