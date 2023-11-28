package com.example.venditabiglietti_categoria.controller;

import com.example.venditabiglietti_categoria.dto.request.CategoriaDto;
import com.example.venditabiglietti_categoria.dto.request.FiltroCategoriaDTORequest;
import com.example.venditabiglietti_categoria.dto.response.ErrorMessageDTOResponse;
import com.example.venditabiglietti_categoria.model.Categoria;
import com.example.venditabiglietti_categoria.service.impl.CategoriaServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.print.attribute.standard.Media;
import java.util.List;

@RestController
@RequestMapping("/categoria")
@RequiredArgsConstructor
@Validated
@Tag(name="Metodi Microservice Categoria",description = "metodi del Microservizio Categoria del progetto VenditaBiglietti")
public class CategoriaController {

    private final CategoriaServiceImpl categoriaService;

@Operation(summary = "Aggiunta della categoria",description = "Metodo per la creazione di una categoria, questo metodo prende in ingresso un json con il nome della categoria")
@ApiResponses(value = {
        @ApiResponse(description = "Categoria aggiunta con successo",responseCode = "201 Created",content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,schema = @Schema(implementation = String.class))),
        @ApiResponse(description = "Richiesta non valida,errore nelle validation",responseCode = "400 Bad_Request",content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,schema = @Schema(implementation = MethodArgumentNotValidException.class ))),
        @ApiResponse(description = "Si inserisce una categoria già esistente",responseCode = "406 Not Acceptable.",content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,schema = @Schema(implementation = ResponseStatusException.class)))
})
    @PostMapping("/aggiungicategoria")
    public ResponseEntity<String> aggiungiCategoria(@Valid
                                                    @RequestBody CategoriaDto requestDto){
        categoriaService.aggiungiCategoria(requestDto.getNome());
        return ResponseEntity.status(HttpStatus.CREATED).body("Categoria aggiunta con successo");
    }
@Operation(summary = "Modifica della categoria",description = "Metodo per la modifica di una categoria tramite l'inserimento in pathvariable di un id della categoria da modificare, e una stringa con il nuovo nome della categoria")
@ApiResponses(value = {
        @ApiResponse(description = "Categoria modificata con successo.",responseCode = "200 OK.",content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,schema = @Schema(implementation = String.class))),
        @ApiResponse(description = "Richiesta non valida, errore nelle validation(id errato o nome non conforme)",responseCode = "400 Bad_Request",content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,schema = @Schema(implementation = ConstraintViolationException.class))),
        @ApiResponse(description = "Id inserito non presente nel db",responseCode = "403 FORBIDDEN.",content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,schema = @Schema(implementation = ResponseStatusException.class)))
})
    @GetMapping("/modifica/{id}/{nomeCategoria}")
    public ResponseEntity<String> modificaCategoria(@Valid
                                                    @Positive(message = "l'id deve essere maggiore di 0.")
                                                    @PathVariable long id,
                                                    @Valid
                                                    @Pattern(regexp = "[A-Za-z]+(\\s[A-Za-z]+)*", message = "Il campo deve essere una frase di lettere")
                                                    @PathVariable String nomeCategoria){
        categoriaService.modificaCategoria(id,nomeCategoria);
        return ResponseEntity.status(HttpStatus.OK).body("Categoria modificata con successo.");
    }
