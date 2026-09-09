package com.gestaodeloja.estoque.exception;

public class ProdutoJaExistePeloNomeException extends RuntimeException {
    public ProdutoJaExistePeloNomeException() {
        super("Ja existe um produto cadastrado com esse nome.");
    }
}
