package com.gestaodeloja.estoque.fixture;

import com.gestaodeloja.estoque.domain.Categoria;
import com.gestaodeloja.estoque.dto.request.CategoriaRequestDto;

public class CategoriaFixture {

    public static Categoria criaCategoria(Long id, String nome) {
        return Categoria.builder()
                .id(id)
                .nome(nome)
                .build();
    }

    public static CategoriaRequestDto criaCategoriaRequestDto(String nome) {
        return new CategoriaRequestDto(nome);
    }
}
