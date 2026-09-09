package br.com.fiap.reservas.repository;

import br.com.fiap.reservas.model.Professor;
import java.util.List;
import java.util.Optional;

public interface ProfessorRepository {
    Professor save(Professor professor);
    Optional<Professor> findById(Long id);
    List<Professor> findAll();
}
