package br.com.fiap.reservas.domain.model;

import br.com.fiap.reservas.domain.exception.InactiveEquipmentException;
import br.com.fiap.reservas.domain.exception.MissingReservationDataException;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertThrows;

class ReservationEquipmentRuleTest {

    private final Professor professor = Professor.reconstituir(1L, "Professor", "professor@example.com");
    private final Sala sala = Sala.reconstituir(1L, "204");
    private final TimeWindow window = new TimeWindow(
            LocalDateTime.of(2026, 9, 10, 18, 0),
            LocalDateTime.of(2026, 9, 10, 20, 0));

    @Test
    void rejectsInactiveEquipmentAndNamesIt() {
        Equipamento inactive = Equipamento.reconstituir(3L, "Microfone 02", "Microfone", false);
        assertThrows(InactiveEquipmentException.class, () -> Reserva.criar(
                professor, new Curso("Engenharia"), sala, window,
                Set.of(new ReservedEquipment(inactive, 1))));
    }

    @Test
    void rejectsEmptyEquipmentCollection() {
        assertThrows(MissingReservationDataException.class, () -> Reserva.criar(
                professor, new Curso("Engenharia"), sala, window, Set.of()));
    }

    @Test
    void rejectsMissingProfessor() {
        Equipamento equipment = Equipamento.reconstituir(1L, "Datashow", "Datashow", true);
        assertThrows(MissingReservationDataException.class, () -> Reserva.criar(
                null, new Curso("Engenharia"), sala, window,
                Set.of(new ReservedEquipment(equipment, 1))));
    }

    @Test
    void retainsRequestedEquipmentQuantity() {
        Equipamento equipment = Equipamento.reconstituir(1L, "Datashow", "Datashow", true);
        Reserva reservation = Reserva.criar(
                professor, new Curso("Engenharia"), sala, window,
                Set.of(new ReservedEquipment(equipment, 3)));
        org.junit.jupiter.api.Assertions.assertEquals(
                3, reservation.getItensEquipamento().iterator().next().quantidade());
    }
}
