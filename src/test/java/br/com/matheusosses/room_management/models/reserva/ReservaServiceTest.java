package br.com.matheusosses.room_management.models.reserva;

import br.com.matheusosses.room_management.exceptions.EntidadeNaoEncontradaException;
import br.com.matheusosses.room_management.exceptions.RegraDeNegocioException;
import br.com.matheusosses.room_management.models.reserva.dto.AtualizacaoReservaDto;
import br.com.matheusosses.room_management.models.reserva.dto.CadastroReservaDto;
import br.com.matheusosses.room_management.models.reserva.dto.ReservaDto;
import br.com.matheusosses.room_management.models.sala.Sala;
import br.com.matheusosses.room_management.models.sala.SalaRepository;
import br.com.matheusosses.room_management.models.usuario.Usuario;
import br.com.matheusosses.room_management.models.usuario.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReservaServiceTest {

    @Mock
    private ReservaRepository reservaRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private SalaRepository salaRepository;

    @InjectMocks
    private ReservaService service;

    private static final Long SALA_ID = 1L;
    private static final Long USUARIO_ID = 1L;
    private static final Long RESERVA_ID = 1L;

    private Sala criarSala(Long id) {
        Sala sala = new Sala(new br.com.matheusosses.room_management.models.sala.dto.DadosSalaDto(10));
        ReflectionTestUtils.setField(sala, "id", id);
        return sala;
    }

    private Usuario criarUsuario(Long id) {
        Usuario usuario = new Usuario(new br.com.matheusosses.room_management.models.usuario.dto.DadosUsuarioDto("Nome", "email@test.com"));
        ReflectionTestUtils.setField(usuario, "id", id);
        return usuario;
    }

    private Reserva criarReserva(Sala sala, Usuario usuario, LocalDateTime inicio, LocalDateTime fim) {
        Reserva reserva = new Reserva(usuario, sala, inicio, fim);
        ReflectionTestUtils.setField(reserva, "id", RESERVA_ID);
        return reserva;
    }

    @Test
    void deveRetornarReservaDto_quandoCriarReservaComDadosValidosSemConflito() {
        LocalDateTime inicio = LocalDateTime.now().plusDays(1);
        LocalDateTime fim = inicio.plusHours(1);
        CadastroReservaDto dto = new CadastroReservaDto(SALA_ID, USUARIO_ID, inicio, fim);
        Sala sala = criarSala(SALA_ID);
        Usuario usuario = criarUsuario(USUARIO_ID);
        Reserva reservaSalva = criarReserva(sala, usuario, inicio, fim);

        when(salaRepository.findById(SALA_ID)).thenReturn(Optional.of(sala));
        when(reservaRepository.verificaConflitoHorario(SALA_ID, inicio, fim)).thenReturn(false);
        when(usuarioRepository.findById(USUARIO_ID)).thenReturn(Optional.of(usuario));
        when(reservaRepository.save(any(Reserva.class))).thenReturn(reservaSalva);

        ReservaDto resultado = service.criarReserva(dto);

        assertThat(resultado.salaId()).isEqualTo(SALA_ID);
        assertThat(resultado.usuarioId()).isEqualTo(USUARIO_ID);
        assertThat(resultado.inicioReserva()).isEqualTo(inicio);
        assertThat(resultado.fimReserva()).isEqualTo(fim);
        verify(reservaRepository).save(any(Reserva.class));
    }

    @Test
    void deveLancarRegraDeNegocioException_quandoCriarReservaComDataInicioNoPassado() {
        LocalDateTime inicioPassado = LocalDateTime.now().minusHours(1);
        CadastroReservaDto dto = new CadastroReservaDto(SALA_ID, USUARIO_ID, inicioPassado, null);

        assertThatThrownBy(() -> service.criarReserva(dto))
                .isInstanceOf(RegraDeNegocioException.class)
                .hasMessageContaining("A data de início não pode estar no passado");
    }

    @Test
    void deveLancarEntidadeNaoEncontradaException_quandoCriarReservaComSalaInexistente() {
        LocalDateTime inicio = LocalDateTime.now().plusDays(1);
        CadastroReservaDto dto = new CadastroReservaDto(SALA_ID, USUARIO_ID, inicio, null);
        when(salaRepository.findById(SALA_ID)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.criarReserva(dto))
                .isInstanceOf(EntidadeNaoEncontradaException.class)
                .hasMessageContaining("Não existe sala");
    }

    @Test
    void deveLancarEntidadeNaoEncontradaException_quandoCriarReservaComUsuarioInexistente() {
        LocalDateTime inicio = LocalDateTime.now().plusDays(1);
        CadastroReservaDto dto = new CadastroReservaDto(SALA_ID, USUARIO_ID, inicio, null);
        Sala sala = criarSala(SALA_ID);
        when(salaRepository.findById(SALA_ID)).thenReturn(Optional.of(sala));
        when(reservaRepository.verificaConflitoHorario(SALA_ID, inicio, inicio.plusHours(1))).thenReturn(false);
        when(usuarioRepository.findById(USUARIO_ID)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.criarReserva(dto))
                .isInstanceOf(EntidadeNaoEncontradaException.class)
                .hasMessageContaining("Não existe usuario");
    }

    @Test
    void deveLancarRegraDeNegocioException_quandoCriarReservaComConflitoDeHorario() {
        LocalDateTime inicio = LocalDateTime.now().plusDays(1);
        LocalDateTime fim = inicio.plusHours(1);
        CadastroReservaDto dto = new CadastroReservaDto(SALA_ID, USUARIO_ID, inicio, fim);
        Sala sala = criarSala(SALA_ID);
        when(salaRepository.findById(SALA_ID)).thenReturn(Optional.of(sala));
        when(reservaRepository.verificaConflitoHorario(SALA_ID, inicio, fim)).thenReturn(true);

        assertThatThrownBy(() -> service.criarReserva(dto))
                .isInstanceOf(RegraDeNegocioException.class)
                .hasMessageContaining("conflito de horario");
    }

    @Test
    void deveRetornarListaDeReservaDto_quandoListarReservas() {
        Sala sala = criarSala(SALA_ID);
        Usuario usuario = criarUsuario(USUARIO_ID);
        LocalDateTime inicio = LocalDateTime.now().plusDays(1);
        Reserva reserva = criarReserva(sala, usuario, inicio, inicio.plusHours(1));
        when(reservaRepository.buscarReservas()).thenReturn(List.of(reserva));

        List<ReservaDto> resultado = service.listarReservas();

        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).salaId()).isEqualTo(SALA_ID);
        assertThat(resultado.get(0).usuarioId()).isEqualTo(USUARIO_ID);
    }

    @Test
    void deveCancelarReserva_quandoCancelarReservaComIdExistente() {
        Sala sala = criarSala(SALA_ID);
        Usuario usuario = criarUsuario(USUARIO_ID);
        LocalDateTime inicio = LocalDateTime.now().plusDays(1);
        Reserva reserva = criarReserva(sala, usuario, inicio, inicio.plusHours(1));
        when(reservaRepository.reservaPorId(RESERVA_ID)).thenReturn(Optional.of(reserva));

        service.cancelarReserva(RESERVA_ID);

        assertThat(reserva.getStatus()).isEqualTo(Status.CANCELADA);
    }

    @Test
    void deveLancarEntidadeNaoEncontradaException_quandoCancelarReservaComIdInexistente() {
        when(reservaRepository.reservaPorId(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.cancelarReserva(999L))
                .isInstanceOf(EntidadeNaoEncontradaException.class)
                .hasMessageContaining("Não existe reserva");
    }

    @Test
    void deveRetornarReservaDto_quandoDetalharReservaComIdExistente() {
        Sala sala = criarSala(SALA_ID);
        Usuario usuario = criarUsuario(USUARIO_ID);
        LocalDateTime inicio = LocalDateTime.now().plusDays(1);
        Reserva reserva = criarReserva(sala, usuario, inicio, inicio.plusHours(1));
        when(reservaRepository.reservaPorId(RESERVA_ID)).thenReturn(Optional.of(reserva));

        ReservaDto resultado = service.detalharReserva(RESERVA_ID);

        assertThat(resultado.id()).isEqualTo(RESERVA_ID);
        assertThat(resultado.salaId()).isEqualTo(SALA_ID);
        assertThat(resultado.usuarioId()).isEqualTo(USUARIO_ID);
    }

    @Test
    void deveLancarEntidadeNaoEncontradaException_quandoDetalharReservaComIdInexistente() {
        when(reservaRepository.reservaPorId(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.detalharReserva(999L))
                .isInstanceOf(EntidadeNaoEncontradaException.class)
                .hasMessageContaining("Não existe reserva");
    }

    @Test
    void deveRetornarReservaDtoAtualizada_quandoAtualizarReservaComIdExistenteSemConflito() {
        Sala sala = criarSala(SALA_ID);
        Usuario usuario = criarUsuario(USUARIO_ID);
        LocalDateTime inicio = LocalDateTime.now().plusDays(1);
        LocalDateTime fim = inicio.plusHours(1);
        Reserva reserva = criarReserva(sala, usuario, inicio, fim);
        LocalDateTime novoInicio = inicio.plusHours(2);
        LocalDateTime novoFim = novoInicio.plusHours(1);
        AtualizacaoReservaDto dto = new AtualizacaoReservaDto(null, novoInicio, novoFim);

        when(reservaRepository.findById(RESERVA_ID)).thenReturn(Optional.of(reserva));
        when(reservaRepository.verificaConflitoAtualizacao(eq(SALA_ID), eq(novoInicio), eq(novoFim), eq(RESERVA_ID))).thenReturn(false);

        ReservaDto resultado = service.atualizarReserva(RESERVA_ID, dto);

        assertThat(resultado.id()).isEqualTo(RESERVA_ID);
        assertThat(reserva.getInicioReserva()).isEqualTo(novoInicio);
        assertThat(reserva.getFimReserva()).isEqualTo(novoFim);
    }

    @Test
    void deveLancarEntidadeNaoEncontradaException_quandoAtualizarReservaComIdInexistente() {
        AtualizacaoReservaDto dto = new AtualizacaoReservaDto(null, LocalDateTime.now().plusDays(1), null);
        when(reservaRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.atualizarReserva(999L, dto))
                .isInstanceOf(EntidadeNaoEncontradaException.class)
                .hasMessageContaining("Não existe reserva");
    }

    @Test
    void deveLancarRegraDeNegocioException_quandoAtualizarReservaComDataInicioNoPassado() {
        Sala sala = criarSala(SALA_ID);
        Usuario usuario = criarUsuario(USUARIO_ID);
        LocalDateTime inicio = LocalDateTime.now().plusDays(1);
        Reserva reserva = criarReserva(sala, usuario, inicio, inicio.plusHours(1));
        LocalDateTime inicioPassado = LocalDateTime.now().minusHours(1);
        AtualizacaoReservaDto dto = new AtualizacaoReservaDto(null, inicioPassado, null);

        when(reservaRepository.findById(RESERVA_ID)).thenReturn(Optional.of(reserva));

        assertThatThrownBy(() -> service.atualizarReserva(RESERVA_ID, dto))
                .isInstanceOf(RegraDeNegocioException.class)
                .hasMessageContaining("A nova data de início não pode estar no passado");
    }

    @Test
    void deveLancarRegraDeNegocioException_quandoAtualizarReservaComConflitoDeHorario() {
        Sala sala = criarSala(SALA_ID);
        Usuario usuario = criarUsuario(USUARIO_ID);
        LocalDateTime inicio = LocalDateTime.now().plusDays(1);
        LocalDateTime fim = inicio.plusHours(1);
        Reserva reserva = criarReserva(sala, usuario, inicio, fim);
        LocalDateTime novoInicio = inicio.plusHours(2);
        LocalDateTime novoFim = novoInicio.plusHours(1);
        AtualizacaoReservaDto dto = new AtualizacaoReservaDto(null, novoInicio, novoFim);

        when(reservaRepository.findById(RESERVA_ID)).thenReturn(Optional.of(reserva));
        when(reservaRepository.verificaConflitoAtualizacao(eq(SALA_ID), eq(novoInicio), eq(novoFim), eq(RESERVA_ID))).thenReturn(true);

        assertThatThrownBy(() -> service.atualizarReserva(RESERVA_ID, dto))
                .isInstanceOf(RegraDeNegocioException.class)
                .hasMessageContaining("Há conflito de horário");
    }
}
