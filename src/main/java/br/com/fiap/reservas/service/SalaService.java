package br.com.fiap.reservas.service;

import br.com.fiap.reservas.view.request.SalaRequest;
import br.com.fiap.reservas.view.response.SalaResponse;
import java.util.List;

public interface SalaService {
    SalaResponse criar(SalaRequest request);
    List<SalaResponse> listar();
    SalaResponse buscar(Long id);
}
