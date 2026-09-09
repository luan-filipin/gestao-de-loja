package com.gestaodeloja.estoque.repository;

import com.gestaodeloja.estoque.domain.Produto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {

    boolean existsByNome(String nome);
}
