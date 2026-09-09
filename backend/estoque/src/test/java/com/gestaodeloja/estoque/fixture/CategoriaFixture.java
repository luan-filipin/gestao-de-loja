package com.gestaodeloja.estoque.fixture;

import com.gestaodeloja.estoque.domain.Categoria;

public class CategoriaFixture {

    public static Categoria criaCategoria(Long id, String nome) {
        return Categoria.builder()
                .id(id)
                .nome(nome)
                .build();
    }
}
