package br.com.fiap.reservas.domain.repository;

import br.com.fiap.reservas.domain.model.Reserva;
import br.com.fiap.reservas.domain.model.TimeWindow;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface IReservaRepository {

    Reserva save(Reserva reserva);

    Optional<Reserva> findById(Long id);

    List<Reserva> findAll();

    List<TimeWindow> findEquipmentWindows(Long equipmentId, LocalDate date);

    List<TimeWindow> findRoomWindows(Long roomId, LocalDate date);
}
