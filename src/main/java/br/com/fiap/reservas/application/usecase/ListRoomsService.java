package br.com.fiap.reservas.application.usecase;

import br.com.fiap.reservas.application.dto.response.SalaResponse;
import br.com.fiap.reservas.application.mapper.SalaMapper;
import br.com.fiap.reservas.application.usecase.port.in.IListRoomsUseCase;
import br.com.fiap.reservas.domain.repository.ISalaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ListRoomsService implements IListRoomsUseCase {

    private final ISalaRepository repository;

    public ListRoomsService(ISalaRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<SalaResponse> execute() {
        return repository.findAll().stream().map(SalaMapper::toResponse).toList();
    }
}
