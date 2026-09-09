package com.gestaodeloja.estoque.repository;

import com.gestaodeloja.estoque.domain.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
    Optional<Categoria> findById(Long id);
}
