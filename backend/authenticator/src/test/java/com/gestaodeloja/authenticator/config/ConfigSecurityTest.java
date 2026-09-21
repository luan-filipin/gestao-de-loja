package com.gestaodeloja.authenticator.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ConfigSecurity.class)
@Import({ConfigSecurity.class, ConfigSecurityTest.TestController.class})
class ConfigSecurityTest {

    private final ConfigSecurity configSecurity = new ConfigSecurity();

    @Autowired
    private MockMvc mockMvc;

    @Test
    void devePermitirAcessoSemAutenticacaoAoCadastroDeUsuario() throws Exception {
        mockMvc.perform(post("/api/user"))
                .andExpect(status().isOk());
    }

    @Test
    void devePermitirAcessoSemAutenticacaoAoLogin() throws Exception {
        mockMvc.perform(post("/api/auth/login"))
                .andExpect(status().isOk());
    }

    @Test
    void deveBloquearRotaProtegidaSemAutenticacao() throws Exception {
        mockMvc.perform(get("/api/qualquer"))
                .andExpect(status().isForbidden());
    }

    @RestController
    static class TestController {

        @PostMapping("/api/user")
        void user() {
        }

        @PostMapping("/api/auth/login")
        void login() {
        }

        @GetMapping("/api/qualquer")
        void qualquer() {
        }
    }

    @Test
    void deveCriarHahsDaSenha() {

        String senha = "123456";

        PasswordEncoder result = configSecurity.passwordEncoder();
        String encoded = result.encode(senha);

        assertThat(encoded).isNotEqualTo("123456");
        assertThat(result.matches(senha, encoded)).isTrue();
        assertThat(result.matches("1234567", encoded)).isFalse();
    }

    @Test
    void deveLancarExceptionQuandoSenhaForNull() {
        PasswordEncoder result = configSecurity.passwordEncoder();
        assertThat(result.encode(null)).isNull();
    }

    @Test
    void deveRetornarFalseQuandoHashForInvalida() {
        PasswordEncoder result = configSecurity.passwordEncoder();
        assertThat(result.matches("senha", "invalido")).isFalse();
    }

    @Test
    void deveRetornarAuthenticationManagerDaConfiguracao() throws Exception {

        AuthenticationConfiguration config = mock(AuthenticationConfiguration.class);
        AuthenticationManager authenticationManager = mock(AuthenticationManager.class);

        when(config.getAuthenticationManager()).thenReturn(authenticationManager);

        AuthenticationManager result = configSecurity.authenticationManager(config);

        assertThat(result).isSameAs(authenticationManager);
    }

    @Test
    void devePropagarExceptionQuandoConfiguracaoFalhar() throws Exception {

        AuthenticationConfiguration config = mock(AuthenticationConfiguration.class);

        when(config.getAuthenticationManager()).thenThrow(new IllegalStateException("erro"));

        assertThatThrownBy(() -> configSecurity.authenticationManager(config))
                .isInstanceOf(IllegalStateException.class);
    }

}
