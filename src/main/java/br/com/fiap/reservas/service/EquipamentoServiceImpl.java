package br.com.fiap.reservas.service;

import br.com.fiap.reservas.exception.RecursoNaoEncontradoException;
import br.com.fiap.reservas.model.Equipamento;
import br.com.fiap.reservas.repository.EquipamentoRepository;
import br.com.fiap.reservas.view.request.EquipamentoRequest;
import br.com.fiap.reservas.view.response.EquipamentoResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class EquipamentoServiceImpl implements EquipamentoService {

    private final EquipamentoRepository repository;

    public EquipamentoServiceImpl(EquipamentoRepository repository) {
        this.repository = repository;
    }

    @Override
    public EquipamentoResponse criar(EquipamentoRequest request) {
        boolean ativo = request.ativo() == null || request.ativo();
        Equipamento equipamento = Equipamento.criar(request.nome(), request.tipo(), ativo);
        return toResponse(repository.save(equipamento));
    }

    @Override
    public List<EquipamentoResponse> listar() {
        return repository.findAll().stream().map(this::toResponse).toList();
    }

    @Override
    public EquipamentoResponse buscar(Long id) {
        return toResponse(findEntity(id));
    }

    @Override
    @Transactional
    public EquipamentoResponse alterarStatus(Long id, boolean ativo) {
        Equipamento equipamento = findEntity(id);

        if (ativo) {
            equipamento.ativar();
        } else {
            equipamento.desativar();
        }

        return toResponse(repository.save(equipamento));
    }

    private Equipamento findEntity(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Equipamento não encontrado"));
    }

    private EquipamentoResponse toResponse(Equipamento equipamento) {
        return new EquipamentoResponse(
                equipamento.getId(),
                equipamento.getNome(),
                equipamento.getTipo(),
                equipamento.isAtivo());
    }
}
