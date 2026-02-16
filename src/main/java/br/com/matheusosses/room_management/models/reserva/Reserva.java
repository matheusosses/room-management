package br.com.matheusosses.room_management.models.reserva;

import br.com.matheusosses.room_management.models.sala.Sala;
import br.com.matheusosses.room_management.models.usuario.Usuario;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
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

    @ManyToOne
    @JoinColumn(name = "sala_id", nullable = false)
    private Sala sala;

    @ManyToOne
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
}
