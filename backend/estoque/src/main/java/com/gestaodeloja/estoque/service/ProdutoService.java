package com.gestaodeloja.estoque.service;

import com.gestaodeloja.estoque.dto.request.ProdutoRequestDto;
import com.gestaodeloja.estoque.dto.response.ProdutoResponseDto;

public interface ProdutoService {
    public ProdutoResponseDto criaProduto(ProdutoRequestDto dto);
}
