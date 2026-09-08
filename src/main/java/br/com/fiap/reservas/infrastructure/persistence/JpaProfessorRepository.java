package br.com.fiap.reservas.infrastructure.persistence;
import br.com.fiap.reservas.domain.model.Professor;
import br.com.fiap.reservas.domain.repository.ProfessorRepository;
import org.springframework.data.jpa.repository.JpaRepository;
public interface JpaProfessorRepository extends JpaRepository<Professor,Long>, ProfessorRepository {}
