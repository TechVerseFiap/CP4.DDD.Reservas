package br.com.fiap.reservas.domain.model;

import br.com.fiap.reservas.domain.exception.InvalidTimeRangeException;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertThrows;

class InvalidTimeRangeTest {

    @Test
    void rejectsEqualTimes() {
        LocalDateTime time = LocalDateTime.of(2026, 9, 10, 18, 0);
        assertThrows(InvalidTimeRangeException.class, () -> new TimeWindow(time, time));
    }

    @Test
    void rejectsReturnBeforePickup() {
        LocalDateTime pickup = LocalDateTime.of(2026, 9, 10, 20, 0);
        assertThrows(InvalidTimeRangeException.class, () -> new TimeWindow(pickup, pickup.minusHours(1)));
    }

    @Test
    void rejectsDifferentReservationDates() {
        LocalDateTime pickup = LocalDateTime.of(2026, 9, 10, 23, 0);
        assertThrows(InvalidTimeRangeException.class,
                () -> new TimeWindow(pickup, pickup.plusHours(2)));
    }
}
