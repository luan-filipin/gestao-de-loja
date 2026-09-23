package com.gestaodeloja.estoque.controller;

import com.gestaodeloja.estoque.dto.request.ProdutoFiltrosRequestDto;
import com.gestaodeloja.estoque.dto.request.ProdutoRequestDto;
import com.gestaodeloja.estoque.dto.response.ProdutoInativoResponseDto;
import com.gestaodeloja.estoque.dto.response.ProdutoResponseDto;
import com.gestaodeloja.estoque.service.ProdutoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RequiredArgsConstructor
@RestController
@RequestMapping("/api/produto")
public class ProdutoController {

    private final ProdutoService produtoService;

    @PostMapping()
    public ResponseEntity<ProdutoResponseDto> criarProduto(@RequestBody @Valid ProdutoRequestDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(produtoService.criaProduto(dto));
    }

    @GetMapping()
    public ResponseEntity<Page<ProdutoResponseDto>> buscaProdutos(Pageable pageable, @ModelAttribute ProdutoFiltrosRequestDto filtros) {
        return ResponseEntity.status(HttpStatus.OK).body(produtoService.buscaProdutos(pageable, filtros));
    }

    @PatchMapping("/inativar/{id}")
    public ResponseEntity<ProdutoInativoResponseDto> desativaProduto(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(produtoService.desativaProduto(id));
    }
}
