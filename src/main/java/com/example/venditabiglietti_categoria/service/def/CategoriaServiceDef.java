package com.example.venditabiglietti_categoria.service.def;

import com.example.venditabiglietti_categoria.dto.request.FiltroCategoriaDTORequest;
import com.example.venditabiglietti_categoria.model.Categoria;
import java.util.List;

public interface CategoriaServiceDef {

    public void aggiungiCategoria(String nomeCategoria);
    public void rimuoviCategoria(long idCategoria);
    public void modificaCategoria(long idCategoria,String nomeCategoria);
    public Categoria findByNome(String nomeCategoria);
    public List<Categoria> findAllByNomeList(List<String> nomiCategorie);
    public Categoria findById(long id);
    public Categoria findByIdAndCancellatoFalse(long id);

    public List<Categoria> findAllByIds(List<Long> ids);
    public List<Categoria> filtraCategorie(FiltroCategoriaDTORequest request);

}
