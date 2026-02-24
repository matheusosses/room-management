package br.com.matheusosses.room_management.models.usuario;

import br.com.matheusosses.room_management.exceptions.EntidadeNaoEncontradaException;
import br.com.matheusosses.room_management.exceptions.RegraDeNegocioException;
import br.com.matheusosses.room_management.models.usuario.dto.DadosUsuarioDto;
import br.com.matheusosses.room_management.models.usuario.dto.UsuarioDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UsuarioService {

    private final UsuarioRepository repository;

    public UsuarioService(UsuarioRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public UsuarioDto criarUsuario(DadosUsuarioDto dto) {
        if(repository.existsByEmail(dto.email())) {
            throw new RegraDeNegocioException("Ja existe um usuário cadastrado com esse email");
        }

        Usuario usuarioCriado = repository.save(new Usuario(dto));
        return new UsuarioDto(usuarioCriado);
    }

    public Page<UsuarioDto> listarUsuarios(Pageable pageable) {
        return repository.findAll(pageable).map(UsuarioDto::new);
    }

    public UsuarioDto detalharUsuario(Long id) {
        Usuario usuario = repository.findById(id).orElseThrow(() -> new EntidadeNaoEncontradaException("Usuario não encontrado para o id fornecido"));
        return new UsuarioDto(usuario);
    }

    @Transactional
    public UsuarioDto atualizarUsuario(Long id, DadosUsuarioDto dto) {
        Usuario usuario = repository.findById(id)
            .orElseThrow(() -> new EntidadeNaoEncontradaException("Usuário não encontrado para o id fornecido"));

        usuario.atualizar(dto);
        return new UsuarioDto(usuario);
    }
}
