package com.gestaodeloja.estoque.exception;

public class CategoriaJaExistePeloNome extends RuntimeException {
    public CategoriaJaExistePeloNome() {
        super("Ja existe uma categoria cadastrada com esse nome.");
    }
}
