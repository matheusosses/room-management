package br.com.matheusosses.room_management.models.usuario;

import br.com.matheusosses.room_management.models.usuario.dto.DadosUsuarioDto;
import br.com.matheusosses.room_management.models.usuario.dto.UsuarioDto;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioService service;

    public UsuarioController(UsuarioService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<UsuarioDto> criarUsuario(@RequestBody @Valid DadosUsuarioDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.criarUsuario(dto));
    }

    @GetMapping
    public ResponseEntity<List<UsuarioDto>> listarUsuarios() {
        return ResponseEntity.ok(service.listarUsuarios());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioDto> detalharUsuario(@PathVariable Long id) {
        return ResponseEntity.ok(service.detalharUsuario(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UsuarioDto> atualizarUsuario(@PathVariable Long id, @RequestBody @Valid DadosUsuarioDto dto) {
        return ResponseEntity.ok(service.atualizarUsuario(id, dto));
    }
}
