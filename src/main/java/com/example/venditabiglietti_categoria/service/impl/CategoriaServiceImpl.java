package com.example.venditabiglietti_categoria.service.impl;
import com.example.venditabiglietti_categoria.dto.request.FiltroCategoriaDTORequest;
import com.example.venditabiglietti_categoria.model.Categoria;
import com.example.venditabiglietti_categoria.repository.CategoriaRepository;
import com.example.venditabiglietti_categoria.repository.CriteriaCategoriaRepository;
import com.example.venditabiglietti_categoria.service.def.CategoriaServiceDef;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.util.ArrayList;
import java.util.List;



@RequiredArgsConstructor
@Service
public class CategoriaServiceImpl implements CategoriaServiceDef {

    private final CategoriaRepository categoriaRepository;
    private final CriteriaCategoriaRepository criteriaCategoriaRepository;
//questo non verrà usato
    public Categoria findById(long id){
        return categoriaRepository.findById(id).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,"Non esiste una categoria con questo Id"));
    }
    @Override
    public Categoria findByIdAndCancellatoFalse(long id) {
        return categoriaRepository.findByIdAndIsCancellatoFalse(id).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,"Non esiste una categoria con questo Id"));
    }
    @Override
    public List<Categoria> findAllByIds(List<Long> ids) {
        List<Categoria> listaCategoriePerListaid= categoriaRepository.findAllById(ids).stream().filter(e->!e.isCancellato()).toList();
        if(listaCategoriePerListaid.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        } return listaCategoriePerListaid;
    }

    @Transactional(rollbackOn = DataAccessException.class)
    @Override
    public void aggiungiCategoria(String nomeCategoria) {
        Categoria c=new Categoria();
        c.setNome(nomeCategoria);
        c.setCancellato(false);
        for(Categoria categoria:categoriaRepository.findAll()){
           if(categoria.getNome().equals(nomeCategoria)){
               if(categoria.isCancellato()){
                   categoria.setCancellato(false);
                   categoriaRepository.save(categoria);
               } else {
                   throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE,"Non è possibile aggiungere questa categoria poiché già presente.");
               }
           } else {
               categoriaRepository.save(c);
           }
        }
    }
    @Transactional(rollbackOn = DataAccessException.class)
    @Override
    public void rimuoviCategoria(long idCategoria) {
        Categoria c=categoriaRepository.findById(idCategoria).orElseThrow(()->new ResponseStatusException(HttpStatus.FORBIDDEN,"Non esiste una categoria con questo Id"));
        c.setCancellato(true);
        categoriaRepository.save(c);
    }
    @Transactional(rollbackOn = DataAccessException.class)
    @Override
    public void modificaCategoria(long idCategoria, String nomeCategoria) {
        Categoria c=categoriaRepository.findById(idCategoria).orElseThrow(()-> new ResponseStatusException(HttpStatus.FORBIDDEN,"Non esiste una categoria con questo Id"));
        c.setNome(nomeCategoria);
        categoriaRepository.save(c);
    }
    @Override
    public Categoria findByNome(String nomeCategoria) {
        return categoriaRepository.findByNomeAndIsCancellatoFalse(nomeCategoria).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"Non esiste una categoria con questo nome"));
    }

    @Override
    public List<Categoria> findAllByNomeList(List<String> nomiCategorie) {
        List<Categoria> categorieTrovate=new ArrayList<>();
        for (String nomeCategoria:nomiCategorie) {
            Categoria cat=categoriaRepository.findByNomeAndIsCancellatoFalse(nomeCategoria).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"il nome"+nomeCategoria+"non è valido e non è stato trovato"));
            categorieTrovate.add(cat);
        }  if(categorieTrovate.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        } return categorieTrovate;
    }

    @Override
    public List<Categoria> filtraCategorie(FiltroCategoriaDTORequest request) {
        return criteriaCategoriaRepository.filtraCategorie(request);
    }
}
