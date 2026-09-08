package br.com.fiap.reservas.application;

import br.com.fiap.reservas.application.dto.ReservaRequest;
import br.com.fiap.reservas.application.exception.RegraNegocioException;
import br.com.fiap.reservas.domain.model.*;
import br.com.fiap.reservas.domain.repository.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import java.time.LocalDateTime;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
class ReservaApplicationServiceTest {
    @Mock ReservaRepository reservaRepository;
    @Mock ProfessorRepository professorRepository;
    @Mock SalaRepository salaRepository;
    @Mock EquipamentoRepository equipamentoRepository;
    @InjectMocks ReservaApplicationService service;

    private Professor professor;
    private Sala sala;
    private Equipamento ativo;
    private LocalDateTime inicio;
    private LocalDateTime fim;

    @BeforeEach void setup() {
        professor = Professor.builder().id(1L).nome("João").email("joao@fiap.com.br").build();
        sala = Sala.builder().id(1L).nome("204").build();
        ativo = Equipamento.builder().id(1L).nome("Datashow 01").tipo("Datashow").ativo(true).build();
        inicio = LocalDateTime.now().plusDays(8).withHour(18).withMinute(30).withSecond(0).withNano(0);
        fim = inicio.plusHours(2);
        when(professorRepository.findById(1L)).thenReturn(Optional.of(professor));
        when(salaRepository.findById(1L)).thenReturn(Optional.of(sala));
        when(equipamentoRepository.findById(1L)).thenReturn(Optional.of(ativo));
        when(reservaRepository.existeConflitoEquipamento(anyLong(), any(), any())).thenReturn(false);
        when(reservaRepository.existeConflitoSala(anyLong(), any(), any())).thenReturn(false);
        when(reservaRepository.save(any())).thenAnswer(i -> i.getArgument(0));
    }

    private ReservaRequest request() {
        return new ReservaRequest(1L, "Engenharia de Software", 1L, inicio, fim, Set.of(1L));
    }

    @Test void deveCriarReservaValida() {
        Reserva reserva = service.criar(request());
        assertEquals("Engenharia de Software", reserva.getCurso());
        assertEquals(1, reserva.getEquipamentos().size());
        verify(reservaRepository).save(any(Reserva.class));
    }

    @Test void deveRejeitarReservaComMenosDeUmaSemana() {
        var r = new ReservaRequest(1L, "Engenharia", 1L, LocalDateTime.now().plusDays(2), LocalDateTime.now().plusDays(2).plusHours(2), Set.of(1L));
        var ex = assertThrows(RegraNegocioException.class, () -> service.criar(r));
        assertTrue(ex.getMessage().contains("uma semana"));
        verifyNoInteractions(reservaRepository);
    }

    @Test void deveRejeitarEquipamentoInativo() {
        ativo.setAtivo(false);
        var ex = assertThrows(RegraNegocioException.class, () -> service.criar(request()));
        assertTrue(ex.getMessage().contains("inativo"));
        verify(reservaRepository, never()).save(any());
    }

    @Test void deveRejeitarConflitoDeEquipamento() {
        when(reservaRepository.existeConflitoEquipamento(1L, inicio, fim)).thenReturn(true);
        var ex = assertThrows(RegraNegocioException.class, () -> service.criar(request()));
        assertTrue(ex.getMessage().contains("já está reservado"));
        verify(reservaRepository, never()).save(any());
    }

    @Test void deveRejeitarConflitoDeSala() {
        when(reservaRepository.existeConflitoSala(1L, inicio, fim)).thenReturn(true);
        var ex = assertThrows(RegraNegocioException.class, () -> service.criar(request()));
        assertTrue(ex.getMessage().contains("sala"));
        verify(reservaRepository, never()).save(any());
    }

    @Test void entidadeDeveRejeitarHorarioInvalido() {
        assertThrows(IllegalArgumentException.class,
                () -> new Reserva(professor, "Engenharia", sala, fim, inicio, Set.of(ativo)));
    }
}
