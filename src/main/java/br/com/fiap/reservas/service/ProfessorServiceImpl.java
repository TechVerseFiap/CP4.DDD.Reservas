package br.com.fiap.reservas.service;

import br.com.fiap.reservas.exception.RecursoNaoEncontradoException;
import br.com.fiap.reservas.model.Professor;
import br.com.fiap.reservas.repository.ProfessorRepository;
import br.com.fiap.reservas.view.request.ProfessorRequest;
import br.com.fiap.reservas.view.response.ProfessorResponse;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ProfessorServiceImpl implements ProfessorService {

    private final ProfessorRepository repository;

    public ProfessorServiceImpl(ProfessorRepository repository) {
        this.repository = repository;
    }

    @Override
    public ProfessorResponse criar(ProfessorRequest request) {
        Professor professor = Professor.criar(request.nome(), request.email());
        return toResponse(repository.save(professor));
    }

    @Override
    public List<ProfessorResponse> listar() {
        return repository.findAll().stream().map(this::toResponse).toList();
    }

    @Override
    public ProfessorResponse buscar(Long id) {
        return toResponse(findEntity(id));
    }

    private Professor findEntity(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Professor não encontrado"));
    }

    private ProfessorResponse toResponse(Professor professor) {
        return new ProfessorResponse(professor.getId(), professor.getNome(), professor.getEmail());
    }
}
