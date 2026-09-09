package br.com.fiap.reservas.application.usecase;

import br.com.fiap.reservas.application.dto.request.ReservaRequest;
import br.com.fiap.reservas.application.dto.request.EquipamentoReservaRequest;
import br.com.fiap.reservas.application.dto.response.ReservaResponse;
import br.com.fiap.reservas.application.exception.ResourceNotFoundException;
import br.com.fiap.reservas.application.mapper.ReservaMapper;
import br.com.fiap.reservas.application.usecase.port.in.ICreateReservationUseCase;
import br.com.fiap.reservas.application.usecase.port.out.IReservationAvailabilityPort;
import br.com.fiap.reservas.domain.model.Equipamento;
import br.com.fiap.reservas.domain.model.Professor;
import br.com.fiap.reservas.domain.model.Reserva;
import br.com.fiap.reservas.domain.model.ReservedEquipment;
import br.com.fiap.reservas.domain.model.Sala;
import br.com.fiap.reservas.domain.model.TimeWindow;
import br.com.fiap.reservas.domain.repository.IEquipamentoRepository;
import br.com.fiap.reservas.domain.repository.IProfessorRepository;
import br.com.fiap.reservas.domain.repository.IReservaRepository;
import br.com.fiap.reservas.domain.repository.ISalaRepository;
import br.com.fiap.reservas.domain.service.IResourceAvailabilityChecker;
import br.com.fiap.reservas.domain.service.MinimumAdvanceNoticePolicy;
import br.com.fiap.reservas.domain.service.ResourceType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Service
public class CreateReservationService implements ICreateReservationUseCase {

    private static final Logger LOGGER = LoggerFactory.getLogger(CreateReservationService.class);

    private final IReservaRepository reservaRepository;
    private final IProfessorRepository professorRepository;
    private final ISalaRepository salaRepository;
    private final IEquipamentoRepository equipamentoRepository;
    private final IReservationAvailabilityPort availabilityPort;
    private final IResourceAvailabilityChecker availabilityChecker;
    private final MinimumAdvanceNoticePolicy advanceNoticePolicy;
    private final Clock clock;

    public CreateReservationService(
            IReservaRepository reservaRepository,
            IProfessorRepository professorRepository,
            ISalaRepository salaRepository,
            IEquipamentoRepository equipamentoRepository,
            IReservationAvailabilityPort availabilityPort,
            IResourceAvailabilityChecker availabilityChecker,
            MinimumAdvanceNoticePolicy advanceNoticePolicy,
            Clock clock) {
        this.reservaRepository = reservaRepository;
        this.professorRepository = professorRepository;
        this.salaRepository = salaRepository;
        this.equipamentoRepository = equipamentoRepository;
        this.availabilityPort = availabilityPort;
        this.availabilityChecker = availabilityChecker;
        this.advanceNoticePolicy = advanceNoticePolicy;
        this.clock = clock;
    }

    @Override
    @Transactional
    public ReservaResponse execute(ReservaRequest request) {
        TimeWindow requestedWindow = new TimeWindow(request.retirada(), request.entrega());
        advanceNoticePolicy.ensureSatisfied(requestedWindow, clock);

        Professor professor = professorRepository.findById(request.professorId())
                .orElseThrow(() -> new ResourceNotFoundException("Professor nao encontrado"));
        Sala sala = salaRepository.findById(request.salaId())
                .orElseThrow(() -> new ResourceNotFoundException("Sala nao encontrada"));
        Set<Equipamento> equipamentos = findEquipments(request.equipmentIds());

        Set<ReservedEquipment> reservedEquipment = equipamentos.stream()
                .map(equipment -> new ReservedEquipment(
                        equipment,
                        quantityFor(request, equipment.getId())))
                .collect(java.util.stream.Collectors.toCollection(java.util.LinkedHashSet::new));
        Reserva reserva = ReservaMapper.toDomainWithQuantities(request, professor, sala, reservedEquipment);

        equipamentos.forEach(equipamento -> availabilityChecker.ensureAvailable(
                ResourceType.EQUIPMENT,
                equipamento.getNome(),
                requestedWindow,
                availabilityPort.findEquipmentWindows(equipamento.getId(), requestedWindow.date())));
        availabilityChecker.ensureAvailable(
                ResourceType.ROOM,
                sala.getNome(),
                requestedWindow,
                availabilityPort.findRoomWindows(sala.getId(), requestedWindow.date()));

        Reserva saved = reservaRepository.save(reserva);
        LOGGER.info("reservation created id={} professorId={} roomId={}",
                saved.getId(), professor.getId(), sala.getId());
        return ReservaMapper.toResponse(saved);
    }

    private Set<Equipamento> findEquipments(Set<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return Set.of();
        }
        Set<Equipamento> equipamentos = new LinkedHashSet<>();
        for (Long id : ids) {
            equipamentos.add(equipamentoRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Equipamento nao encontrado: " + id)));
        }
        return equipamentos;
    }

    private int quantityFor(ReservaRequest request, Long equipmentId) {
        if (request.equipamentos() == null || request.equipamentos().isEmpty()) {
            return 1;
        }
        return request.equipamentos().stream()
                .filter(item -> equipmentId.equals(item.equipamentoId()))
                .map(EquipamentoReservaRequest::quantidade)
                .findFirst()
                .orElse(1);
    }
}
