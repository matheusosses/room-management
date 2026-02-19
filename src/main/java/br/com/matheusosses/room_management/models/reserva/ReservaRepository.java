package br.com.matheusosses.room_management.models.reserva;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ReservaRepository extends JpaRepository<Reserva, Long> {
    @Query("""
            SELECT COUNT(r) > 0
            FROM Reserva r
            WHERE r.sala.id = :salaId
            AND r.inicioReserva < :fimReserva
            AND r.fimReserva > :inicioReserva
            """)
    boolean verificaConflitoHorario(
        @Param("salaId") Long salaId,
        @Param("inicioReserva") LocalDateTime inicioReserva,
        @Param("fimReserva") LocalDateTime fimReserva
    );

    @Query("""
            SELECT r 
            FROM Reserva r 
            JOIN FETCH r.sala 
            JOIN FETCH r.usuario
            """)
    List<Reserva> buscarReservas();

    @Query("""
            SELECT r 
            FROM Reserva r 
            JOIN FETCH r.sala 
            JOIN FETCH r.usuario
            WHERE r.id = :id
            """)
    Optional<Reserva> reservaPorId(@Param("id") Long id);

    @Query("""
            SELECT COUNT(r) > 0
            FROM Reserva r
            WHERE r.sala.id = :salaId
            AND r.id != :reservaId
            AND r.inicioReserva < :fimReserva
            AND r.fimReserva > :inicioReserva
            """)
    boolean verificaConflitoAtualizacao(
        @Param("salaId") Long salaId,
        @Param("inicioReserva") LocalDateTime inicioReserva,
        @Param("fimReserva") LocalDateTime fimReserva,
        @Param("reservaId") Long reservaId
    );
}
