package br.com.fiap.reservas.application.usecase;

import br.com.fiap.reservas.application.dto.request.SalaRequest;
import br.com.fiap.reservas.application.dto.response.SalaResponse;
import br.com.fiap.reservas.application.mapper.SalaMapper;
import br.com.fiap.reservas.application.usecase.port.in.ICreateRoomUseCase;
import br.com.fiap.reservas.domain.repository.ISalaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CreateRoomService implements ICreateRoomUseCase {

    private final ISalaRepository repository;

    public CreateRoomService(ISalaRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional
    public SalaResponse execute(SalaRequest request) {
        return SalaMapper.toResponse(repository.save(SalaMapper.toDomain(request)));
    }
}
