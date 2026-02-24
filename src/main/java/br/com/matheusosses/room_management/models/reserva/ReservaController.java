package br.com.matheusosses.room_management.models.reserva;

import br.com.matheusosses.room_management.models.reserva.dto.AtualizacaoReservaDto;
import br.com.matheusosses.room_management.models.reserva.dto.CadastroReservaDto;
import br.com.matheusosses.room_management.models.reserva.dto.ReservaDto;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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
    public ResponseEntity<Page<ReservaDto>> listarReservas(@PageableDefault(size = 10, sort = {"id"}) Pageable pageable) {
        return ResponseEntity.ok(service.listarReservas(pageable));
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
