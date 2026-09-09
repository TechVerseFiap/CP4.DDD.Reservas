package br.com.fiap.reservas.application.usecase;

import br.com.fiap.reservas.application.dto.response.ProfessorResponse;
import br.com.fiap.reservas.application.mapper.ProfessorMapper;
import br.com.fiap.reservas.application.usecase.port.in.IListProfessorsUseCase;
import br.com.fiap.reservas.domain.repository.IProfessorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ListProfessorsService implements IListProfessorsUseCase {

    private final IProfessorRepository repository;

    public ListProfessorsService(IProfessorRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProfessorResponse> execute() {
        return repository.findAll().stream().map(ProfessorMapper::toResponse).toList();
    }
}
