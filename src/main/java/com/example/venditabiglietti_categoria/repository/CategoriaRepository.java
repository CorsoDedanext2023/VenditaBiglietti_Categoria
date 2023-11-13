package com.example.venditabiglietti_categoria.repository;

import com.example.venditabiglietti_categoria.model.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;


public interface CategoriaRepository extends JpaRepository<Categoria,Long> {

    public Optional<Categoria> findByNomeAndIsCancellatoFalse(String nome);
    public Optional<Categoria> findByIdAndIsCancellatoFalse(long id);



}
