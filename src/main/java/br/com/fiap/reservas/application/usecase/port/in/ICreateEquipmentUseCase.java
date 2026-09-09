package br.com.fiap.reservas.application.usecase.port.in;

import br.com.fiap.reservas.application.dto.request.EquipamentoRequest;
import br.com.fiap.reservas.application.dto.response.EquipamentoResponse;

public interface ICreateEquipmentUseCase {
    EquipamentoResponse execute(EquipamentoRequest request);
}
