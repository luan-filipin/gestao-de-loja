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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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

    @Test
    @DataSet(value = {"datasets/categoria.xml", "datasets/produto.xml"})
    void deveLancarExceptionSeProdutoJaExistir() throws Exception {
        ProdutoRequestDto dto = ProdutoFixture.criaProdutoRequestDto(
                "Coca-cola",
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
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.mensagem").value("Ja existe um produto cadastrado com esse nome."))
                .andExpect(jsonPath("$.path").value("/api/produto"))
                .andExpect(jsonPath("$.status").value(409))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    void deveLancarExceptionSeCategoriaNaoExistir() throws Exception {
        ProdutoRequestDto dto = ProdutoFixture.criaProdutoRequestDto(
                "Fanta",
                99L,
                10,
                new BigDecimal("1000"),
                new BigDecimal("8.00"),
                10,
                20,
                "Fanta 2L");

        mockMvc.perform(post("/api/produto")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.mensagem").value("A categoria não existe."))
                .andExpect(jsonPath("$.path").value("/api/produto"))
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    void deveRetornarTodosErrosQuandoCamposObrigatoriosEstiveremNulos() throws Exception {

        ProdutoRequestDto dto = ProdutoFixture.criaProdutoRequestDto(
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                "Fanta 2L");

        mockMvc.perform(post("/api/produto")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.mensagem").value("Campos inválidos"))
                .andExpect(jsonPath("$.erros.length()").value(7));
    }

    @Test
    @DataSet(value = {"datasets/categoria.xml", "datasets/produto.xml"})
    void deveRetornarPageDeProdutosPeloIdComSucesso() throws Exception {

        mockMvc.perform(get("/api/produto")
                        .param("page", "0")
                        .param("size", "10")
                        .param("id", "2")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements").value(1))
                .andExpect(jsonPath("$.content[0].nome").value("Fanta"))
        ;
    }

    @Test
    @DataSet(value = {"datasets/categoria.xml", "datasets/produto.xml"})
    void deveRetornarTodosOsProdutosComsucesso() throws Exception {
        mockMvc.perform(get("/api/produto")
                        .param("page", "0")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(2));
    }

    @Test
    @DataSet(value = {"datasets/categoria.xml", "datasets/produto.xml"})
    void deveLancarErroQuandoCampoForDoTipoDiferente() throws Exception {
        mockMvc.perform(get("/api/produto")
                        .param("page", "0")
                        .param("size", "10")
                        .param("id", "adc")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.mensagem").value("Campos inválidos"))
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.path").value("/api/produto"));
    }
}
