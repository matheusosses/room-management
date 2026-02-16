package br.com.matheusosses.room_management.models.sala.dto;

import jakarta.validation.constraints.NotNull;

public record DadosSalaDto(

    @NotNull
    Integer capacidade) {
}
