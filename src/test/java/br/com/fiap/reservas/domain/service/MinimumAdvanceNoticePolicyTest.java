package br.com.fiap.reservas.domain.service;

import br.com.fiap.reservas.domain.exception.MinimumAdvanceNoticeNotMetException;
import br.com.fiap.reservas.domain.model.TimeWindow;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class MinimumAdvanceNoticePolicyTest {

    private final MinimumAdvanceNoticePolicy policy = new MinimumAdvanceNoticePolicy();
    private final Clock clock = Clock.fixed(Instant.parse("2026-09-01T12:00:00Z"), ZoneOffset.UTC);

    @Test
    void acceptsExactlySevenDaysInAdvance() {
        LocalDateTime start = LocalDateTime.of(2026, 9, 8, 12, 0);
        assertDoesNotThrow(() -> policy.ensureSatisfied(window(start), clock));
    }

    @Test
    void rejectsReservationBeforeSevenDays() {
        LocalDateTime start = LocalDateTime.of(2026, 9, 8, 11, 59);
        assertThrows(MinimumAdvanceNoticeNotMetException.class,
                () -> policy.ensureSatisfied(window(start), clock));
    }

    private TimeWindow window(LocalDateTime start) {
        return new TimeWindow(start, start.plusHours(2));
    }
}
