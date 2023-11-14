package com.example.venditabiglietti_categoria;

import com.example.venditabiglietti_categoria.dto.request.CategoriaDto;
import com.example.venditabiglietti_categoria.model.Categoria;
import com.example.venditabiglietti_categoria.repository.CategoriaRepository;
import com.example.venditabiglietti_categoria.service.impl.CategoriaServiceImpl;
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
import java.util.ArrayList;
import java.util.List;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ContextConfiguration(classes = TicketSellerCategoriaApplication.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@AutoConfigureMockMvc
class TicketSellerCategoriaApplicationTests {

    @Autowired
    private  MockMvc mock;
    @Autowired
    private CategoriaRepository repo;
    @Autowired
    private CategoriaServiceImpl service;

    @Test
    @Order(1)
    public void creaCategoriaSenzaBody() throws Exception{
        mock.perform(MockMvcRequestBuilders.post("/categoria/aggiungicategoria")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
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
    @Test
    @Order(3)
    public void CreaCategoriaStessonome()throws Exception{
        CategoriaDto categoriadtoRequestTest=new CategoriaDto();
        categoriadtoRequestTest.setNome("Festa di carnevale");
        String json=new ObjectMapper().writeValueAsString(categoriadtoRequestTest);
        mock.perform(MockMvcRequestBuilders.post("/categoria/aggiungicategoria")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotAcceptable())
                .andDo(print());
    }
    @Test
    @Order(4)
    public void trovaPerIdOk() throws Exception{
        long id=2;
        mock.perform(MockMvcRequestBuilders.get("/categoria/trova/{id}",id)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(print());

    }

    @Test
    @Order(5)
    public void trovaPerIdNotOk() throws Exception{
        long id=50;
        mock.perform(MockMvcRequestBuilders.get("/categoria/trova/{id}",id)
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(MockMvcResultMatchers.status().isNotFound())
                        .andDo(print());
    }

    @Test
    @Order(6)
    public void TestAttributoCategoria() {
        assertThat(repo.findByNomeAndIsCancellatoFalse("concerto")).get()
                .extracting(Categoria::getNome)
                .isEqualTo("concerto");
    }

    @Test
    @Order(7)
    public void TestListaPerIds(){
        List<Long> ids=new ArrayList<>(List.of(1L,3L,5L,9L));
        List<Categoria> items= repo.findAllById(ids);
        assertEquals(4,items.size());
    }
    @Test
    @Order(8)
    public void TestListaPerIdsVuota(){
        List<Long> ids=new ArrayList<>();
        List<Categoria> items=repo.findAllById(ids);
        assertEquals(0,items.size());
    }
    @Test
    @Order(9)
    public void TestListaPerNomeList(){
        List<String> listaNomi= new ArrayList<>(List.of("Concerto di rock","Festa di carnevale","Gara di cucina"));
        List<Categoria> items=service.findAllByNomeList(listaNomi);
        assertEquals(3,items.size());
    }
    @Test
    @Order(10)
    public void TestListaPerNomeListVuota()throws Exception{
        List<String> listaStringheVuota=new ArrayList<>();
        mock.perform(MockMvcRequestBuilders.post("/categoria/trovaTuttiPerListaNomi")
                .contentType(MediaType.APPLICATION_JSON)
                .content(listaStringheVuota.toString()))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andDo(print());
    }
    @Test
    @Order(11)
    public void TestModificaOk() throws Exception{
        long id=5;
        String testo="Gara di canto";
        mock.perform(MockMvcRequestBuilders.get("/categoria/modifica/{id}/{nomeCategoria}",id,testo)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(print());
    }

    @Test
    @Order(12)
    public void TestModificaIdSbagliato() throws Exception{
        long id=50;
        String testo="Gara di canto";
        mock.perform(MockMvcRequestBuilders.get("/categoria/modifica/{id}/{nomeCategoria}",id,testo)
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(MockMvcResultMatchers.status().isForbidden())
                        .andDo(print());
    }

    @Test
    @Order(13)
    public void TestModificaSenzaTesto()throws Exception{
        long id=5;
        mock.perform(MockMvcRequestBuilders.get("/categoria/modifica/{id}/{nomeCategoria}",id,5)
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(MockMvcResultMatchers.status().isBadRequest())
                        .andDo(print());
    }

    @Test
    @Order(14)
    public void TestModificaSenzaId() throws Exception{
        String nome="Sagra dello scrocchiarello";
        mock.perform(MockMvcRequestBuilders.get("/categoria/modifica/{id}/{nomeCategoria}","a",nome)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andDo(print());
    }

}
