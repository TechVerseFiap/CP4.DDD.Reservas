package br.com.fiap.reservas.service;

import br.com.fiap.reservas.view.request.ReservaRequest;
import br.com.fiap.reservas.view.response.ReservaResponse;
import java.util.List;

public interface ReservaService {
    ReservaResponse criar(ReservaRequest request);
    List<ReservaResponse> listar();
    ReservaResponse buscar(Long id);
}
