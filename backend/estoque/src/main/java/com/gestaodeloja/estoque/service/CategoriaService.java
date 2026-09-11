package com.gestaodeloja.estoque.service;

import com.gestaodeloja.estoque.dto.request.CategoriaRequestDto;
import com.gestaodeloja.estoque.dto.response.CategoriaResponseDto;

public interface CategoriaService {
    public CategoriaResponseDto criaCategoria(CategoriaRequestDto dto);
}
