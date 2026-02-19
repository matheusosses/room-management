package br.com.matheusosses.room_management.models.usuario;

import br.com.matheusosses.room_management.exceptions.EntidadeNaoEncontradaException;
import br.com.matheusosses.room_management.exceptions.RegraDeNegocioException;
import br.com.matheusosses.room_management.models.usuario.dto.DadosUsuarioDto;
import br.com.matheusosses.room_management.models.usuario.dto.UsuarioDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository repository;

    @InjectMocks
    private UsuarioService service;

    @Test
    void deveRetornarUsuarioDto_quandoCriarUsuarioComEmailNaoCadastrado() {
        DadosUsuarioDto dto = new DadosUsuarioDto("João", "joao@email.com");
        Usuario usuarioSalvo = new Usuario(dto);
        ReflectionTestUtils.setField(usuarioSalvo, "id", 1L);

        when(repository.existsByEmail(dto.email())).thenReturn(false);
        when(repository.save(any(Usuario.class))).thenReturn(usuarioSalvo);

        UsuarioDto resultado = service.criarUsuario(dto);

        assertThat(resultado.id()).isEqualTo(1L);
        assertThat(resultado.nome()).isEqualTo("João");
        assertThat(resultado.email()).isEqualTo("joao@email.com");
        verify(repository).save(any(Usuario.class));
    }

    @Test
    void deveLancarRegraDeNegocioException_quandoCriarUsuarioComEmailJaCadastrado() {
        DadosUsuarioDto dto = new DadosUsuarioDto("João", "joao@email.com");
        when(repository.existsByEmail(dto.email())).thenReturn(true);

        assertThatThrownBy(() -> service.criarUsuario(dto))
                .isInstanceOf(RegraDeNegocioException.class)
                .hasMessageContaining("Ja existe um usuário cadastrado com esse email");
    }

    @Test
    void deveRetornarListaDeUsuarioDto_quandoListarUsuarios() {
        Usuario usuario = new Usuario(new DadosUsuarioDto("Maria", "maria@email.com"));
        ReflectionTestUtils.setField(usuario, "id", 1L);
        when(repository.findAll()).thenReturn(List.of(usuario));

        List<UsuarioDto> resultado = service.listarUsuarios();

        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).id()).isEqualTo(1L);
        assertThat(resultado.get(0).nome()).isEqualTo("Maria");
        assertThat(resultado.get(0).email()).isEqualTo("maria@email.com");
    }

    @Test
    void deveRetornarUsuarioDto_quandoDetalharUsuarioComIdExistente() {
        Long id = 1L;
        Usuario usuario = new Usuario(new DadosUsuarioDto("Pedro", "pedro@email.com"));
        ReflectionTestUtils.setField(usuario, "id", id);
        when(repository.findById(id)).thenReturn(Optional.of(usuario));

        UsuarioDto resultado = service.detalharUsuario(id);

        assertThat(resultado.id()).isEqualTo(id);
        assertThat(resultado.nome()).isEqualTo("Pedro");
        assertThat(resultado.email()).isEqualTo("pedro@email.com");
    }

    @Test
    void deveLancarEntidadeNaoEncontradaException_quandoDetalharUsuarioComIdInexistente() {
        Long id = 999L;
        when(repository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.detalharUsuario(id))
                .isInstanceOf(EntidadeNaoEncontradaException.class)
                .hasMessageContaining("Usuario não encontrado");
    }

    @Test
    void deveRetornarUsuarioDtoAtualizado_quandoAtualizarUsuarioComIdExistente() {
        Long id = 1L;
        DadosUsuarioDto dtoAtualizado = new DadosUsuarioDto("Pedro Silva", "pedro.silva@email.com");
        Usuario usuario = new Usuario(new DadosUsuarioDto("Pedro", "pedro@email.com"));
        ReflectionTestUtils.setField(usuario, "id", id);
        when(repository.findById(id)).thenReturn(Optional.of(usuario));

        UsuarioDto resultado = service.atualizarUsuario(id, dtoAtualizado);

        assertThat(resultado.id()).isEqualTo(id);
        assertThat(resultado.nome()).isEqualTo("Pedro Silva");
        assertThat(resultado.email()).isEqualTo("pedro.silva@email.com");
    }

    @Test
    void deveLancarEntidadeNaoEncontradaException_quandoAtualizarUsuarioComIdInexistente() {
        Long id = 999L;
        DadosUsuarioDto dto = new DadosUsuarioDto("Nome", "email@test.com");
        when(repository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.atualizarUsuario(id, dto))
                .isInstanceOf(EntidadeNaoEncontradaException.class)
                .hasMessageContaining("Usuário não encontrado");
    }
}
