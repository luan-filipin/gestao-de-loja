package com.gestaodeloja.estoque.exception;

public class ProdutoNaoExisteException extends RuntimeException {
    public ProdutoNaoExisteException() {
        super("O produto não existe.");
    }
}
