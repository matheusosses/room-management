package br.com.matheusosses.room_management.models.reserva;

import br.com.matheusosses.room_management.exceptions.EntidadeNaoEncontradaException;
import br.com.matheusosses.room_management.exceptions.RegraDeNegocioException;
import br.com.matheusosses.room_management.models.reserva.dto.DadosReservaDto;
import br.com.matheusosses.room_management.models.reserva.dto.ReservaDto;
import br.com.matheusosses.room_management.models.sala.Sala;
import br.com.matheusosses.room_management.models.sala.SalaRepository;
import br.com.matheusosses.room_management.models.usuario.Usuario;
import br.com.matheusosses.room_management.models.usuario.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
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
    public ReservaDto criarReserva(DadosReservaDto dto) {
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
}
