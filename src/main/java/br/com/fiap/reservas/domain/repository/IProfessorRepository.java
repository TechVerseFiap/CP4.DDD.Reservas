package br.com.fiap.reservas.domain.repository;

import br.com.fiap.reservas.domain.model.Professor;

import java.util.List;
import java.util.Optional;

public interface IProfessorRepository {

    Professor save(Professor professor);

    Optional<Professor> findById(Long id);

    List<Professor> findAll();
}
