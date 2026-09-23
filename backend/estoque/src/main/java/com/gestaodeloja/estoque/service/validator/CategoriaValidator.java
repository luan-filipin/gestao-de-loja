package com.gestaodeloja.estoque.service.validator;

import com.gestaodeloja.estoque.domain.Categoria;
import com.gestaodeloja.estoque.exception.CategoriaJaExistePeloNome;
import com.gestaodeloja.estoque.exception.CategoriaNaoExistePeloIdException;
import com.gestaodeloja.estoque.repository.CategoriaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class CategoriaValidator {

    private final CategoriaRepository categoriaRepository;

    public Categoria validaCategoriaExistentePeloId(Long id) {
        return categoriaRepository.findById(id).orElseThrow(
                CategoriaNaoExistePeloIdException::new);
    }

    public void validaSeCateogriaExisteSemRetorno(Long id) {
        if (!categoriaRepository.existsById(id)) {
            throw new CategoriaNaoExistePeloIdException();
        }
    }

    public void validaSeCategoriaJaExistePeloNome(String nome) {
        if (categoriaRepository.existsByNome(nome)) {
            throw new CategoriaJaExistePeloNome();
        }
    }
}
