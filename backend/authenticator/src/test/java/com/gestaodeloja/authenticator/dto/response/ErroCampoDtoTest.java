package com.gestaodeloja.authenticator.dto.response;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class ErroCampoDtoTest {

    @Test
    void deveCriarErroCampoDto() {

        ErroCampoDto erroCampoDto = new ErroCampoDto("userName", "O campo userName é obrigatorio.");

        assertThat(erroCampoDto.campo()).isEqualTo("userName");
        assertThat(erroCampoDto.mensagem()).isEqualTo("O campo userName é obrigatorio.");
    }
}
