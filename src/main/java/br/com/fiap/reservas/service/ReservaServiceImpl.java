package br.com.fiap.reservas.service;

import br.com.fiap.reservas.exception.RecursoNaoEncontradoException;
import br.com.fiap.reservas.exception.RegraNegocioException;
import br.com.fiap.reservas.model.*;
import br.com.fiap.reservas.repository.*;
import br.com.fiap.reservas.view.request.ReservaRequest;
import br.com.fiap.reservas.view.response.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class ReservaServiceImpl implements ReservaService {

    private static final long ANTECEDENCIA_MINIMA_DIAS = 7;

    private final ReservaRepository reservaRepository;
    private final ProfessorRepository professorRepository;
    private final SalaRepository salaRepository;
    private final EquipamentoRepository equipamentoRepository;
    private final Clock clock;

    public ReservaServiceImpl(
            ReservaRepository reservaRepository,
            ProfessorRepository professorRepository,
            SalaRepository salaRepository,
            EquipamentoRepository equipamentoRepository,
            Clock clock) {
        this.reservaRepository = reservaRepository;
        this.professorRepository = professorRepository;
        this.salaRepository = salaRepository;
        this.equipamentoRepository = equipamentoRepository;
        this.clock = clock;
    }

    @Override
    @Transactional
    public ReservaResponse criar(ReservaRequest request) {
        validarAntecedencia(request.retirada());

        Professor professor = buscarProfessor(request.professorId());
        Sala sala = buscarSala(request.salaId());
        Set<Equipamento> equipamentos = buscarEValidarEquipamentos(request);

        validarConflitoSala(sala, request);

        Reserva reserva = Reserva.criar(
                professor,
                request.curso(),
                sala,
                request.retirada(),
                request.entrega(),
                equipamentos);

        return toResponse(reservaRepository.save(reserva));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReservaResponse> listar() {
        return reservaRepository.findAll().stream().map(this::toResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ReservaResponse buscar(Long id) {
        return toResponse(reservaRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Reserva não encontrada")));
    }

    private Professor buscarProfessor(Long id) {
        return professorRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Professor não encontrado"));
    }

    private Sala buscarSala(Long id) {
        return salaRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Sala não encontrada"));
    }

    private Set<Equipamento> buscarEValidarEquipamentos(ReservaRequest request) {
        Set<Equipamento> equipamentos = new HashSet<>();

        for (Long id : request.equipamentosIds()) {
            Equipamento equipamento = equipamentoRepository.findById(id)
                    .orElseThrow(() -> new RecursoNaoEncontradoException(
                            "Equipamento não encontrado: " + id));

            validarEquipamento(equipamento, request);
            equipamentos.add(equipamento);
        }

        return equipamentos;
    }

    private void validarEquipamento(Equipamento equipamento, ReservaRequest request) {
        if (!equipamento.isAtivo()) {
            throw new RegraNegocioException(
                    "Equipamento " + equipamento.getNome() + " está inativo e não pode ser reservado");
        }

        if (reservaRepository.existeConflitoEquipamento(
                equipamento.getId(), request.retirada(), request.entrega())) {
            throw new RegraNegocioException(
                    "Equipamento " + equipamento.getNome() +
                    " já está reservado no período solicitado");
        }
    }

    private void validarConflitoSala(Sala sala, ReservaRequest request) {
        if (reservaRepository.existeConflitoSala(
                sala.getId(), request.retirada(), request.entrega())) {
            throw new RegraNegocioException(
                    "A sala " + sala.getNome() +
                    " já possui reserva no período solicitado");
        }
    }

    private void validarAntecedencia(LocalDateTime retirada) {
        if (!retirada.isAfter(LocalDateTime.now(clock).plusDays(ANTECEDENCIA_MINIMA_DIAS))) {
            throw new RegraNegocioException(
                    "A reserva deve ser realizada com no mínimo uma semana de antecedência");
        }
    }

    private ReservaResponse toResponse(Reserva reserva) {
        List<EquipamentoResponse> equipamentos = reserva.getEquipamentos().stream()
                .map(e -> new EquipamentoResponse(
                        e.getId(), e.getNome(), e.getTipo(), e.isAtivo()))
                .toList();

        return new ReservaResponse(
                reserva.getId(),
                reserva.getProfessor().getId(),
                reserva.getProfessor().getNome(),
                reserva.getCurso(),
                reserva.getSala().getId(),
                reserva.getSala().getNome(),
                reserva.getRetirada(),
                reserva.getEntrega(),
                equipamentos,
                reserva.getStatus());
    }
}
