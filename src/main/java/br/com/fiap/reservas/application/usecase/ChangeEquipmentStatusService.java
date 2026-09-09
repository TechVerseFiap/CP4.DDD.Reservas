package br.com.fiap.reservas.application.usecase;

import br.com.fiap.reservas.application.dto.response.EquipamentoResponse;
import br.com.fiap.reservas.application.exception.ResourceNotFoundException;
import br.com.fiap.reservas.application.mapper.EquipamentoMapper;
import br.com.fiap.reservas.application.usecase.port.in.IChangeEquipmentStatusUseCase;
import br.com.fiap.reservas.domain.model.Equipamento;
import br.com.fiap.reservas.domain.repository.IEquipamentoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ChangeEquipmentStatusService implements IChangeEquipmentStatusUseCase {

    private final IEquipamentoRepository repository;

    public ChangeEquipmentStatusService(IEquipamentoRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional
    public EquipamentoResponse execute(Long id, boolean active) {
        Equipamento equipamento = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Equipamento nao encontrado"));
        if (active) {
            equipamento.ativar();
        } else {
            equipamento.desativar();
        }
        return EquipamentoMapper.toResponse(repository.save(equipamento));
    }
}