@Operation(summary = "Rimozione di una categoria",description = "Metodo per la rimozione di una categoria,il boolean isCancellato si setta a TRUE,e non viene ripreso dalle repository")
@ApiResponses(value = {
        @ApiResponse(description = "Categoria rimossa con successo.",responseCode = "200 OK.",content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,schema = @Schema(implementation = String.class))),
        @ApiResponse(description = "Richiesta non valida, errore nelle validation(id errato)",responseCode = "400 Bad_Request",content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,schema = @Schema(implementation = ConstraintViolationException.class))),
        @ApiResponse(description = "Id inserito non presente nel db",responseCode = "403 FORBIDDEN.",content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,schema = @Schema(implementation = ResponseStatusException.class)))
})
    @GetMapping("/rimuovi/{id}")
    public ResponseEntity<String> rimuoviCategoria(@Valid
                                                   @Positive(message = "l'id deve essere maggiore di 0.")
                                                   @PathVariable long id){
        categoriaService.rimuoviCategoria(id);
        return ResponseEntity.status(HttpStatus.OK).body("Categoria rimossa con successo.");
    }
    @Operation(summary = "Trova una categoria in base all' id",description = "Metodo per il find di una categoria inserendo l'id direttamente nell' URL")
    @ApiResponses(value = {
            @ApiResponse(description = "Categoria Recuperata.",responseCode = "200 OK.",content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,schema = @Schema(implementation = Categoria.class))),
            @ApiResponse(description = "Richiesta non valida, errore nelle validation(id errato)",responseCode = "400 Bad_Request",content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,schema = @Schema(implementation = ConstraintViolationException.class))),
            @ApiResponse(description = "Id inserito non presente nel db",responseCode = "403 FORBIDDEN.",content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,schema = @Schema(implementation = ResponseStatusException.class)))
    })
    @GetMapping("/trova/{id}")
    public ResponseEntity<Categoria> getCategoria(@Valid
                                                      @Positive(message = "l'id deve essere maggiore di 0.")
                                                      @PathVariable long id){
        return ResponseEntity.status(HttpStatus.OK).body(categoriaService.findByIdAndCancellatoFalse(id));
    }
    @Operation(summary = "Trova una lista di categorie in base ad una lista di id.",description = "Metodo per il find di una categoria inserendo una lista di id come requestBody")
    @ApiResponses(value = {
            @ApiResponse(description = "Lista di Categorie Recuperata.",responseCode = "200 OK.",content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,schema = @Schema(implementation = Categoria.class))),
            @ApiResponse(description = "Richiesta non valida, errore nelle validation(id errato)",responseCode = "400 Bad_Request",content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,schema = @Schema(implementation = ConstraintViolationException.class))),
            @ApiResponse(description = "Uno o piu id nella lista sono errati",responseCode = "400 BAD_REQUEST.",content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,schema = @Schema(implementation = ResponseStatusException.class)))
    })
    @GetMapping("/trovaTuttiPerId")
    public ResponseEntity<List<Categoria>> getAllCategoriaById(@Valid
                                                                   @Positive(message = "l'id deve essere maggiore di 0.")
                                                                   @RequestBody List<Long> ids){
        return ResponseEntity.status(HttpStatus.OK).body(categoriaService.findAllByIds(ids));
    }
    @Operation(summary = "Trova una categoria in base al nome",description = "Metodo per il find di una categoria inserendo una RequestBody con il nome")
    @ApiResponses(value = {
            @ApiResponse(description = "Categoria Recuperata.",responseCode = "200 OK.",content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,schema = @Schema(implementation = Categoria.class))),
            @ApiResponse(description = "Richiesta non valida, errore nelle validation(id errato)",responseCode = "400 Bad_Request",content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,schema = @Schema(implementation = ConstraintViolationException.class))),
            @ApiResponse(description = "Body non presente",responseCode = "400 BAD_REQUEST.",content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,schema = @Schema(implementation = ResponseStatusException.class)))
    })
    @PostMapping("/findpernome")
    public ResponseEntity<Categoria> trovaPerNome( @Valid
                                                       @RequestBody CategoriaDto request){
        return ResponseEntity.status(HttpStatus.FOUND).body(categoriaService.findByNome(request.getNome()));
    }
    @Operation(summary = "Trova una lista di categorie in base ad una lista di nomi.",description = "Metodo per il find di una categoria inserendo una lista di nomi come requestBody")
    @ApiResponses(value = {
            @ApiResponse(description = "Lista di Categorie Recuperata.",responseCode = "200 OK.",content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,schema = @Schema(implementation = Categoria.class))),
            @ApiResponse(description = "Richiesta non valida, errore nelle validation(id errato)",responseCode = "400 Bad_Request",content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,schema = @Schema(implementation = ConstraintViolationException.class))),
            @ApiResponse(description = "Uno o piu id nella lista sono errati",responseCode = "400 BAD_REQUEST.",content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,schema = @Schema(implementation = ResponseStatusException.class))),
            @ApiResponse(description = "Body non presente",responseCode = "400 BAD_REQUEST.",content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,schema = @Schema(implementation = ResponseStatusException.class)))
    })
    @PostMapping("/trovaTuttiPerListaNomi")
    public  ResponseEntity<List<Categoria>> trovaTuttiPerListaNomi(
                                                                    @Valid
                                                                    @Positive(message = "l'id deve essere maggiore di 0.")
                                                                    @RequestBody List<String> nomiCategorie){
        return ResponseEntity.status(HttpStatus.OK).body(categoriaService.findAllByNomeList(nomiCategorie));
    }

    @PostMapping("/filtraCategorie")
    public ResponseEntity<List<Categoria>> filtraCategorie(@RequestBody FiltroCategoriaDTORequest request){
        return ResponseEntity.ok().body(categoriaService.filtraCategorie(request));
    }

}
