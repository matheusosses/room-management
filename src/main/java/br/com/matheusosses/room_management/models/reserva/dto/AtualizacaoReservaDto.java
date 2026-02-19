package br.com.matheusosses.room_management.models.reserva.dto;

import java.time.LocalDateTime;

public record AtualizacaoReservaDto(Long salaId, LocalDateTime inicioReserva, LocalDateTime fimReserva) {
}
