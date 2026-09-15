package com.gestaodeloja.authenticator.config;

import com.gestaodeloja.authenticator.dto.response.ErroCampoDto;
import com.gestaodeloja.authenticator.dto.response.ErroResponseDto;
import com.gestaodeloja.authenticator.exception.UserJaExisteException;
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

    @ExceptionHandler(UserJaExisteException.class)
    public ResponseEntity<ErroResponseDto> handlerCategoriaJaExistePeloNome(UserJaExisteException e, HttpServletRequest request) {
        ErroResponseDto erroResponseDto = new ErroResponseDto(
                e.getMessage(),
                HttpStatus.CONFLICT.value(),
                request.getRequestURI());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(erroResponseDto);
    }


}
