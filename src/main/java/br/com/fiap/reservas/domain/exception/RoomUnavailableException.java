package br.com.fiap.reservas.domain.exception;

public class RoomUnavailableException extends DomainException {

    public RoomUnavailableException(String roomName) {
        super("room-unavailable", "A sala " + roomName
                + " ja possui reserva no periodo solicitado");
    }
}
