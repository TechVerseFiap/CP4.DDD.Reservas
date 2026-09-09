package br.com.fiap.reservas.application.usecase.port.in;

import br.com.fiap.reservas.application.dto.response.EquipamentoResponse;

public interface IChangeEquipmentStatusUseCase {

    EquipamentoResponse execute(Long id, boolean active);
}
