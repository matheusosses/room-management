package br.com.matheusosses.room_management.models.reserva.dto;

import br.com.matheusosses.room_management.models.reserva.Reserva;

import java.time.LocalDateTime;

public record ReservaDto(Long id, Long salaId, Long usuarioId, LocalDateTime inicioReserva, LocalDateTime fimReserva) {
    public ReservaDto(Reserva reserva) {
        this(reserva.getId(),
            reserva.getSala().getId(),
            reserva.getUsuario().getId(),
            reserva.getInicioReserva(),
            reserva.getFimReserva());
    }
}
