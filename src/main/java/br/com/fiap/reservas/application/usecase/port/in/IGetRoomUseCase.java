package br.com.fiap.reservas.application.usecase.port.in;

import br.com.fiap.reservas.application.dto.response.SalaResponse;

public interface IGetRoomUseCase {

    SalaResponse execute(Long id);
}
