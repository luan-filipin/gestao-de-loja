package com.gestaodeloja.authenticator.config;

import com.gestaodeloja.authenticator.dto.response.ErroCampoDto;
import com.gestaodeloja.authenticator.dto.response.ErroResponseDto;
import com.gestaodeloja.authenticator.exception.UserJaExisteException;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Mock
    private HttpServletRequest request;

    @Mock
    private MethodArgumentNotValidException exception;

    @Mock
    private BindingResult bindingResult;

    @Test
    void deveRetornarErro500ComMensagemDaExcecao() {

        when(request.getRequestURI()).thenReturn("/api/user");

        ResponseEntity<ErroResponseDto> result =
                handler.handlerGeneric(new RuntimeException("Erro inesperado"), request);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(result.getBody()).isNotNull();
        assertThat(result.getBody().getMensagem()).isEqualTo("Erro inesperado");
        assertThat(result.getBody().getStatus()).isEqualTo(500);
        assertThat(result.getBody().getPath()).isEqualTo("/api/user");
    }

    @Test
    void deveRetornarErro500QuandoExcecaoNaoTiverMensagem() {

        when(request.getRequestURI()).thenReturn("/api/auth/login");

        ResponseEntity<ErroResponseDto> result =
                handler.handlerGeneric(new RuntimeException(), request);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(result.getBody()).isNotNull();
        assertThat(result.getBody().getMensagem()).isNull();
        assertThat(result.getBody().getPath()).isEqualTo("/api/auth/login");
    }

    @Test
    void deveRetornarErro409ComMensagemDaExcecao() {

        when(request.getRequestURI()).thenReturn("/api/user");

        ResponseEntity<ErroResponseDto> result =
                handler.handlerCategoriaJaExistePeloNome(new UserJaExisteException(), request);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        assertThat(result.getBody()).isNotNull();
        assertThat(result.getBody().getMensagem()).isEqualTo("O usuario informado ja existe.");
        assertThat(result.getBody().getStatus()).isEqualTo(409);
        assertThat(result.getBody().getPath()).isEqualTo("/api/user");
    }

    @Test
    void deveRetornarErro400ComMensagemDoCampoInvalido() {

        when(request.getRequestURI()).thenReturn("/api/user");
        when(exception.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getFieldErrors()).thenReturn(List.of(
                new FieldError("createUserRequestDto", "userName", "O campo userName é obrigatorio."),
                new FieldError("createUserRequestDto", "password", "O campo password é obrigatorio.")
        ));

        ResponseEntity<ErroResponseDto> result =
                handler.handlerMethodArgumentNotValid(exception, request);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(result.getBody()).isNotNull();
        assertThat(result.getBody().getMensagem()).isEqualTo("Campos inválidos");
        assertThat(result.getBody().getStatus()).isEqualTo(400);
        assertThat(result.getBody().getPath()).isEqualTo("/api/user");
        assertThat(result.getBody().getErros()).containsExactly(
                new ErroCampoDto("userName", "O campo userName é obrigatorio."),
                new ErroCampoDto("password", "O campo password é obrigatorio.")
        );
    }

    @Test
    void deveRetornarErro400ComListaVaziaQuandoNaoHouverErrosDeCampo() {

        when(request.getRequestURI()).thenReturn("/api/user");
        when(exception.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getFieldErrors()).thenReturn(List.of());

        ResponseEntity<ErroResponseDto> result =
                handler.handlerMethodArgumentNotValid(exception, request);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(result.getBody()).isNotNull();
        assertThat(result.getBody().getErros()).isEmpty();
    }
}
