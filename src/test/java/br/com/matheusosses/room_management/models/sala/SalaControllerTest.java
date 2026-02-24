package br.com.matheusosses.room_management.models.sala;

import br.com.matheusosses.room_management.models.sala.dto.DadosSalaDto;
import br.com.matheusosses.room_management.models.sala.dto.SalaDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SalaController.class)
@AutoConfigureMockMvc(addFilters = false)
class SalaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @MockitoBean
    private SalaService service;

    @Test
    void deveRetornarStatusCreatedEBodyComSalaDto_quandoCriarSalaComDadosValidos() throws Exception {
        DadosSalaDto dto = new DadosSalaDto(10);
        SalaDto salaDto = new SalaDto(1L, 10);
        when(service.criarSala(any(DadosSalaDto.class))).thenReturn(salaDto);

        mockMvc.perform(post("/salas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.capacidade").value(10));

        verify(service).criarSala(any(DadosSalaDto.class));
    }

    @Test
    void deveRetornarStatusOkEPageDeSalas_quandoListarSalas() throws Exception {
        List<SalaDto> lista = List.of(new SalaDto(1L, 10), new SalaDto(2L, 20));
        when(service.listarSalas(any(Pageable.class))).thenReturn(new PageImpl<>(lista, PageRequest.of(0, 10), 2));

        mockMvc.perform(get("/salas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(2))
                .andExpect(jsonPath("$.content[0].id").value(1))
                .andExpect(jsonPath("$.content[0].capacidade").value(10))
                .andExpect(jsonPath("$.content[1].id").value(2))
                .andExpect(jsonPath("$.content[1].capacidade").value(20))
                .andExpect(jsonPath("$.totalElements").value(2));

        verify(service).listarSalas(any(Pageable.class));
    }

    @Test
    void deveRetornarStatusOkESalaDto_quandoDetalharSalaComIdExistente() throws Exception {
        Long id = 1L;
        SalaDto salaDto = new SalaDto(id, 10);
        when(service.detalharSala(id)).thenReturn(salaDto);

        mockMvc.perform(get("/salas/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.capacidade").value(10));

        verify(service).detalharSala(id);
    }

    @Test
    void deveRetornarStatusOkESalaDtoAtualizada_quandoAtualizarSalaComIdExistente() throws Exception {
        Long id = 1L;
        DadosSalaDto dto = new DadosSalaDto(25);
        SalaDto salaDto = new SalaDto(id, 25);
        when(service.atualizarSala(eq(id), any(DadosSalaDto.class))).thenReturn(salaDto);

        mockMvc.perform(put("/salas/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.capacidade").value(25));

        verify(service).atualizarSala(eq(id), any(DadosSalaDto.class));
    }
}
