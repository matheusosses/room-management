package br.com.matheusosses.room_management.models.reserva;

import br.com.matheusosses.room_management.models.reserva.dto.DadosReservaDto;
import br.com.matheusosses.room_management.models.reserva.dto.ReservaDto;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/reservas")
public class ReservaController {

    public ReservaController(ReservaService service) {
        this.service = service;
    }

    private final ReservaService service;

    @PostMapping
    public ResponseEntity<ReservaDto> criarReserva(@RequestBody @Valid DadosReservaDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.criarReserva(dto));
    }
}
