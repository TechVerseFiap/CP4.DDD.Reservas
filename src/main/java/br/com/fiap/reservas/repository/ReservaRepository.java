package br.com.fiap.reservas.repository;

import br.com.fiap.reservas.model.Reserva;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ReservaRepository {
    Reserva save(Reserva reserva);
    Optional<Reserva> findById(Long id);
    List<Reserva> findAll();

    boolean existeConflitoEquipamento(Long equipamentoId, LocalDateTime retirada, LocalDateTime entrega);
    boolean existeConflitoSala(Long salaId, LocalDateTime retirada, LocalDateTime entrega);
}
