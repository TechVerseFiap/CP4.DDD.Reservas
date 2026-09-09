package br.com.fiap.reservas.domain.model;

import br.com.fiap.reservas.domain.exception.InvalidTimeRangeException;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record TimeWindow(LocalDateTime pickup, LocalDateTime returnTime) {

    public TimeWindow {
        if (pickup == null || returnTime == null) {
            throw new InvalidTimeRangeException("Horarios de retirada e entrega sao obrigatorios");
        }
        if (!pickup.isBefore(returnTime)) {
            throw new InvalidTimeRangeException("Horario de retirada deve ser anterior ao horario de entrega");
        }
        if (!pickup.toLocalDate().equals(returnTime.toLocalDate())) {
            throw new InvalidTimeRangeException("A retirada e a entrega devem ocorrer na mesma data");
        }
    }

    public LocalDate date() {
        return pickup.toLocalDate();
    }

    public boolean overlaps(TimeWindow other) {
        return pickup.isBefore(other.returnTime) && returnTime.isAfter(other.pickup);
    }
}
