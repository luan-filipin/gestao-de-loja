package com.gestaodeloja.estoque.mapper;

import com.gestaodeloja.estoque.domain.Categoria;
import com.gestaodeloja.estoque.domain.Produto;
import com.gestaodeloja.estoque.dto.request.ProdutoAtualizadoRequestDto;
import com.gestaodeloja.estoque.dto.request.ProdutoRequestDto;
import com.gestaodeloja.estoque.dto.response.ProdutoInativoResponseDto;
import com.gestaodeloja.estoque.dto.response.ProdutoResponseDto;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface ProdutoMapper {

    @Mapping(source = "categoria.id", target = "categoriaId")
    ProdutoResponseDto toResponseDto(Produto produto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "nome", source = "dto.nome")
    @Mapping(target = "dataCadastro", ignore = true)
    @Mapping(target = "dataAtualizacao", ignore = true)
    @Mapping(target = "ativo", ignore = true)
    @Mapping(source = "categoria", target = "categoria")
    Produto toEntity(ProdutoRequestDto dto, Categoria categoria);


    ProdutoInativoResponseDto toInativoResponseDto(Produto produto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "ativo", ignore = true)
    @Mapping(target = "dataCadastro", ignore = true)
    @Mapping(target = "dataAtualizacao", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntity(ProdutoAtualizadoRequestDto dto, @MappingTarget Produto produto);

}
