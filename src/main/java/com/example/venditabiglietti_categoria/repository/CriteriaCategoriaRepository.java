package com.example.venditabiglietti_categoria.repository;

import com.example.venditabiglietti_categoria.dto.request.FiltroCategoriaDTORequest;
import com.example.venditabiglietti_categoria.model.Categoria;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Tuple;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class CriteriaCategoriaRepository {

    @Autowired
    private EntityManager manager;

    public List<Categoria> filtraCategorie(FiltroCategoriaDTORequest request) {
        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<Tuple> query = builder.createQuery(Tuple.class);
        Root<Categoria> root = query.from(Categoria.class);
        List<Predicate> predicate = new ArrayList<>();
        if(request.getNome() != null) predicate.add(builder.like(builder.lower(root.get("nome")), "%"+ request.getNome().toLowerCase()+"%"));
        predicate.add(builder.equal(root.get("isCancellato"), false));
        Predicate[] predicateArray = predicate.toArray(new Predicate[predicate.size()]);
        query.where(predicateArray);
        List<Tuple> list = manager.createQuery(query).getResultList();
        return list.stream().map(t -> t.get(0, Categoria.class)).toList();
    }
}
