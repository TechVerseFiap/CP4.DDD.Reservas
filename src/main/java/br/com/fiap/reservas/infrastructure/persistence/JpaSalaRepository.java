package br.com.fiap.reservas.infrastructure.persistence;

import br.com.fiap.reservas.model.Sala;
import br.com.fiap.reservas.repository.SalaRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaSalaRepository extends JpaRepository<Sala, Long>, SalaRepository {
}
