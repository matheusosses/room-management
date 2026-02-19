package br.com.matheusosses.room_management.models.reserva;

import br.com.matheusosses.room_management.exceptions.RegraDeNegocioException;
import br.com.matheusosses.room_management.models.sala.Sala;
import br.com.matheusosses.room_management.models.usuario.Usuario;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.Optional;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
@Table(name = "reservas")
public class Reserva {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sala_id", nullable = false)
    private Sala sala;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    private LocalDateTime inicioReserva;

    private LocalDateTime fimReserva;

    @Enumerated(EnumType.STRING)
    private Status status;

    public Reserva(Usuario usuario, Sala sala, LocalDateTime inicioReserva, LocalDateTime fimReserva){
        this.usuario = usuario;
        this.sala = sala;
        this.inicioReserva = inicioReserva;
        this.fimReserva = fimReserva;
        this.status = Status.CONFIRMADA;
    }

    public void cancelar() {
        if(this.status == Status.CANCELADA) {
            throw new RegraDeNegocioException("Esta reserva ja foi cancelada.");
        }

        if(this.inicioReserva.isBefore(LocalDateTime.now())) {
            throw new RegraDeNegocioException("Não é possível cancelar uma reserva passada ou em andamento");
        }

        this.status = Status.CANCELADA;
    }

    public void atualizar(Sala novaSala, LocalDateTime novoInicio, LocalDateTime novoFim) {
        if(this.status == Status.CANCELADA) {
            throw new RegraDeNegocioException("Esta reserva ja foi cancelada.");
        }

        if(this.inicioReserva.isBefore(LocalDateTime.now())) {
            throw new RegraDeNegocioException("Não é possível cancelar uma reserva passada ou em andamento");
        }

        this.sala = Optional.ofNullable(novaSala).orElse(this.sala);
        this.inicioReserva = Optional.ofNullable(novoInicio).orElse(this.inicioReserva);
        this.fimReserva = Optional.ofNullable(novoFim).orElse(this.fimReserva);
    }
}
