package br.com.fiap.reservas.domain.repository;

import br.com.fiap.reservas.domain.model.Sala;

import java.util.List;
import java.util.Optional;

public interface ISalaRepository {

    Sala save(Sala sala);

    Optional<Sala> findById(Long id);

    List<Sala> findAll();
}
