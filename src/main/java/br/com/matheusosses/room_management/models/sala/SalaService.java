package br.com.matheusosses.room_management.models.sala;

import br.com.matheusosses.room_management.exceptions.EntidadeNaoEncontradaException;
import br.com.matheusosses.room_management.models.sala.dto.DadosSalaDto;
import br.com.matheusosses.room_management.models.sala.dto.SalaDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SalaService {

    private final SalaRepository repository;

    public SalaService(SalaRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public SalaDto criarSala(DadosSalaDto dto) {
        Sala salaCriada = repository.save(new Sala(dto));
        return new SalaDto(salaCriada);
    }

    public Page<SalaDto> listarSalas(Pageable pageable) {
        return repository.findAll(pageable).map(SalaDto::new);
    }

    public SalaDto detalharSala(Long id) {
        Sala sala = repository.findById(id).orElseThrow(() -> new EntidadeNaoEncontradaException("Sala não encontrada para o id fornecido"));
        return new SalaDto(sala);
    }

    @Transactional
    public SalaDto atualizarSala(Long id, DadosSalaDto dto) {
        Sala sala = repository.findById(id)
            .orElseThrow(() -> new EntidadeNaoEncontradaException("Sala não encontrada para o id fornecido"));

        sala.atualizar(dto);
        return new SalaDto(sala);
    }
}
