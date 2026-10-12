package com.gestaodeloja.estoque.exception;

public class EstoqueMinimoMaiorOuIgualMaximo extends RuntimeException {
    public EstoqueMinimoMaiorOuIgualMaximo() {
        super("O estoque minimo deve ser menor que o estoque maximo.");
    }
}
