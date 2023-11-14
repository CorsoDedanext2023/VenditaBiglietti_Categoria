package com.example.venditabiglietti_categoria.dto.response;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data
@NoArgsConstructor
public class ErrorMessageDTOResponse {

    private String fieldName;
    private String message;
    private String invalid_Data;

}
