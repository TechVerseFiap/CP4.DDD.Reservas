package br.com.fiap.reservas.application.usecase;

import br.com.fiap.reservas.application.dto.response.ReservaResponse;
import br.com.fiap.reservas.application.mapper.ReservaMapper;
import br.com.fiap.reservas.application.usecase.port.in.IListReservationsUseCase;
import br.com.fiap.reservas.domain.repository.IReservaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ListReservationsService implements IListReservationsUseCase {

    private final IReservaRepository repository;

    public ListReservationsService(IReservaRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReservaResponse> execute() {
        return repository.findAll().stream().map(ReservaMapper::toResponse).toList();
    }
}
