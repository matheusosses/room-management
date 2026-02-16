package br.com.matheusosses.room_management.models.sala.dto;

import br.com.matheusosses.room_management.models.sala.Sala;

public record SalaDto(Long id, Integer capacidade) {
    public SalaDto(Sala sala){
        this(sala.getId(), sala.getCapacidade());
    }
}
