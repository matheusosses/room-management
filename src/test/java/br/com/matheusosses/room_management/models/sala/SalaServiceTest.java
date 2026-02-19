package br.com.matheusosses.room_management.models.sala;

import br.com.matheusosses.room_management.exceptions.EntidadeNaoEncontradaException;
import br.com.matheusosses.room_management.models.sala.dto.DadosSalaDto;
import br.com.matheusosses.room_management.models.sala.dto.SalaDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SalaServiceTest {

    @Mock
    private SalaRepository repository;

    @InjectMocks
    private SalaService service;

    @Test
    void deveRetornarSalaDto_quandoCriarSalaComDadosValidos() {
        DadosSalaDto dto = new DadosSalaDto(10);
        Sala salaSalva = new Sala(dto);
        ReflectionTestUtils.setField(salaSalva, "id", 1L);

        when(repository.save(any(Sala.class))).thenReturn(salaSalva);

        SalaDto resultado = service.criarSala(dto);

        assertThat(resultado.id()).isEqualTo(1L);
        assertThat(resultado.capacidade()).isEqualTo(10);
        verify(repository).save(any(Sala.class));
    }

    @Test
    void deveRetornarListaDeSalaDto_quandoListarSalas() {
        Sala sala = new Sala(new DadosSalaDto(5));
        ReflectionTestUtils.setField(sala, "id", 1L);
        when(repository.findAll()).thenReturn(List.of(sala));

        List<SalaDto> resultado = service.listarSalas();

        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).id()).isEqualTo(1L);
        assertThat(resultado.get(0).capacidade()).isEqualTo(5);
    }

    @Test
    void deveRetornarSalaDto_quandoDetalharSalaComIdExistente() {
        Long id = 1L;
        Sala sala = new Sala(new DadosSalaDto(8));
        ReflectionTestUtils.setField(sala, "id", id);
        when(repository.findById(id)).thenReturn(Optional.of(sala));

        SalaDto resultado = service.detalharSala(id);

        assertThat(resultado.id()).isEqualTo(id);
        assertThat(resultado.capacidade()).isEqualTo(8);
    }

    @Test
    void deveLancarEntidadeNaoEncontradaException_quandoDetalharSalaComIdInexistente() {
        Long id = 999L;
        when(repository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.detalharSala(id))
                .isInstanceOf(EntidadeNaoEncontradaException.class)
                .hasMessageContaining("Sala não encontrada");
    }

    @Test
    void deveRetornarSalaDtoAtualizada_quandoAtualizarSalaComIdExistente() {
        Long id = 1L;
        DadosSalaDto dtoAtualizado = new DadosSalaDto(20);
        Sala sala = new Sala(new DadosSalaDto(10));
        ReflectionTestUtils.setField(sala, "id", id);
        when(repository.findById(id)).thenReturn(Optional.of(sala));

        SalaDto resultado = service.atualizarSala(id, dtoAtualizado);

        assertThat(resultado.id()).isEqualTo(id);
        assertThat(resultado.capacidade()).isEqualTo(20);
    }

    @Test
    void deveLancarEntidadeNaoEncontradaException_quandoAtualizarSalaComIdInexistente() {
        Long id = 999L;
        DadosSalaDto dto = new DadosSalaDto(10);
        when(repository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.atualizarSala(id, dto))
                .isInstanceOf(EntidadeNaoEncontradaException.class)
                .hasMessageContaining("Sala não encontrada");
    }
}
