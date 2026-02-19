package br.com.matheusosses.room_management.models.reserva;

import br.com.matheusosses.room_management.models.reserva.dto.AtualizacaoReservaDto;
import br.com.matheusosses.room_management.models.reserva.dto.CadastroReservaDto;
import br.com.matheusosses.room_management.models.reserva.dto.ReservaDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ReservaController.class)
@AutoConfigureMockMvc(addFilters = false)
class ReservaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ReservaService service;

    private static ObjectMapper createObjectMapperWithJavaTime() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        return mapper;
    }

    @Test
    void deveRetornarStatusCreatedEBodyComReservaDto_quandoCriarReservaComDadosValidos() throws Exception {
        LocalDateTime inicio = LocalDateTime.now().plusDays(1);
        CadastroReservaDto dto = new CadastroReservaDto(1L, 1L, inicio, inicio.plusHours(1));
        ReservaDto reservaDto = new ReservaDto(1L, 1L, 1L, inicio, inicio.plusHours(1));
        when(service.criarReserva(any(CadastroReservaDto.class))).thenReturn(reservaDto);

        mockMvc.perform(post("/reservas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createObjectMapperWithJavaTime().writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.salaId").value(1))
                .andExpect(jsonPath("$.usuarioId").value(1));

        verify(service).criarReserva(any(CadastroReservaDto.class));
    }

    @Test
    void deveRetornarStatusOkEListaDeReservas_quandoListarReservas() throws Exception {
        LocalDateTime inicio = LocalDateTime.now().plusDays(1);
        List<ReservaDto> lista = List.of(new ReservaDto(1L, 1L, 1L, inicio, inicio.plusHours(1)));
        when(service.listarReservas()).thenReturn(lista);

        mockMvc.perform(get("/reservas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].salaId").value(1))
                .andExpect(jsonPath("$[0].usuarioId").value(1));

        verify(service).listarReservas();
    }

    @Test
    void deveRetornarStatusOkEReservaDto_quandoDetalharReservaComIdExistente() throws Exception {
        Long id = 1L;
        LocalDateTime inicio = LocalDateTime.now().plusDays(1);
        ReservaDto reservaDto = new ReservaDto(id, 1L, 1L, inicio, inicio.plusHours(1));
        when(service.detalharReserva(id)).thenReturn(reservaDto);

        mockMvc.perform(get("/reservas/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.salaId").value(1))
                .andExpect(jsonPath("$.usuarioId").value(1));

        verify(service).detalharReserva(id);
    }

    @Test
    void deveRetornarStatusOk_quandoCancelarReservaComIdExistente() throws Exception {
        Long id = 1L;

        mockMvc.perform(delete("/reservas/{id}", id))
                .andExpect(status().isOk())
                .andExpect(content().string("Reserva cancelada com sucesso"));

        verify(service).cancelarReserva(id);
    }

    @Test
    void deveRetornarStatusOkEReservaDtoAtualizada_quandoAtualizarReservaComIdExistente() throws Exception {
        Long id = 1L;
        LocalDateTime novoInicio = LocalDateTime.now().plusDays(2);
        AtualizacaoReservaDto dto = new AtualizacaoReservaDto(1L, novoInicio, novoInicio.plusHours(1));
        ReservaDto reservaDto = new ReservaDto(id, 1L, 1L, novoInicio, novoInicio.plusHours(1));
        when(service.atualizarReserva(eq(id), any(AtualizacaoReservaDto.class))).thenReturn(reservaDto);

        mockMvc.perform(put("/reservas/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createObjectMapperWithJavaTime().writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.salaId").value(1))
                .andExpect(jsonPath("$.usuarioId").value(1));

        verify(service).atualizarReserva(eq(id), any(AtualizacaoReservaDto.class));
    }
}
