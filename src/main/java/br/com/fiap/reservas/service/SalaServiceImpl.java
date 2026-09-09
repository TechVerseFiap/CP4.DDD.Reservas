package br.com.fiap.reservas.service;

import br.com.fiap.reservas.exception.RecursoNaoEncontradoException;
import br.com.fiap.reservas.model.Sala;
import br.com.fiap.reservas.repository.SalaRepository;
import br.com.fiap.reservas.view.request.SalaRequest;
import br.com.fiap.reservas.view.response.SalaResponse;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class SalaServiceImpl implements SalaService {

    private final SalaRepository repository;

    public SalaServiceImpl(SalaRepository repository) {
        this.repository = repository;
    }

    @Override
    public SalaResponse criar(SalaRequest request) {
        return toResponse(repository.save(Sala.criar(request.nome())));
    }

    @Override
    public List<SalaResponse> listar() {
        return repository.findAll().stream().map(this::toResponse).toList();
    }

    @Override
    public SalaResponse buscar(Long id) {
        return toResponse(findEntity(id));
    }

    private Sala findEntity(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Sala não encontrada"));
    }

    private SalaResponse toResponse(Sala sala) {
        return new SalaResponse(sala.getId(), sala.getNome());
    }
}
