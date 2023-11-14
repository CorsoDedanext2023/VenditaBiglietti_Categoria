package com.example.venditabiglietti_categoria.dto.request;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class CategoriaDto {
    @NotEmpty(message="Il campo Nome non può essere vuoto")
    @Pattern(regexp = "[A-Za-z]+(\\s[A-Za-z]+)*", message = "Il campo deve essere una frase di lettere")
    private String nome;

}
