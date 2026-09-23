package com.gestaodeloja.estoque.service.validator;

import com.gestaodeloja.estoque.domain.Produto;
import com.gestaodeloja.estoque.exception.ProdutoJaExistePeloNomeException;
import com.gestaodeloja.estoque.exception.ProdutoNaoExisteException;
import com.gestaodeloja.estoque.repository.ProdutoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class ProdutoValidator {

    private final ProdutoRepository produtoRepository;

    public void validaSeProdutoJaExistePeloNome(String nome) {
        if (produtoRepository.existsByNome(nome)) {
            throw new ProdutoJaExistePeloNomeException();
        }
    }

    public Produto validaSeProdutoExistePeloId(Long id) {
        return produtoRepository.findById(id).orElseThrow(ProdutoNaoExisteException::new);
    }

}
