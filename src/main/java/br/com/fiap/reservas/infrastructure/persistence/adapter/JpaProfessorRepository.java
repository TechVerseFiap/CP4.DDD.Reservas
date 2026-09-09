package br.com.fiap.reservas.infrastructure.persistence.adapter;

import br.com.fiap.reservas.domain.model.Professor;
import br.com.fiap.reservas.domain.repository.IProfessorRepository;
import br.com.fiap.reservas.infrastructure.persistence.entity.ProfessorJpaEntity;
import br.com.fiap.reservas.infrastructure.persistence.repository.IProfessorJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class JpaProfessorRepository implements IProfessorRepository {

    private final IProfessorJpaRepository repository;

    public JpaProfessorRepository(IProfessorJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    public Professor save(Professor professor) {
        ProfessorJpaEntity saved = repository.save(toEntity(professor));
        return toDomain(saved);
    }

    @Override
    public Optional<Professor> findById(Long id) {
        return repository.findById(id).map(this::toDomain);
    }

    @Override
    public List<Professor> findAll() {
        return repository.findAll().stream().map(this::toDomain).toList();
    }

    private ProfessorJpaEntity toEntity(Professor professor) {
        if (professor.getId() == null) {
            return new ProfessorJpaEntity(professor.getNome(), professor.getEmail());
        }
        return repository.findById(professor.getId())
                .orElseGet(() -> new ProfessorJpaEntity(professor.getNome(), professor.getEmail()));
    }

    private Professor toDomain(ProfessorJpaEntity professor) {
        return Professor.reconstituir(professor.getId(), professor.getNome(), professor.getEmail());
    }
}
