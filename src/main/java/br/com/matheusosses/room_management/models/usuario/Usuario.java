package br.com.matheusosses.room_management.models.usuario;

import br.com.matheusosses.room_management.models.usuario.dto.DadosUsuarioDto;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Optional;

@Getter
@Entity
@Table(name = "usuarios")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false, unique = true)
    private String email;

    public Usuario(DadosUsuarioDto dto) {
        this.email = dto.email();
        this.nome = dto.nome();
    }

    public void atualizar(DadosUsuarioDto dto) {
        this.nome = Optional.ofNullable(dto.nome()).orElse(this.nome);
        this.email = Optional.ofNullable(dto.email()).orElse(this.email);
    }
}
