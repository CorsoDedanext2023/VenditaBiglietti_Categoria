package com.example.venditabiglietti_categoria.controller;

import com.example.venditabiglietti_categoria.dto.request.CategoriaDto;
import com.example.venditabiglietti_categoria.model.Categoria;
import com.example.venditabiglietti_categoria.service.impl.CategoriaServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/categoria")
@RequiredArgsConstructor
public class CategoriaController {

    private final CategoriaServiceImpl categoriaService;


    @PostMapping("/aggiungicategoria")
    public ResponseEntity<String> aggiungiCategoria(@Valid @RequestBody CategoriaDto requestDto){
        categoriaService.aggiungiCategoria(requestDto.getNome());
        return ResponseEntity.status(HttpStatus.CREATED).body("Categoria aggiunta con successo");
    }
    @GetMapping("/modifica/{id}")
    public ResponseEntity<String> modificaCategoria(@PathVariable long id){
        return ResponseEntity.status(HttpStatus.OK).body("Categoria modificata con successo.");
    }

    @GetMapping("/rimuovi/{id}")
    public ResponseEntity<String> rimuoviCategoria(@PathVariable long id){
        return ResponseEntity.status(HttpStatus.OK).body("Categoria rimossa con successo");
    }



    @GetMapping("/trova/{id}")
    public ResponseEntity<Categoria> getCategoria(@PathVariable long id){
        return ResponseEntity.status(HttpStatus.OK).body(categoriaService.findById(id));
    }
    @GetMapping("/trovaTuttiPerId")
    public ResponseEntity<List<Categoria>> getAllCategoriaById(@RequestBody List<Long> ids){
        return ResponseEntity.status(HttpStatus.OK).body(categoriaService.findAllByIds(ids));
    }


    @PostMapping("/findpernome")
    public ResponseEntity<Categoria> trovaPerNome( @Valid @RequestBody CategoriaDto request){
        return ResponseEntity.status(HttpStatus.FOUND).body(categoriaService.findByNome(request.getNome()));
    }
    @PostMapping("/trovaTuttiPerListaNomi")
    public  ResponseEntity<List<Categoria>> trovaTuttiPerListaNomi(@RequestBody List<String> nomiCategorie){
        return ResponseEntity.status(HttpStatus.OK).body(categoriaService.findAllByNomeList(nomiCategorie));
    }





}
