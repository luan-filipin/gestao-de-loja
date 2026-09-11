package com.gestaodeloja.estoque.controller;

import com.gestaodeloja.estoque.dto.request.CategoriaRequestDto;
import com.gestaodeloja.estoque.dto.response.CategoriaResponseDto;
import com.gestaodeloja.estoque.service.CategoriaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/categoria")
public class CategoriaController {

    private final CategoriaService categoriaService;

    @PostMapping()
    public ResponseEntity<CategoriaResponseDto> criarCategoria(@RequestBody @Valid CategoriaRequestDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(categoriaService.criaCategoria(dto));
    }
}
