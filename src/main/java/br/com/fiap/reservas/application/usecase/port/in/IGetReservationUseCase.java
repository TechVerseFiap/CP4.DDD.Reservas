package br.com.fiap.reservas.application.usecase.port.in;

import br.com.fiap.reservas.application.dto.response.ReservaResponse;

public interface IGetReservationUseCase {

    ReservaResponse execute(Long id);
}
