package br.com.matheusosses.room_management.models.usuario.dto;

import jakarta.validation.constraints.NotBlank;

public record DadosUsuarioDto(

    @NotBlank
    String nome,

    @NotBlank
    String email) {
}
