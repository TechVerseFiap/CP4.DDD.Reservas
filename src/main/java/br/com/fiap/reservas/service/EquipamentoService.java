package br.com.fiap.reservas.service;

import br.com.fiap.reservas.view.request.EquipamentoRequest;
import br.com.fiap.reservas.view.response.EquipamentoResponse;
import java.util.List;

public interface EquipamentoService {
    EquipamentoResponse criar(EquipamentoRequest request);
    List<EquipamentoResponse> listar();
    EquipamentoResponse buscar(Long id);
    EquipamentoResponse alterarStatus(Long id, boolean ativo);
}
