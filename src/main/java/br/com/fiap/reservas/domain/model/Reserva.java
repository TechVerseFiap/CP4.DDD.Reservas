package br.com.fiap.reservas.domain.model;

import br.com.fiap.reservas.domain.exception.InactiveEquipmentException;
import br.com.fiap.reservas.domain.exception.MissingReservationDataException;
import br.com.fiap.reservas.domain.exception.ReservationAlreadyCancelledException;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public final class Reserva {

    private final Long id;
    private final Professor professor;
    private final Curso curso;
    private final Sala sala;
    private final TimeWindow timeWindow;
    private final Set<ReservedEquipment> itensEquipamento;
    private ReservaStatus status;

    private Reserva(
            Long id,
            Professor professor,
            Curso curso,
            Sala sala,
            TimeWindow timeWindow,
            Collection<ReservedEquipment> itensEquipamento,
            ReservaStatus status
    ) {
        if (professor == null) {
            throw new MissingReservationDataException("Professor e obrigatorio");
        }
        if (curso == null) {
            throw new MissingReservationDataException("Curso e obrigatorio");
        }
        if (sala == null) {
            throw new MissingReservationDataException("Sala e obrigatoria");
        }
        if (timeWindow == null) {
            throw new MissingReservationDataException("Data e horarios da reserva sao obrigatorios");
        }
        if (itensEquipamento == null || itensEquipamento.isEmpty()) {
            throw new MissingReservationDataException("Ao menos um equipamento deve ser reservado");
        }

        List<String> inactive = itensEquipamento.stream()
                .map(ReservedEquipment::equipamento)
                .filter(equipment -> !equipment.isAtivo())
                .map(Equipamento::getNome)
                .toList();
        if (!inactive.isEmpty()) {
            throw new InactiveEquipmentException(inactive);
        }

        this.id = id;
        this.professor = professor;
        this.curso = curso;
        this.sala = sala;
        this.timeWindow = timeWindow;
        this.itensEquipamento = new LinkedHashSet<>(itensEquipamento);
        this.status = status == null ? ReservaStatus.CONFIRMADA : status;
    }

    public static Reserva criar(
            Professor professor,
            String curso,
            Sala sala,
            LocalDateTime retirada,
            LocalDateTime entrega,
            Collection<Equipamento> equipamentos
    ) {
        if (equipamentos == null) {
            return new Reserva(
                    null, professor,
                    new Curso(curso),
                    sala,
                    new TimeWindow(retirada, entrega),
                    null,
                    ReservaStatus.CONFIRMADA
            );
        }
        return criar(
                professor,
                new Curso(curso),
                sala,
                new TimeWindow(retirada, entrega),
                equipamentos.stream().map(equipment -> new ReservedEquipment(equipment, 1)).toList()
        );
    }

    public static Reserva criar(
            Professor professor,
            Curso curso,
            Sala sala,
            TimeWindow timeWindow,
            Collection<ReservedEquipment> equipamentos
    ) {
        return new Reserva(null, professor, curso, sala, timeWindow, equipamentos, ReservaStatus.CONFIRMADA);
    }

    public static Reserva reconstituir(
            Long id,
            Professor professor,
            Curso curso,
            Sala sala,
            TimeWindow timeWindow,
            Collection<ReservedEquipment> equipamentos,
            ReservaStatus status
    ) {
        return new Reserva(id, professor, curso, sala, timeWindow, equipamentos, status);
    }

    public void cancelar() {
        if (status == ReservaStatus.CANCELADA) {
            throw new ReservationAlreadyCancelledException();
        }
        status = ReservaStatus.CANCELADA;
    }

    public Long getId() {
        return id;
    }

    public Professor getProfessor() {
        return professor;
    }

    public String getCurso() {
        return curso.nome();
    }

    public Curso getCursoValue() {
        return curso;
    }

    public Sala getSala() {
        return sala;
    }

    public LocalDateTime getRetirada() {
        return timeWindow.pickup();
    }

    public LocalDateTime getEntrega() {
        return timeWindow.returnTime();
    }

    public TimeWindow getTimeWindow() {
        return timeWindow;
    }

    public Set<Equipamento> getEquipamentos() {
        return itensEquipamento.stream().map(ReservedEquipment::equipamento).collect(java.util.stream.Collectors.toUnmodifiableSet());
    }

    public Set<ReservedEquipment> getItensEquipamento() {
        return Set.copyOf(itensEquipamento);
    }

    public ReservaStatus getStatus() {
        return status;
    }
}
