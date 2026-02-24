package br.com.matheusosses.room_management.models.sala;

import br.com.matheusosses.room_management.models.sala.dto.DadosSalaDto;
import br.com.matheusosses.room_management.models.sala.dto.SalaDto;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/salas")
public class SalaController {

    private final SalaService service;

    public SalaController(SalaService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<SalaDto> criarSala(@RequestBody @Valid DadosSalaDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.criarSala(dto));
    }

    @GetMapping
    public ResponseEntity<Page<SalaDto>> listarSalas(@PageableDefault(size = 10, sort = {"id"}) Pageable pageable) {
        return ResponseEntity.ok(service.listarSalas(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SalaDto> detalharSala(@PathVariable Long id) {
        return ResponseEntity.ok(service.detalharSala(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SalaDto> atualizarSala(@PathVariable Long id, @RequestBody @Valid DadosSalaDto dto) {
        return ResponseEntity.ok(service.atualizarSala(id, dto));
    }
}
