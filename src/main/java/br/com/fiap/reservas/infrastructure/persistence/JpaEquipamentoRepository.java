package br.com.fiap.reservas.infrastructure.persistence;
import br.com.fiap.reservas.domain.model.Equipamento;
import br.com.fiap.reservas.domain.repository.EquipamentoRepository;
import org.springframework.data.jpa.repository.JpaRepository;
public interface JpaEquipamentoRepository extends JpaRepository<Equipamento,Long>, EquipamentoRepository {}
