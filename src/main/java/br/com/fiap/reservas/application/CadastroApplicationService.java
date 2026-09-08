package br.com.fiap.reservas.application;

import br.com.fiap.reservas.application.dto.*;
import br.com.fiap.reservas.application.exception.RecursoNaoEncontradoException;
import br.com.fiap.reservas.domain.model.*;
import br.com.fiap.reservas.domain.repository.*;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CadastroApplicationService {
    private final EquipamentoRepository equipamentoRepository;
    private final ProfessorRepository professorRepository;
    private final SalaRepository salaRepository;
    public CadastroApplicationService(EquipamentoRepository e, ProfessorRepository p, SalaRepository s) { this.equipamentoRepository=e; this.professorRepository=p; this.salaRepository=s; }
    public Equipamento criarEquipamento(EquipamentoRequest r){ return equipamentoRepository.save(Equipamento.builder().nome(r.nome()).tipo(r.tipo()).ativo(r.ativo()==null || r.ativo()).build()); }
    public Professor criarProfessor(ProfessorRequest r){ return professorRepository.save(Professor.builder().nome(r.nome()).email(r.email()).build()); }
    public Sala criarSala(SalaRequest r){ return salaRepository.save(Sala.builder().nome(r.nome()).build()); }
    public List<Equipamento> listarEquipamentos(){ return equipamentoRepository.findAll(); }
    public List<Professor> listarProfessores(){ return professorRepository.findAll(); }
    public List<Sala> listarSalas(){ return salaRepository.findAll(); }
    public Equipamento equipamento(Long id){ return equipamentoRepository.findById(id).orElseThrow(()->new RecursoNaoEncontradoException("Equipamento não encontrado")); }
    public Equipamento alterarStatus(Long id, boolean ativo){ var e=equipamento(id); e.setAtivo(ativo); return equipamentoRepository.save(e); }
}
