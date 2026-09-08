package br.com.fiap.reservas.domain.repository;
import br.com.fiap.reservas.domain.model.Sala;
import java.util.*;
public interface SalaRepository { Sala save(Sala s); Optional<Sala> findById(Long id); List<Sala> findAll(); }
