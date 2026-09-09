package br.com.fiap.reservas.application.usecase.port.in;

import br.com.fiap.reservas.application.dto.response.EquipamentoResponse;

import java.util.List;

public interface IListEquipmentsUseCase {

    List<EquipamentoResponse> execute();
}
