package br.com.matheusosses.room_management.models.reserva;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;

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
}
