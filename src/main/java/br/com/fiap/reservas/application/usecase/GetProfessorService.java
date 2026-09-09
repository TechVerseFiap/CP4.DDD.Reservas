package br.com.fiap.reservas.application.usecase;

import br.com.fiap.reservas.application.dto.response.ProfessorResponse;
import br.com.fiap.reservas.application.exception.ResourceNotFoundException;
import br.com.fiap.reservas.application.mapper.ProfessorMapper;
import br.com.fiap.reservas.application.usecase.port.in.IGetProfessorUseCase;
import br.com.fiap.reservas.domain.repository.IProfessorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class GetProfessorService implements IGetProfessorUseCase {

    private final IProfessorRepository repository;

    public GetProfessorService(IProfessorRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional(readOnly = true)
    public ProfessorResponse execute(Long id) {
        return repository.findById(id)
                .map(ProfessorMapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Professor nao encontrado"));
    }
}
