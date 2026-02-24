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
import org.springframework.cglib.core.Local;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ReservaService {

    public ReservaService(ReservaRepository reservaRepository, UsuarioRepository usuarioRepository, SalaRepository salaRepository) {
        this.reservaRepository = reservaRepository;
        this.usuarioRepository = usuarioRepository;
        this.salaRepository = salaRepository;
    }

    private final ReservaRepository reservaRepository;
    private final UsuarioRepository usuarioRepository;
    private final SalaRepository salaRepository;

    @Transactional
    public ReservaDto criarReserva(CadastroReservaDto dto) {

        if(dto.inicioReserva().isBefore(LocalDateTime.now())){
            throw new RegraDeNegocioException("A data de início não pode estar no passado.");
        }

        LocalDateTime fimReserva = Optional.ofNullable(dto.fimReserva()).orElse(dto.inicioReserva().plusHours(1));

        Sala sala = salaRepository.findById(dto.salaId())
            .orElseThrow(() -> new EntidadeNaoEncontradaException("Não existe sala para o id fornecido"));

        if(reservaRepository.verificaConflitoHorario(dto.salaId(), dto.inicioReserva(), fimReserva)) {
            throw new RegraDeNegocioException("Há um conflito de horario nas datas fornecidas");
        }
        Usuario usuario = usuarioRepository.findById(dto.usuarioId())
            .orElseThrow(() -> new EntidadeNaoEncontradaException("Não existe usuario para o id fornecido"));

        Reserva novaReserva = reservaRepository.save(new Reserva(usuario, sala, dto.inicioReserva(), fimReserva));
        return new ReservaDto(novaReserva);
    }

    public Page<ReservaDto> listarReservas(Pageable pageable) {
        return reservaRepository.buscarReservas(pageable).map(ReservaDto::new);
    }

    @Transactional
    public void cancelarReserva(Long id) {
        Reserva reserva = reservaRepository.reservaPorId(id).orElseThrow(() -> new EntidadeNaoEncontradaException("Não existe reserva para o id forncecido"));
        reserva.cancelar();
    }

    public ReservaDto detalharReserva(Long id) {
        Reserva reserva = reservaRepository.reservaPorId(id).orElseThrow(() -> new EntidadeNaoEncontradaException("Não existe reserva para o id forncecido"));
        return new ReservaDto(reserva);
    }

    @Transactional
    public ReservaDto atualizarReserva(Long id, AtualizacaoReservaDto dto) {
        Reserva reserva = reservaRepository.findById(id)
            .orElseThrow(() -> new EntidadeNaoEncontradaException("Não existe reserva para o id fornecido"));

        Sala novaSala = null;
        Long salaId = reserva.getSala().getId();

        if (dto.salaId() != null) {
            novaSala = salaRepository.findById(dto.salaId())
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Não existe sala para o id fornecido"));
            salaId = novaSala.getId();
        }

        LocalDateTime inicioEfetivo = dto.inicioReserva() != null ? dto.inicioReserva() : reserva.getInicioReserva();
        LocalDateTime fimEfetivo = dto.fimReserva() != null ? dto.fimReserva() : reserva.getFimReserva();

        if (inicioEfetivo.isBefore(LocalDateTime.now())) {
            throw new RegraDeNegocioException("A nova data de início não pode estar no passado.");
        }

        if (reservaRepository.verificaConflitoAtualizacao(salaId, inicioEfetivo, fimEfetivo, reserva.getId())) {
            throw new RegraDeNegocioException("Há conflito de horário nas novas datas/sala fornecidas");
        }

        reserva.atualizar(novaSala, dto.inicioReserva(), dto.fimReserva());
        return new ReservaDto(reserva);
    }
}
