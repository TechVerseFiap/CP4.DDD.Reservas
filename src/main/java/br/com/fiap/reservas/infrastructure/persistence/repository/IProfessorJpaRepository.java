package br.com.fiap.reservas.infrastructure.persistence.repository;

import br.com.fiap.reservas.infrastructure.persistence.entity.ProfessorJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IProfessorJpaRepository extends JpaRepository<ProfessorJpaEntity, Long> {
}
