package br.com.fiap.reservas.domain.repository;
import br.com.fiap.reservas.domain.model.Equipamento;
import java.util.*;
public interface EquipamentoRepository { Equipamento save(Equipamento e); Optional<Equipamento> findById(Long id); List<Equipamento> findAll(); }
