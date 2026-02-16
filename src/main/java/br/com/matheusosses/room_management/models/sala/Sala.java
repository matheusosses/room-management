package br.com.matheusosses.room_management.models.sala;

import br.com.matheusosses.room_management.models.sala.dto.DadosSalaDto;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Optional;

@Getter
@Entity
@Table(name = "salas")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Sala {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Integer capacidade;

    public Sala(DadosSalaDto dto) {
        this.capacidade = dto.capacidade();
    }

    public void atualizar(DadosSalaDto dto) {
        this.capacidade = Optional.ofNullable(dto.capacidade()).orElse(this.capacidade);
    }
}
