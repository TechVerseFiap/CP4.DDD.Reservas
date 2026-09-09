package br.com.fiap.reservas.application.mapper;

import br.com.fiap.reservas.application.dto.request.ReservaRequest;
import br.com.fiap.reservas.application.dto.response.EquipamentoResponse;
import br.com.fiap.reservas.application.dto.response.ReservaResponse;
import br.com.fiap.reservas.domain.model.Equipamento;
import br.com.fiap.reservas.domain.model.Reserva;
import br.com.fiap.reservas.domain.model.ReservedEquipment;
import br.com.fiap.reservas.domain.model.Professor;
import br.com.fiap.reservas.domain.model.Sala;
import br.com.fiap.reservas.domain.model.TimeWindow;

import java.util.Collection;

public final class ReservaMapper {

    private ReservaMapper() {
    }

    public static Reserva toDomain(
            ReservaRequest request,
            Professor professor,
            Sala sala,
            Collection<Equipamento> equipamentos) {
        return Reserva.criar(
                professor,
                request.curso(),
                sala,
                request.retirada(),
                request.entrega(),
                equipamentos);
    }

    public static Reserva toDomainWithQuantities(
            ReservaRequest request,
            Professor professor,
            Sala sala,
            Collection<ReservedEquipment> equipamentos) {
        return Reserva.criar(
                professor,
                new br.com.fiap.reservas.domain.model.Curso(request.curso()),
                sala,
                new TimeWindow(request.retirada(), request.entrega()),
                equipamentos);
    }

    public static ReservaResponse toResponse(Reserva reserva) {
        return new ReservaResponse(
                reserva.getId(),
                reserva.getProfessor().getId(),
                reserva.getProfessor().getNome(),
                reserva.getCurso(),
                reserva.getSala().getId(),
                reserva.getSala().getNome(),
                reserva.getRetirada(),
                reserva.getEntrega(),
                reserva.getItensEquipamento().stream()
                        .map(item -> new EquipamentoResponse(
                                item.equipamento().getId(),
                                item.equipamento().getNome(),
                                item.equipamento().getTipo(),
                                item.equipamento().isAtivo()))
                        .toList(),
                reserva.getStatus());
    }
}
