package br.com.fiap.reservas.application.usecase;

import br.com.fiap.reservas.application.dto.request.ProfessorRequest;
import br.com.fiap.reservas.application.dto.response.ProfessorResponse;
import br.com.fiap.reservas.application.mapper.ProfessorMapper;
import br.com.fiap.reservas.application.usecase.port.in.ICreateProfessorUseCase;
import br.com.fiap.reservas.domain.repository.IProfessorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CreateProfessorService implements ICreateProfessorUseCase {

    private final IProfessorRepository repository;

    public CreateProfessorService(IProfessorRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional
    public ProfessorResponse execute(ProfessorRequest request) {
        return ProfessorMapper.toResponse(repository.save(ProfessorMapper.toDomain(request)));
    }
}
