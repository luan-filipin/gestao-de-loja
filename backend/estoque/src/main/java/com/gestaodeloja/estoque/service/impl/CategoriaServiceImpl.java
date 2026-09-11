package com.gestaodeloja.estoque.service.impl;

import com.gestaodeloja.estoque.domain.Categoria;
import com.gestaodeloja.estoque.dto.request.CategoriaRequestDto;
import com.gestaodeloja.estoque.dto.response.CategoriaResponseDto;
import com.gestaodeloja.estoque.mapper.CategoriaMapper;
import com.gestaodeloja.estoque.repository.CategoriaRepository;
import com.gestaodeloja.estoque.service.CategoriaService;
import com.gestaodeloja.estoque.service.validator.CategoriaValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CategoriaServiceImpl implements CategoriaService {

    private final CategoriaRepository categoriaRepository;
    private final CategoriaValidator categoriaValidator;
    private final CategoriaMapper categoriaMapper;

    @Override
    public CategoriaResponseDto criaCategoria(CategoriaRequestDto dto) {
        categoriaValidator.validaSeCategoriaJaExistePeloNome(dto.nome());
        Categoria categoriaSalva = categoriaRepository.save(categoriaMapper.toEntity(dto));
        return categoriaMapper.toDto(categoriaSalva);
    }
}
