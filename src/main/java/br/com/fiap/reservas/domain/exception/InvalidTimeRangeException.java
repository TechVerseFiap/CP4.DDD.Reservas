package br.com.fiap.reservas.domain.exception;

public class InvalidTimeRangeException extends DomainException {

    public InvalidTimeRangeException(String message) {
        super("invalid-time-range", message);
    }
}
