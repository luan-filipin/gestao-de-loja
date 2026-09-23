package com.gestaodeloja.estoque.config;

import com.gestaodeloja.estoque.dto.response.ErroCampoDto;
import com.gestaodeloja.estoque.dto.response.ErroResponseDto;
import com.gestaodeloja.estoque.exception.CategoriaJaExistePeloNome;
import com.gestaodeloja.estoque.exception.CategoriaNaoExistePeloIdException;
import com.gestaodeloja.estoque.exception.ProdutoJaExistePeloNomeException;
import com.gestaodeloja.estoque.exception.ProdutoNaoExisteException;
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

        when(request.getRequestURI()).thenReturn("/api/produto");

        ResponseEntity<ErroResponseDto> result =
                handler.handlerGeneric(new RuntimeException("Erro inesperado"), request);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(result.getBody()).isNotNull();
        assertThat(result.getBody().getMensagem()).isEqualTo("Erro inesperado");
        assertThat(result.getBody().getStatus()).isEqualTo(500);
        assertThat(result.getBody().getPath()).isEqualTo("/api/produto");
    }

    @Test
    void deveRetornarErro404QuandoCategoriaNaoExistePeloId() {

        when(request.getRequestURI()).thenReturn("/api/produto");

        ResponseEntity<ErroResponseDto> result =
                handler.handlerCategoriaNaoExistePeloId(new CategoriaNaoExistePeloIdException(), request);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(result.getBody()).isNotNull();
        assertThat(result.getBody().getStatus()).isEqualTo(404);
        assertThat(result.getBody().getMensagem()).isEqualTo("A categoria não existe.");
        assertThat(result.getBody().getPath()).isEqualTo("/api/produto");
    }

    @Test
    void deveRetornarErro409QuandoCategoriaJaExistePeloNome() {

        when(request.getRequestURI()).thenReturn("/api/produto");

        ResponseEntity<ErroResponseDto> result =
                handler.handlerCategoriaJaExistePeloNome(new CategoriaJaExistePeloNome(), request);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        assertThat(result.getBody()).isNotNull();
        assertThat(result.getBody().getStatus()).isEqualTo(409);
        assertThat(result.getBody().getMensagem()).isEqualTo("Ja existe uma categoria cadastrada com esse nome.");
        assertThat(result.getBody().getPath()).isEqualTo("/api/produto");
    }

    @Test
    void deveRetornarErro409QuandoProdutojaExistePeloNome() {

        when(request.getRequestURI()).thenReturn("/api/produto");

        ResponseEntity<ErroResponseDto> result =
                handler.handlerProdutoJaExistePeloNome(new ProdutoJaExistePeloNomeException(), request);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        assertThat(result.getBody()).isNotNull();
        assertThat(result.getBody().getStatus()).isEqualTo(409);
        assertThat(result.getBody().getMensagem()).isEqualTo("Ja existe um produto cadastrado com esse nome.");
        assertThat(result.getBody().getPath()).isEqualTo("/api/produto");
    }

    @Test
    void deveRetornarErro404QuandoProdutoNaoExiste() {

        when(request.getRequestURI()).thenReturn("/api/produto");

        ResponseEntity<ErroResponseDto> result =
                handler.handlerProdutoNaoExiste(new ProdutoNaoExisteException(), request);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(result.getBody()).isNotNull();
        assertThat(result.getBody().getMensagem()).isEqualTo("Não existe um produto com esse id.");
        assertThat(result.getBody().getStatus()).isEqualTo(404);
        assertThat(result.getBody().getPath()).isEqualTo("/api/produto");
    }

    @Test
    void deveRetorarErro400ComMensagemDoCampoInvalido() {

        when(request.getRequestURI()).thenReturn("/api/produto");
        when(exception.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getFieldErrors()).thenReturn(List.of(
                new FieldError("produtoRequestDto", "nome", "O campo nome é obrigatorio."),
                new FieldError("produtoRequestDto", "idCategoria", "O campo categoria é obrigatorio.")
        ));

        ResponseEntity<ErroResponseDto> result =
                handler.handlerMethodArgumentNotValid(exception, request);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(result.getBody()).isNotNull();
        assertThat(result.getBody().getMensagem()).isEqualTo("Campos inválidos");
        assertThat(result.getBody().getStatus()).isEqualTo(400);
        assertThat(result.getBody().getPath()).isEqualTo("/api/produto");
        assertThat(result.getBody().getErros()).containsExactly(
                new ErroCampoDto("nome", "O campo nome é obrigatorio."),
                new ErroCampoDto("idCategoria", "O campo categoria é obrigatorio.")
        );

    }
}
