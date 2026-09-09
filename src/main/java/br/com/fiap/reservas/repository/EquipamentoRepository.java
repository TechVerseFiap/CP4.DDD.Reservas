package br.com.fiap.reservas.repository;

import br.com.fiap.reservas.model.Equipamento;
import java.util.List;
import java.util.Optional;

public interface EquipamentoRepository {
    Equipamento save(Equipamento equipamento);
    Optional<Equipamento> findById(Long id);
    List<Equipamento> findAll();
}
