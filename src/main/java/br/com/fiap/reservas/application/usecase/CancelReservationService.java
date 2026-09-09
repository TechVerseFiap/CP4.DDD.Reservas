package br.com.fiap.reservas.application.usecase;

import br.com.fiap.reservas.application.exception.ResourceNotFoundException;
import br.com.fiap.reservas.application.usecase.port.in.ICancelReservationUseCase;
import br.com.fiap.reservas.domain.model.Reserva;
import br.com.fiap.reservas.domain.repository.IReservaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CancelReservationService implements ICancelReservationUseCase {

    private static final Logger LOGGER = LoggerFactory.getLogger(CancelReservationService.class);

    private final IReservaRepository repository;

    public CancelReservationService(IReservaRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional
    public void execute(Long id) {
        Reserva reserva = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reserva nao encontrada"));
        reserva.cancelar();
        repository.save(reserva);
        LOGGER.info("reservation cancelled id={}", id);
    }
}
