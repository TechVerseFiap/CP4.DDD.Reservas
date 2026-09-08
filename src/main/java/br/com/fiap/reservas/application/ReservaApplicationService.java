package br.com.fiap.reservas.application;

import br.com.fiap.reservas.application.dto.ReservaRequest;
import br.com.fiap.reservas.application.exception.*;
import br.com.fiap.reservas.domain.model.*;
import br.com.fiap.reservas.domain.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.*;
import java.util.*;

@Service
public class ReservaApplicationService {
    private final ReservaRepository reservaRepository;
    private final ProfessorRepository professorRepository;
    private final SalaRepository salaRepository;
    private final EquipamentoRepository equipamentoRepository;

    public ReservaApplicationService(ReservaRepository reservaRepository, ProfessorRepository professorRepository,
                                     SalaRepository salaRepository, EquipamentoRepository equipamentoRepository) {
        this.reservaRepository = reservaRepository;
        this.professorRepository = professorRepository;
        this.salaRepository = salaRepository;
        this.equipamentoRepository = equipamentoRepository;
    }

    @Transactional
    public Reserva criar(ReservaRequest request) {
        validarAntecedencia(request.retirada());

        Professor professor = professorRepository.findById(request.professorId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Professor não encontrado"));
        Sala sala = salaRepository.findById(request.salaId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Sala não encontrada"));

        Set<Equipamento> equipamentos = new HashSet<>();
        for (Long id : request.equipamentosIds()) {
            Equipamento equipamento = equipamentoRepository.findById(id)
                    .orElseThrow(() -> new RecursoNaoEncontradoException("Equipamento não encontrado: " + id));
            if (!Boolean.TRUE.equals(equipamento.getAtivo())) {
                throw new RegraNegocioException("Equipamento " + equipamento.getNome() + " está inativo e não pode ser reservado");
            }
            if (reservaRepository.existeConflitoEquipamento(id, request.retirada(), request.entrega())) {
                throw new RegraNegocioException("Equipamento " + equipamento.getNome() + " já está reservado no período solicitado");
            }
            equipamentos.add(equipamento);
        }

        if (reservaRepository.existeConflitoSala(sala.getId(), request.retirada(), request.entrega())) {
            throw new RegraNegocioException("A sala " + sala.getNome() + " já possui reserva no período solicitado");
        }

        Reserva reserva = new Reserva(professor, request.curso(), sala, request.retirada(), request.entrega(), equipamentos);
        return reservaRepository.save(reserva);
    }

    public List<Reserva> listar() { return reservaRepository.findAll(); }
    public Reserva buscar(Long id) { return reservaRepository.findById(id).orElseThrow(() -> new RecursoNaoEncontradoException("Reserva não encontrada")); }

    private void validarAntecedencia(LocalDateTime retirada) {
        if (!retirada.isAfter(LocalDateTime.now().plusDays(7).minusNanos(1))) {
            throw new RegraNegocioException("A reserva deve ser realizada com no mínimo uma semana de antecedência");
        }
    }
}
