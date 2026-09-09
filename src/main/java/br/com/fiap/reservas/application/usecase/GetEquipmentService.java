package br.com.fiap.reservas.application.usecase;

import br.com.fiap.reservas.application.dto.response.EquipamentoResponse;
import br.com.fiap.reservas.application.exception.ResourceNotFoundException;
import br.com.fiap.reservas.application.mapper.EquipamentoMapper;
import br.com.fiap.reservas.application.usecase.port.in.IGetEquipmentUseCase;
import br.com.fiap.reservas.domain.repository.IEquipamentoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class GetEquipmentService implements IGetEquipmentUseCase {

    private final IEquipamentoRepository repository;

    public GetEquipmentService(IEquipamentoRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional(readOnly = true)
    public EquipamentoResponse execute(Long id) {
        return repository.findById(id)
                .map(EquipamentoMapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Equipamento nao encontrado"));
    }
}
