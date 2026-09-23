package com.gestaodeloja.estoque.config;

import com.gestaodeloja.estoque.dto.response.ErroCampoDto;
import com.gestaodeloja.estoque.dto.response.ErroResponseDto;
import com.gestaodeloja.estoque.exception.CategoriaJaExistePeloNome;
import com.gestaodeloja.estoque.exception.CategoriaNaoExistePeloIdException;
import com.gestaodeloja.estoque.exception.ProdutoJaExistePeloNomeException;
import com.gestaodeloja.estoque.exception.ProdutoNaoExisteException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErroResponseDto> handlerGeneric(RuntimeException e, HttpServletRequest request) {
        ErroResponseDto erroResponseDto = new ErroResponseDto(
                e.getMessage(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                request.getRequestURI());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(erroResponseDto);
    }

    @ExceptionHandler(CategoriaNaoExistePeloIdException.class)
    public ResponseEntity<ErroResponseDto> handlerCategoriaNaoExistePeloId(CategoriaNaoExistePeloIdException e, HttpServletRequest request) {
        ErroResponseDto erroResponseDto = new ErroResponseDto(
                e.getMessage(),
                HttpStatus.NOT_FOUND.value(),
                request.getRequestURI());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(erroResponseDto);
    }

    @ExceptionHandler(CategoriaJaExistePeloNome.class)
    public ResponseEntity<ErroResponseDto> handlerCategoriaJaExistePeloNome(CategoriaJaExistePeloNome e, HttpServletRequest request) {
        ErroResponseDto erroResponseDto = new ErroResponseDto(
                e.getMessage(),
                HttpStatus.CONFLICT.value(),
                request.getRequestURI());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(erroResponseDto);
    }

    @ExceptionHandler(ProdutoJaExistePeloNomeException.class)
    public ResponseEntity<ErroResponseDto> handlerProdutoJaExistePeloNome(ProdutoJaExistePeloNomeException e, HttpServletRequest request) {
        ErroResponseDto erroResponseDto = new ErroResponseDto(
                e.getMessage(),
                HttpStatus.CONFLICT.value(),
                request.getRequestURI());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(erroResponseDto);
    }

    @ExceptionHandler(ProdutoNaoExisteException.class)
    public ResponseEntity<ErroResponseDto> handlerProdutoNaoExiste(ProdutoNaoExisteException e, HttpServletRequest request) {
        ErroResponseDto erroResponseDto = new ErroResponseDto(
                e.getMessage(),
                HttpStatus.NOT_FOUND.value(),
                request.getRequestURI());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(erroResponseDto);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErroResponseDto> handlerMethodArgumentNotValid(MethodArgumentNotValidException e, HttpServletRequest request) {

        List<ErroCampoDto> erros = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(erro -> new ErroCampoDto(erro.getField(), erro.getDefaultMessage()))
                .toList();

        ErroResponseDto resposta = new ErroResponseDto(
                "Campos inválidos",
                HttpStatus.BAD_REQUEST.value(),
                request.getRequestURI(),
                erros);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resposta);
    }
}
