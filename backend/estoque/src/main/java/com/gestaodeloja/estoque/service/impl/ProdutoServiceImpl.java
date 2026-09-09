package com.gestaodeloja.estoque.service.impl;

import com.gestaodeloja.estoque.domain.Categoria;
import com.gestaodeloja.estoque.domain.Produto;
import com.gestaodeloja.estoque.dto.request.ProdutoRequestDto;
import com.gestaodeloja.estoque.dto.response.ProdutoResponseDto;
import com.gestaodeloja.estoque.mapper.ProdutoMapper;
import com.gestaodeloja.estoque.repository.ProdutoRepository;
import com.gestaodeloja.estoque.service.ProdutoService;
import com.gestaodeloja.estoque.service.validator.CategoriaValidator;
import com.gestaodeloja.estoque.service.validator.ProdutoValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ProdutoServiceImpl implements ProdutoService {

    private final ProdutoValidator produtoValidator;
    private final ProdutoRepository produtoRepository;
    private final CategoriaValidator categoriaValidator;
    private final ProdutoMapper produtoMapper;

    @Override
    public ProdutoResponseDto criaProduto(ProdutoRequestDto dto) {
        produtoValidator.validaSeProdutoJaExiste(dto.nome());
        Categoria categoria = categoriaValidator.validaCategoriaExistente(dto.idCategoria());
        Produto produtoSalvo = produtoRepository.save(produtoMapper.toEntity(dto, categoria));
        return produtoMapper.toResponseDto(produtoSalvo);
    }
}
