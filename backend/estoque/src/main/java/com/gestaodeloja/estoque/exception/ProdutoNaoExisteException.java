package com.gestaodeloja.estoque.exception;

public class ProdutoNaoExisteException extends RuntimeException {
    public ProdutoNaoExisteException() {
        super("Não existe um produto com esse id.");
    }
}
