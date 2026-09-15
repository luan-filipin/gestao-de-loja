package com.gestaodeloja.authenticator.controller;

import com.gestaodeloja.authenticator.config.PostgresTestContainer;
import com.gestaodeloja.authenticator.domain.enums.UserRole;
import com.gestaodeloja.authenticator.dto.request.CreateUserRequestDto;
import com.gestaodeloja.authenticator.fixture.UserFixture;
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
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void deveCriarUsuarioComSucesso() throws Exception {

        CreateUserRequestDto dto = UserFixture.criaCreateUserRequestDto(
                "maria.aparecida",
                "123456",
                UserRole.USER);

        mockMvc.perform(post("/api/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.userName").value("maria.aparecida"))
                .andExpect(jsonPath("$.role").value("USER"));
    }

    @Test
    @DataSet("datasets/users.xml")
    void deveLancarExceptionSeUsuarioJaExistir() throws Exception {
        CreateUserRequestDto dto = UserFixture.criaCreateUserRequestDto(
                "joao.pedro",
                "123456",
                UserRole.USER);

        mockMvc.perform(post("/api/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.mensagem").value("O usuario informado ja existe."));
    }
}
