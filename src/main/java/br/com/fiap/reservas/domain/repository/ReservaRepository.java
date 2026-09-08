package br.com.fiap.reservas.domain.repository;
import br.com.fiap.reservas.domain.model.Reserva;
import java.time.LocalDateTime;
import java.util.*;
public interface ReservaRepository {
    Reserva save(Reserva r); Optional<Reserva> findById(Long id); List<Reserva> findAll();
    boolean existeConflitoEquipamento(Long equipamentoId, LocalDateTime retirada, LocalDateTime entrega);
    boolean existeConflitoSala(Long salaId, LocalDateTime retirada, LocalDateTime entrega);
}
