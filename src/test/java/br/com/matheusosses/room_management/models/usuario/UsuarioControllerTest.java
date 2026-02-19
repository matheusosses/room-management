package br.com.matheusosses.room_management.models.usuario;

import br.com.matheusosses.room_management.models.usuario.dto.DadosUsuarioDto;
import br.com.matheusosses.room_management.models.usuario.dto.UsuarioDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UsuarioController.class)
@AutoConfigureMockMvc(addFilters = false)
class UsuarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @MockitoBean
    private UsuarioService service;

    @Test
    void deveRetornarStatusCreatedEBodyComUsuarioDto_quandoCriarUsuarioComDadosValidos() throws Exception {
        DadosUsuarioDto dto = new DadosUsuarioDto("João", "joao@email.com");
        UsuarioDto usuarioDto = new UsuarioDto(1L, "João", "joao@email.com");
        when(service.criarUsuario(any(DadosUsuarioDto.class))).thenReturn(usuarioDto);

        mockMvc.perform(post("/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nome").value("João"))
                .andExpect(jsonPath("$.email").value("joao@email.com"));

        verify(service).criarUsuario(any(DadosUsuarioDto.class));
    }

    @Test
    void deveRetornarStatusOkEListaDeUsuarios_quandoListarUsuarios() throws Exception {
        List<UsuarioDto> lista = List.of(
                new UsuarioDto(1L, "João", "joao@email.com"),
                new UsuarioDto(2L, "Maria", "maria@email.com")
        );
        when(service.listarUsuarios()).thenReturn(lista);

        mockMvc.perform(get("/usuarios"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].nome").value("João"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].nome").value("Maria"));

        verify(service).listarUsuarios();
    }

    @Test
    void deveRetornarStatusOkEUsuarioDto_quandoDetalharUsuarioComIdExistente() throws Exception {
        Long id = 1L;
        UsuarioDto usuarioDto = new UsuarioDto(id, "Pedro", "pedro@email.com");
        when(service.detalharUsuario(id)).thenReturn(usuarioDto);

        mockMvc.perform(get("/usuarios/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nome").value("Pedro"))
                .andExpect(jsonPath("$.email").value("pedro@email.com"));

        verify(service).detalharUsuario(id);
    }

    @Test
    void deveRetornarStatusOkEUsuarioDtoAtualizado_quandoAtualizarUsuarioComIdExistente() throws Exception {
        Long id = 1L;
        DadosUsuarioDto dto = new DadosUsuarioDto("Pedro Silva", "pedro.silva@email.com");
        UsuarioDto usuarioDto = new UsuarioDto(id, "Pedro Silva", "pedro.silva@email.com");
        when(service.atualizarUsuario(eq(id), any(DadosUsuarioDto.class))).thenReturn(usuarioDto);

        mockMvc.perform(put("/usuarios/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nome").value("Pedro Silva"))
                .andExpect(jsonPath("$.email").value("pedro.silva@email.com"));

        verify(service).atualizarUsuario(eq(id), any(DadosUsuarioDto.class));
    }
}
