package com.gestaodeloja.estoque.service;

import com.gestaodeloja.estoque.dto.request.ProdutoFiltrosRequestDto;
import com.gestaodeloja.estoque.dto.request.ProdutoRequestDto;
import com.gestaodeloja.estoque.dto.response.ProdutoResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProdutoService {

    public ProdutoResponseDto criaProduto(ProdutoRequestDto dto);

    public Page<ProdutoResponseDto> buscaProdutos(Pageable pageable, ProdutoFiltrosRequestDto filtros);
}
