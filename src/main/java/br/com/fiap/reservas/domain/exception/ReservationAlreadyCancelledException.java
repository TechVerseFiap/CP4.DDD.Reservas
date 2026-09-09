package br.com.fiap.reservas.domain.exception;

public class ReservationAlreadyCancelledException extends DomainException {

    public ReservationAlreadyCancelledException() {
        super("reservation-already-cancelled", "A reserva ja esta cancelada");
    }
}
