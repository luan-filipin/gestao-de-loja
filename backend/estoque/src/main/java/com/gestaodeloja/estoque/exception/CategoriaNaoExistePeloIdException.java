package com.gestaodeloja.estoque.exception;

public class CategoriaNaoExistePeloIdException extends RuntimeException {
    public CategoriaNaoExistePeloIdException() {
        super("A categoria não existe.");
    }
}
