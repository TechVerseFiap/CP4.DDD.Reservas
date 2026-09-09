package br.com.fiap.reservas.application.usecase;

import br.com.fiap.reservas.application.dto.response.SalaResponse;
import br.com.fiap.reservas.application.exception.ResourceNotFoundException;
import br.com.fiap.reservas.application.mapper.SalaMapper;
import br.com.fiap.reservas.application.usecase.port.in.IGetRoomUseCase;
import br.com.fiap.reservas.domain.repository.ISalaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class GetRoomService implements IGetRoomUseCase {

    private final ISalaRepository repository;

    public GetRoomService(ISalaRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional(readOnly = true)
    public SalaResponse execute(Long id) {
        return repository.findById(id)
                .map(SalaMapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Sala nao encontrada"));
    }
}
