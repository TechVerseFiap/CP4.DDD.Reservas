package br.com.fiap.reservas.domain.service;

import br.com.fiap.reservas.domain.exception.MinimumAdvanceNoticeNotMetException;
import br.com.fiap.reservas.domain.model.TimeWindow;

import java.time.Clock;
import java.time.LocalDateTime;

public final class MinimumAdvanceNoticePolicy {

    private static final long MINIMUM_DAYS = 7;

    public void ensureSatisfied(TimeWindow requested, Clock clock) {
        LocalDateTime minimum = LocalDateTime.now(clock).plusDays(MINIMUM_DAYS);
        if (requested.pickup().isBefore(minimum)) {
            throw new MinimumAdvanceNoticeNotMetException(requested.pickup(), minimum);
        }
    }
}
