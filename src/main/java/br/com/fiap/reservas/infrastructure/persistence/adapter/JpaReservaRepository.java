package br.com.fiap.reservas.infrastructure.persistence.adapter;

import br.com.fiap.reservas.domain.model.Curso;
import br.com.fiap.reservas.domain.model.Equipamento;
import br.com.fiap.reservas.domain.model.Professor;
import br.com.fiap.reservas.domain.model.Reserva;
import br.com.fiap.reservas.domain.model.ReservedEquipment;
import br.com.fiap.reservas.domain.model.Sala;
import br.com.fiap.reservas.domain.model.TimeWindow;
import br.com.fiap.reservas.domain.repository.IReservaRepository;
import br.com.fiap.reservas.application.usecase.port.out.IReservationAvailabilityPort;
import br.com.fiap.reservas.infrastructure.persistence.entity.EquipamentoJpaEntity;
import br.com.fiap.reservas.infrastructure.persistence.entity.ProfessorJpaEntity;
import br.com.fiap.reservas.infrastructure.persistence.entity.ReservaEquipamentoJpaEntity;
import br.com.fiap.reservas.infrastructure.persistence.entity.ReservaJpaEntity;
import br.com.fiap.reservas.infrastructure.persistence.entity.SalaJpaEntity;
import br.com.fiap.reservas.infrastructure.persistence.repository.IEquipamentoJpaRepository;
import br.com.fiap.reservas.infrastructure.persistence.repository.IProfessorJpaRepository;
import br.com.fiap.reservas.infrastructure.persistence.repository.IReservaJpaRepository;
import br.com.fiap.reservas.infrastructure.persistence.repository.ISalaJpaRepository;
import br.com.fiap.reservas.infrastructure.persistence.repository.ITimeWindowProjection;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public class JpaReservaRepository implements IReservaRepository, IReservationAvailabilityPort {

    private final IReservaJpaRepository repository;
    private final IProfessorJpaRepository professorRepository;
    private final ISalaJpaRepository salaRepository;
    private final IEquipamentoJpaRepository equipamentoRepository;

    public JpaReservaRepository(
            IReservaJpaRepository repository,
            IProfessorJpaRepository professorRepository,
            ISalaJpaRepository salaRepository,
            IEquipamentoJpaRepository equipamentoRepository) {
        this.repository = repository;
        this.professorRepository = professorRepository;
        this.salaRepository = salaRepository;
        this.equipamentoRepository = equipamentoRepository;
    }

    @Override
    public Reserva save(Reserva reserva) {
        ReservaJpaEntity entity = toEntity(reserva);
        return toDomain(repository.save(entity));
    }

    @Override
    public Optional<Reserva> findById(Long id) {
        return repository.findById(id).map(this::toDomain);
    }

    @Override
    public List<Reserva> findAll() {
        return repository.findAll().stream().map(this::toDomain).toList();
    }

    @Override
    public List<TimeWindow> findEquipmentWindows(Long equipmentId, LocalDate date) {
        return repository.findEquipmentWindows(equipmentId, date.atStartOfDay(), date.plusDays(1).atStartOfDay())
                .stream().map(this::toTimeWindow).toList();
    }

    @Override
    public List<TimeWindow> findRoomWindows(Long roomId, LocalDate date) {
        return repository.findRoomWindows(roomId, date.atStartOfDay(), date.plusDays(1).atStartOfDay())
                .stream().map(this::toTimeWindow).toList();
    }

    private ReservaJpaEntity toEntity(Reserva reserva) {
        ProfessorJpaEntity professor = professorRepository.getReferenceById(reserva.getProfessor().getId());
        SalaJpaEntity sala = salaRepository.getReferenceById(reserva.getSala().getId());
        Set<ReservaEquipamentoJpaEntity> equipment = reserva.getItensEquipamento().stream()
                .map(item -> new ReservaEquipamentoJpaEntity(
                        equipamentoRepository.getReferenceById(item.equipamento().getId()),
                        item.quantidade()))
                .collect(java.util.stream.Collectors.toCollection(LinkedHashSet::new));

        if (reserva.getId() == null) {
            ReservaJpaEntity entity = new ReservaJpaEntity(
                    professor,
                    reserva.getCurso(),
                    sala,
                    reserva.getRetirada(),
                    reserva.getEntrega(),
                    reserva.getStatus());
            entity.replaceEquipment(equipment);
            return entity;
        }

        ReservaJpaEntity entity = repository.findById(reserva.getId())
                .orElseThrow(() -> new IllegalStateException("Reserva persistida nao encontrada: " + reserva.getId()));
        entity.updateStatus(reserva.getStatus());
        return entity;
    }

    private Reserva toDomain(ReservaJpaEntity reserva) {
        Professor professor = Professor.reconstituir(
                reserva.getProfessor().getId(),
                reserva.getProfessor().getNome(),
                reserva.getProfessor().getEmail());
        Sala sala = Sala.reconstituir(reserva.getSala().getId(), reserva.getSala().getNome());
        Set<ReservedEquipment> equipment = reserva.getEquipamentos().stream()
                .map(item -> new ReservedEquipment(
                        toDomain(item.getEquipamento()), item.getQuantidade()))
                .collect(java.util.stream.Collectors.toCollection(LinkedHashSet::new));
        return Reserva.reconstituir(
                reserva.getId(),
                professor,
                new Curso(reserva.getCurso()),
                sala,
                new TimeWindow(reserva.getRetirada(), reserva.getEntrega()),
                equipment,
                reserva.getStatus());
    }

    private Equipamento toDomain(EquipamentoJpaEntity equipamento) {
        return Equipamento.reconstituir(
                equipamento.getId(), equipamento.getNome(), equipamento.getTipo(), equipamento.isAtivo());
    }

    private TimeWindow toTimeWindow(ITimeWindowProjection projection) {
        return new TimeWindow(projection.getPickup(), projection.getReturnTime());
    }
}
