package br.com.fiap.reservas.application.usecase;

import br.com.fiap.reservas.application.dto.request.ReservaRequest;
import br.com.fiap.reservas.application.dto.response.ReservaResponse;
import br.com.fiap.reservas.domain.exception.EquipmentUnavailableException;
import br.com.fiap.reservas.domain.model.Equipamento;
import br.com.fiap.reservas.domain.model.Professor;
import br.com.fiap.reservas.domain.model.Reserva;
import br.com.fiap.reservas.domain.model.Sala;
import br.com.fiap.reservas.domain.model.TimeWindow;
import br.com.fiap.reservas.domain.repository.IEquipamentoRepository;
import br.com.fiap.reservas.domain.repository.IProfessorRepository;
import br.com.fiap.reservas.domain.repository.IReservaRepository;
import br.com.fiap.reservas.domain.repository.ISalaRepository;
import br.com.fiap.reservas.domain.service.IResourceAvailabilityChecker;
import br.com.fiap.reservas.domain.service.MinimumAdvanceNoticePolicy;
import br.com.fiap.reservas.application.usecase.port.out.IReservationAvailabilityPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreateReservationServiceTest {

    @Mock IReservaRepository reservationRepository;
    @Mock IProfessorRepository professorRepository;
    @Mock ISalaRepository roomRepository;
    @Mock IEquipamentoRepository equipmentRepository;
    @Mock IReservationAvailabilityPort availabilityPort;
    @Mock IResourceAvailabilityChecker availabilityChecker;

    private CreateReservationService service;
    private final Professor professor = Professor.reconstituir(1L, "Professor", "professor@example.com");
    private final Sala room = Sala.reconstituir(1L, "204");
    private final Equipamento equipment = Equipamento.reconstituir(1L, "Datashow 01", "Datashow", true);

    @BeforeEach
    void setUp() {
        service = new CreateReservationService(
                reservationRepository,
                professorRepository,
                roomRepository,
                equipmentRepository,
                availabilityPort,
                availabilityChecker,
                new MinimumAdvanceNoticePolicy(),
                Clock.fixed(Instant.parse("2026-09-01T12:00:00Z"), ZoneOffset.UTC));
        when(professorRepository.findById(1L)).thenReturn(Optional.of(professor));
        when(roomRepository.findById(1L)).thenReturn(Optional.of(room));
        when(equipmentRepository.findById(1L)).thenReturn(Optional.of(equipment));
        org.mockito.Mockito.lenient().when(
                availabilityPort.findEquipmentWindows(1L, LocalDate.of(2026, 9, 10))).thenReturn(List.of());
        org.mockito.Mockito.lenient().when(
                availabilityPort.findRoomWindows(1L, LocalDate.of(2026, 9, 10))).thenReturn(List.of());
        org.mockito.Mockito.lenient().when(reservationRepository.save(any(Reserva.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));
    }

    @Test
    void createsReservationThroughRepositoryPorts() {
        ReservaRequest request = request(LocalDateTime.of(2026, 9, 10, 18, 0));
        ReservaResponse response = service.execute(request);
        org.junit.jupiter.api.Assertions.assertEquals("Engenharia", response.curso());
    }

    @Test
    void propagatesEquipmentConflictFromDomainChecker() {
        org.mockito.Mockito.doThrow(new EquipmentUnavailableException("Datashow 01"))
                .when(availabilityChecker).ensureAvailable(any(), any(), any(TimeWindow.class), any());
        assertThrows(EquipmentUnavailableException.class,
                () -> service.execute(request(LocalDateTime.of(2026, 9, 10, 18, 0))));
    }

    private ReservaRequest request(LocalDateTime start) {
        return new ReservaRequest(1L, "Engenharia", 1L, start, start.plusHours(2), Set.of(1L));
    }
}
