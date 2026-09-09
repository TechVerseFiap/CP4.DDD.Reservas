package br.com.fiap.reservas.application.usecase;

import br.com.fiap.reservas.application.dto.response.ReservaResponse;
import br.com.fiap.reservas.application.exception.ResourceNotFoundException;
import br.com.fiap.reservas.application.mapper.ReservaMapper;
import br.com.fiap.reservas.application.usecase.port.in.IGetReservationUseCase;
import br.com.fiap.reservas.domain.repository.IReservaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class GetReservationService implements IGetReservationUseCase {

    private final IReservaRepository repository;

    public GetReservationService(IReservaRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional(readOnly = true)
    public ReservaResponse execute(Long id) {
        return repository.findById(id)
                .map(ReservaMapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Reserva nao encontrada"));
    }
}
