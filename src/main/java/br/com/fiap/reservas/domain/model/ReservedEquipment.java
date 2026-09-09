package br.com.fiap.reservas.domain.model;

import br.com.fiap.reservas.domain.exception.MissingReservationDataException;

public record ReservedEquipment(Equipamento equipamento, int quantidade) {

    public ReservedEquipment {
        if (equipamento == null) {
            throw new MissingReservationDataException("Equipamento e obrigatorio");
        }
        if (quantidade <= 0) {
            throw new MissingReservationDataException("A quantidade do equipamento deve ser positiva");
        }
    }
}
