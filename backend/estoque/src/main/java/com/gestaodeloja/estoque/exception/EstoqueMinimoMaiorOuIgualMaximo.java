package com.gestaodeloja.estoque.exception;

public class EstoqueMinimoMaiorQueOMaximoException extends RuntimeException {
    public EstoqueMinimoMaiorQueOMaximoException() {
        super("O estoque minimo deve ser menor que o estoque maximo.");
    }
}
