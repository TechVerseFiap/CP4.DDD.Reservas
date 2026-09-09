package br.com.fiap.reservas.application.usecase.port.in;

import br.com.fiap.reservas.application.dto.request.ReservaRequest;
import br.com.fiap.reservas.application.dto.response.ReservaResponse;

public interface ICreateReservationUseCase {

    ReservaResponse execute(ReservaRequest request);
}
