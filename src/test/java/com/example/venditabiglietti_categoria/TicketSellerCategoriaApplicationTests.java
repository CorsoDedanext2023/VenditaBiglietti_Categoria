package com.example.venditabiglietti_categoria;

import com.example.venditabiglietti_categoria.dto.request.CategoriaDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest
@ContextConfiguration(classes = TicketSellerCategoriaApplication.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@AutoConfigureMockMvc
class TicketSellerCategoriaApplicationTests {

    @Autowired
    private  MockMvc mock;

    @Test
    @Order(1)
    public void creaCategoriaSenzaBody() throws Exception{
        mock.perform(MockMvcRequestBuilders.post("/categoria/aggiungicategoria")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andReturn();
    }
    @Test
    @Order(2)
    public void creaCategoriaConBody() throws Exception{
        CategoriaDto categoriadtoRequestTest=new CategoriaDto();
        categoriadtoRequestTest.setNome("concerto");
        String json=new ObjectMapper().writeValueAsString(categoriadtoRequestTest);
        mock.perform(MockMvcRequestBuilders.post("/categoria/aggiungicategoria")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andDo(print());

    }

    /*@Test
    @Order(3)
    public void trovaPerId() throws Exception{
        mock.perform(MockMvcRequestBuilders.post("/categoria/"))
    }*/

}
