package br.com.fiap.reservas.domain.exception;

public class MissingReservationDataException extends DomainException {

    public MissingReservationDataException(String message) {
        super("missing-reservation-data", message);
    }
}
