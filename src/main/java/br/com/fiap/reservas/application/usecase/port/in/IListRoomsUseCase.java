package br.com.fiap.reservas.application.usecase.port.in;

import br.com.fiap.reservas.application.dto.response.SalaResponse;

import java.util.List;

public interface IListRoomsUseCase {

    List<SalaResponse> execute();
}
