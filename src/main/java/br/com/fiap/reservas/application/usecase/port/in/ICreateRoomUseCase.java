package br.com.fiap.reservas.application.usecase.port.in;

import br.com.fiap.reservas.application.dto.request.SalaRequest;
import br.com.fiap.reservas.application.dto.response.SalaResponse;

public interface ICreateRoomUseCase {

    SalaResponse execute(SalaRequest request);
}
