package br.com.fiap.reservas.infrastructure.persistence;

import br.com.fiap.reservas.model.Professor;
import br.com.fiap.reservas.repository.ProfessorRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaProfessorRepository extends JpaRepository<Professor, Long>, ProfessorRepository {
}
