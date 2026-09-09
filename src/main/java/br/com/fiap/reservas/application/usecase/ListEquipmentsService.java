package br.com.fiap.reservas.application.usecase;

import br.com.fiap.reservas.application.dto.response.EquipamentoResponse;
import br.com.fiap.reservas.application.mapper.EquipamentoMapper;
import br.com.fiap.reservas.application.usecase.port.in.IListEquipmentsUseCase;
import br.com.fiap.reservas.domain.repository.IEquipamentoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ListEquipmentsService implements IListEquipmentsUseCase {

    private final IEquipamentoRepository repository;

    public ListEquipmentsService(IEquipamentoRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<EquipamentoResponse> execute() {
        return repository.findAll().stream().map(EquipamentoMapper::toResponse).toList();
    }
}
