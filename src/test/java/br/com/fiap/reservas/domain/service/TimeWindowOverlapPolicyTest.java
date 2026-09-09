package br.com.fiap.reservas.domain.service;

import br.com.fiap.reservas.domain.exception.EquipmentUnavailableException;
import br.com.fiap.reservas.domain.exception.RoomUnavailableException;
import br.com.fiap.reservas.domain.model.TimeWindow;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TimeWindowOverlapPolicyTest {

    private final IResourceAvailabilityChecker checker = new ResourceAvailabilityChecker();
    private final TimeWindow existing = window(18, 20);

    @Test
    void rejectsOverlappingEquipmentWindow() {
        assertThrows(EquipmentUnavailableException.class, () -> checker.ensureAvailable(
                ResourceType.EQUIPMENT, "Datashow 01", window(19, 22), List.of(existing)));
    }

    @Test
    void rejectsOverlappingRoomWindow() {
        assertThrows(RoomUnavailableException.class, () -> checker.ensureAvailable(
                ResourceType.ROOM, "204", window(19, 22), List.of(existing)));
    }

    @Test
    void allowsBackToBackWindows() {
        assertDoesNotThrow(() -> checker.ensureAvailable(
                ResourceType.EQUIPMENT, "Datashow 01", window(20, 22), List.of(existing)));
    }

    private TimeWindow window(int start, int end) {
        LocalDateTime date = LocalDateTime.of(2026, 9, 10, start, 0);
        return new TimeWindow(date, date.withHour(end));
    }
}
