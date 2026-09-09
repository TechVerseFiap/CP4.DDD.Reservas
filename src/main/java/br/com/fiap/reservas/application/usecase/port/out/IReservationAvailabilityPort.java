package br.com.fiap.reservas.application.usecase.port.out;

import br.com.fiap.reservas.domain.model.TimeWindow;

import java.time.LocalDate;
import java.util.List;

public interface IReservationAvailabilityPort {

    List<TimeWindow> findEquipmentWindows(Long equipmentId, LocalDate date);

    List<TimeWindow> findRoomWindows(Long roomId, LocalDate date);
}
