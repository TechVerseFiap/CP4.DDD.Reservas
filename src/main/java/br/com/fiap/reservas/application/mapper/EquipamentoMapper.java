package br.com.fiap.reservas.application.mapper;

import br.com.fiap.reservas.application.dto.request.EquipamentoRequest;
import br.com.fiap.reservas.application.dto.response.EquipamentoResponse;
import br.com.fiap.reservas.domain.model.Equipamento;

public final class EquipamentoMapper {

    private EquipamentoMapper() {
    }

    public static Equipamento toDomain(EquipamentoRequest request) {
        return Equipamento.criar(
                request.nome(),
                request.tipo(),
                request.ativo() == null || request.ativo()
        );
    }

    public static EquipamentoResponse toResponse(Equipamento equipamento) {
        return new EquipamentoResponse(
            equipamento.getId(),
            equipamento.getNome(),
            equipamento.getTipo(),
            equipamento.isAtivo()
        );
    }
}
