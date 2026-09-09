package br.com.fiap.reservas.domain.service;

import br.com.fiap.reservas.domain.model.TimeWindow;

public final class TimeWindowOverlapPolicy {

    private TimeWindowOverlapPolicy() {
    }

    public static boolean overlaps(TimeWindow first, TimeWindow second) {
        return first.overlaps(second);
    }
}
