package com.example.venditabiglietti_categoria.service.impl;
import com.example.venditabiglietti_categoria.model.Categoria;
import com.example.venditabiglietti_categoria.repository.CategoriaRepository;
import com.example.venditabiglietti_categoria.service.def.CategoriaServiceDef;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.util.ArrayList;
import java.util.List;



@RequiredArgsConstructor
@Service
public class CategoriaServiceImpl implements CategoriaServiceDef {

    private final CategoriaRepository categoriaRepository;

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
    @Override
    public void aggiungiCategoria(String nomeCategoria) {
        Categoria c=new Categoria();
        c.setNome(nomeCategoria);
        c.setCancellato(false);
        for(Categoria categoria:categoriaRepository.findAll()){
            if(categoria.getNome().equals(nomeCategoria)){
                throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE,"Non è possibile aggiungere questa categoria poichè già presente.");
            }
        }
        categoriaRepository.save(c);
    }
    @Override
    public void rimuoviCategoria(long idCategoria) {
        Categoria c=categoriaRepository.findById(idCategoria).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"Non esiste una categoria con questo Id"));
        c.setCancellato(true);
    }
    @Override
    public void modificaCategoria(long idCategoria, String nomeCategoria) {
        Categoria c=categoriaRepository.findById(idCategoria).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,"Non esiste una categoria con questo Id"));
        c.setNome(nomeCategoria);
        categoriaRepository.save(c);
    }
    @Override
    public Categoria findByNome(String nomeCategoria) {
        return categoriaRepository.findByNomeAndIsCancellatoFalse(nomeCategoria).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"Non esiste una categoria con questo nome"));
    }

    //FIXME bisogna fare come la ricerca per ids,di modo che se uno inserisce un nome sbagliato non si blocchi ma restituisca comunque tutti gli altri
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
}
