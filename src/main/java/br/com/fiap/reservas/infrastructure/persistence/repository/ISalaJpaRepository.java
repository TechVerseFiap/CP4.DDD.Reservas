package br.com.fiap.reservas.infrastructure.persistence.repository;

import br.com.fiap.reservas.infrastructure.persistence.entity.SalaJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ISalaJpaRepository extends JpaRepository<SalaJpaEntity, Long> {
}
