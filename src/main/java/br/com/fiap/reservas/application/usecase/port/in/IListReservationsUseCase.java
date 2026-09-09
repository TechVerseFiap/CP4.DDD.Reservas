package br.com.fiap.reservas.application.usecase.port.in;

import br.com.fiap.reservas.application.dto.response.ReservaResponse;

import java.util.List;

public interface IListReservationsUseCase {

    List<ReservaResponse> execute();
}
