package br.com.fiap.reservas.domain.exception;

import java.time.Duration;
import java.time.LocalDateTime;

public class MinimumAdvanceNoticeNotMetException extends DomainException {

    private final long missingDays;

    public MinimumAdvanceNoticeNotMetException(LocalDateTime requested, LocalDateTime minimum) {
        super("minimum-advance-not-met", message(requested, minimum));
        this.missingDays = missingDaysBetween(requested, minimum);
    }

    public long missingDays() {
        return missingDays;
    }

    private static String message(LocalDateTime requested, LocalDateTime minimum) {
        long days = missingDaysBetween(requested, minimum);
        return "A reserva deve ser realizada com no minimo 7 dias de antecedencia; faltam " + days + " dia(s)";
    }

    private static long missingDaysBetween(LocalDateTime requested, LocalDateTime minimum) {
        return Math.max(1, (long) Math.ceil(Duration.between(requested, minimum).toSeconds() / (24d * 60d * 60d)));
    }
}
