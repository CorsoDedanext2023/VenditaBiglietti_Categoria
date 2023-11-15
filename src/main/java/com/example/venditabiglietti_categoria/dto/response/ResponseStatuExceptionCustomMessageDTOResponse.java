package com.example.venditabiglietti_categoria.dto.response;

import lombok.Data;
import org.springframework.http.HttpStatusCode;

@Data
public class ResponseStatuExceptionCustomMessageDTOResponse {

    private String reason;
    private HttpStatusCode code;
    private int statusCodeNumber;

}
