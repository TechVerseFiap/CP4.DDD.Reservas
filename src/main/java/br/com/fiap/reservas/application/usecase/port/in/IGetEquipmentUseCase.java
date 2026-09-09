package br.com.fiap.reservas.application.usecase.port.in;

import br.com.fiap.reservas.application.dto.response.EquipamentoResponse;

public interface IGetEquipmentUseCase {

    EquipamentoResponse execute(Long id);
}
