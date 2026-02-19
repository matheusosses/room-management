package br.com.matheusosses.room_management.models.reserva;

import br.com.matheusosses.room_management.models.reserva.dto.AtualizacaoReservaDto;
import br.com.matheusosses.room_management.models.reserva.dto.CadastroReservaDto;
import br.com.matheusosses.room_management.models.reserva.dto.ReservaDto;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reservas")
public class ReservaController {

    public ReservaController(ReservaService service) {
        this.service = service;
    }

    private final ReservaService service;

    @PostMapping
    public ResponseEntity<ReservaDto> criarReserva(@RequestBody @Valid CadastroReservaDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.criarReserva(dto));
    }

    @GetMapping
    public ResponseEntity<List<ReservaDto>> listarReservas() {
        return ResponseEntity.ok().body(service.listarReservas());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> cancelarReserva(@PathVariable Long id) {
        service.cancelarReserva(id);
        return ResponseEntity.ok("Reserva cancelada com sucesso");
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReservaDto> detalharReserva(@PathVariable Long id) {
        return ResponseEntity.ok(service.detalharReserva(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReservaDto> atualizarReserva(@PathVariable Long id, @RequestBody AtualizacaoReservaDto dto) {
        return ResponseEntity.ok(service.atualizarReserva(id, dto));
    }
}
