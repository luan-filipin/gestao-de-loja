package com.gestaodeloja.estoque.service.validator;

import com.gestaodeloja.estoque.domain.Categoria;
import com.gestaodeloja.estoque.exception.CategoriaNaoExistePeloIdException;
import com.gestaodeloja.estoque.repository.CategoriaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class CategoriaValidator {

    private final CategoriaRepository categoriaRepository;

    public Categoria validaCategoriaExistente(Long id) {
        return categoriaRepository.findById(id).orElseThrow(
                CategoriaNaoExistePeloIdException::new);
    }
}
