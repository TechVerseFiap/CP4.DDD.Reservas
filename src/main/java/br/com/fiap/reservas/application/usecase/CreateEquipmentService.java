package br.com.fiap.reservas.application.usecase;

import br.com.fiap.reservas.application.dto.request.EquipamentoRequest;
import br.com.fiap.reservas.application.dto.response.EquipamentoResponse;
import br.com.fiap.reservas.application.mapper.EquipamentoMapper;
import br.com.fiap.reservas.application.usecase.port.in.ICreateEquipmentUseCase;
import br.com.fiap.reservas.domain.repository.IEquipamentoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CreateEquipmentService implements ICreateEquipmentUseCase {

    private final IEquipamentoRepository repository;

    public CreateEquipmentService(IEquipamentoRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional
    public EquipamentoResponse execute(EquipamentoRequest request) {
        return EquipamentoMapper.toResponse(repository.save(EquipamentoMapper.toDomain(request)));
    }
}
