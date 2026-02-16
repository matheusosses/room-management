package br.com.matheusosses.room_management.models.usuario.dto;

import br.com.matheusosses.room_management.models.usuario.Usuario;

public record UsuarioDto(Long id, String nome, String email) {
    public UsuarioDto(Usuario usuario){
        this(usuario.getId(), usuario.getNome(), usuario.getEmail());
    }
}
