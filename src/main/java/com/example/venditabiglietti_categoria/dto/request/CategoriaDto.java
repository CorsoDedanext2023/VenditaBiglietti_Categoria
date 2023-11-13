package com.example.venditabiglietti_categoria.dto.request;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class CategoriaDto {
    @NotEmpty(message="Il campo Nome non può essere vuoto")
    private String nome;

}
