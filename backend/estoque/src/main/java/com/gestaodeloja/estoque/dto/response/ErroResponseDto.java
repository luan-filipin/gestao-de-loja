package com.gestaodeloja.estoque.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.List;

@Getter
@Setter
public class ErroResponseDto {

    private String mensagem;
    private int status;
    private String path;
    private Instant timestamp;
    private List<ErroCampoDto> erros;

    public ErroResponseDto(String mensagem, int status, String path) {
        this.mensagem = mensagem;
        this.status = status;
        this.path = path;
        this.timestamp = Instant.now();
    }

    public ErroResponseDto(String mensagem, int status, String path, List<ErroCampoDto> erros) {
        this(mensagem, status, path);
        this.erros = erros;
    }

}
