package com.gestaodeloja.estoque.controller;

import com.gestaodeloja.estoque.config.PostgresTestContainer;
import com.gestaodeloja.estoque.dto.request.ProdutoRequestDto;
import com.gestaodeloja.estoque.fixture.ProdutoFixture;
import com.github.database.rider.core.api.configuration.DBUnit;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.junit5.api.DBRider;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Import(PostgresTestContainer.class)
@DBRider
@DBUnit(
        cacheConnection = false,
        schema = "public",
        disableSequenceFiltering = true,
        alwaysCleanBefore = true,
        alwaysCleanAfter = true
)
class ProdutoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DataSet(value = {"datasets/categoria.xml"})
    void deveCriarProdutoComSucesso() throws Exception {

        ProdutoRequestDto dto = ProdutoFixture.criaProdutoRequestDto(
                "Fanta",
                1L,
                10,
                new BigDecimal("1000"),
                new BigDecimal("8.00"),
                10,
                20,
                "Fanta 2L");

        mockMvc.perform(post("/api/produto")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nome").value("Fanta"))
                .andExpect(jsonPath("$.categoriaId").value(1L));
    }
}
