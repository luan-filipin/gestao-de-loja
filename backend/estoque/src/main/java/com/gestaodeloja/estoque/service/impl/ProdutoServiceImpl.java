package com.gestaodeloja.estoque.service.impl;

import com.gestaodeloja.estoque.domain.Categoria;
import com.gestaodeloja.estoque.domain.Produto;
import com.gestaodeloja.estoque.dto.request.ProdutoFiltrosRequestDto;
import com.gestaodeloja.estoque.dto.request.ProdutoRequestDto;
import com.gestaodeloja.estoque.dto.response.ProdutoResponseDto;
import com.gestaodeloja.estoque.mapper.ProdutoMapper;
import com.gestaodeloja.estoque.repository.ProdutoRepository;
import com.gestaodeloja.estoque.service.ProdutoService;
import com.gestaodeloja.estoque.service.validator.CategoriaValidator;
import com.gestaodeloja.estoque.service.validator.ProdutoValidator;
import com.gestaodeloja.estoque.specification.ProdutoSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    @Override
    public Page<ProdutoResponseDto> buscaProdutos(Pageable pageable, ProdutoFiltrosRequestDto filtros) {
        Page<Produto> produtos = produtoRepository.findAll(ProdutoSpecification.comFiltros(filtros), pageable);
        return produtos.map(produtoMapper::toResponseDto);
    }
}
