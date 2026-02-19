package br.com.matheusosses.room_management.models.reserva.dto;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record CadastroReservaDto(

    @NotNull
    Long salaId,

    @NotNull
    Long usuarioId,

    @NotNull
    LocalDateTime inicioReserva,

    LocalDateTime fimReserva
) {
}
