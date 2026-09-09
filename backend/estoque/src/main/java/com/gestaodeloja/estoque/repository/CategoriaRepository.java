package com.gestaodeloja.estoque.repository;

import com.gestaodeloja.estoque.domain.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
}
