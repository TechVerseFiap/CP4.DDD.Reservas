package br.com.fiap.reservas.service;

import br.com.fiap.reservas.exception.RegraNegocioException;
import br.com.fiap.reservas.model.*;
import br.com.fiap.reservas.repository.*;
import br.com.fiap.reservas.view.request.ReservaRequest;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import java.time.*;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
class ReservaServiceImplTest {

    @Mock ReservaRepository reservaRepository;
    @Mock ProfessorRepository professorRepository;
    @Mock SalaRepository salaRepository;
    @Mock EquipamentoRepository equipamentoRepository;

    private ReservaServiceImpl service;
    private Professor professor;
    private Sala sala;
    private Equipamento equipamento;
    private LocalDateTime inicio;
    private LocalDateTime fim;

    @BeforeEach
    void setup() {
        Clock clock = Clock.fixed(
                Instant.parse("2026-09-01T12:00:00Z"),
                ZoneOffset.UTC);

        service = new ReservaServiceImpl(
                reservaRepository,
                professorRepository,
                salaRepository,
                equipamentoRepository,
                clock);

        professor = mock(Professor.class);
        sala = mock(Sala.class);
        equipamento = mock(Equipamento.class);

        when(professorRepository.findById(1L)).thenReturn(Optional.of(professor));
        when(salaRepository.findById(1L)).thenReturn(Optional.of(sala));
        when(equipamentoRepository.findById(1L)).thenReturn(Optional.of(equipamento));

        when(professor.getId()).thenReturn(1L);
        when(professor.getNome()).thenReturn("João");
        when(sala.getId()).thenReturn(1L);
        when(sala.getNome()).thenReturn("204");
        when(equipamento.getId()).thenReturn(1L);
        when(equipamento.getNome()).thenReturn("Datashow 01");
        when(equipamento.getTipo()).thenReturn("Datashow");
        when(equipamento.isAtivo()).thenReturn(true);

        inicio = LocalDateTime.of(2026, 9, 10, 18, 30);
        fim = inicio.plusHours(2);

        when(reservaRepository.existeConflitoEquipamento(1L, inicio, fim)).thenReturn(false);
        when(reservaRepository.existeConflitoSala(1L, inicio, fim)).thenReturn(false);
        when(reservaRepository.save(any(Reserva.class))).thenAnswer(invocation -> invocation.getArgument(0));
    }

    private ReservaRequest request() {
        return new ReservaRequest(1L, "Engenharia de Software", 1L, inicio, fim, Set.of(1L));
    }

    @Test
    void deveCriarReservaValida() {
        var response = service.criar(request());

        assertEquals("Engenharia de Software", response.curso());
        assertEquals(1, response.equipamentos().size());
        verify(reservaRepository).save(any(Reserva.class));
    }

    @Test
    void deveRejeitarReservaComMenosDeUmaSemana() {
        var retirada = LocalDateTime.of(2026, 9, 5, 18, 30);
        var request = new ReservaRequest(
                1L, "Engenharia", 1L, retirada, retirada.plusHours(2), Set.of(1L));

        var exception = assertThrows(RegraNegocioException.class, () -> service.criar(request));

        assertTrue(exception.getMessage().contains("uma semana"));
        verifyNoInteractions(professorRepository, salaRepository, equipamentoRepository);
        verifyNoInteractions(reservaRepository);
    }

    @Test
    void deveRejeitarEquipamentoInativo() {
        when(equipamento.isAtivo()).thenReturn(false);

        var exception = assertThrows(
                RegraNegocioException.class,
                () -> service.criar(request()));

        assertTrue(exception.getMessage().contains("inativo"));
        verify(reservaRepository, never()).save(any());
    }

    @Test
    void deveRejeitarConflitoDeEquipamento() {
        when(reservaRepository.existeConflitoEquipamento(1L, inicio, fim)).thenReturn(true);

        var exception = assertThrows(
                RegraNegocioException.class,
                () -> service.criar(request()));

        assertTrue(exception.getMessage().contains("já está reservado"));
        verify(reservaRepository, never()).save(any());
    }

    @Test
    void deveRejeitarConflitoDeSala() {
        when(reservaRepository.existeConflitoSala(1L, inicio, fim)).thenReturn(true);

        var exception = assertThrows(
                RegraNegocioException.class,
                () -> service.criar(request()));

        assertTrue(exception.getMessage().contains("sala"));
        verify(reservaRepository, never()).save(any());
    }

    @Test
    void entidadeDeveRejeitarHorarioInvalido() {
        assertThrows(
                IllegalArgumentException.class,
                () -> Reserva.criar(
                        professor,
                        "Engenharia",
                        sala,
                        fim,
                        inicio,
                        Set.of(equipamento)));
    }
}
