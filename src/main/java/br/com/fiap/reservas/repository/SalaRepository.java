package br.com.fiap.reservas.repository;

import br.com.fiap.reservas.model.Sala;
import java.util.List;
import java.util.Optional;

public interface SalaRepository {
    Sala save(Sala sala);
    Optional<Sala> findById(Long id);
    List<Sala> findAll();
}
