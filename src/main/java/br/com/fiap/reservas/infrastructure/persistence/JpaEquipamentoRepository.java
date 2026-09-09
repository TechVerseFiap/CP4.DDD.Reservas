package br.com.fiap.reservas.infrastructure.persistence;

import br.com.fiap.reservas.model.Equipamento;
import br.com.fiap.reservas.repository.EquipamentoRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaEquipamentoRepository extends JpaRepository<Equipamento, Long>, EquipamentoRepository {
}
