package br.com.fiap.reservas.domain.repository;

import br.com.fiap.reservas.domain.model.Equipamento;

import java.util.List;
import java.util.Optional;

public interface IEquipamentoRepository {

    Equipamento save(Equipamento equipamento);

    Optional<Equipamento> findById(Long id);

    List<Equipamento> findAll();
}
