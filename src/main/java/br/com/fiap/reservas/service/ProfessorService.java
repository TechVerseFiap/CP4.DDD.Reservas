package br.com.fiap.reservas.service;

import br.com.fiap.reservas.view.request.ProfessorRequest;
import br.com.fiap.reservas.view.response.ProfessorResponse;
import java.util.List;

public interface ProfessorService {
    ProfessorResponse criar(ProfessorRequest request);
    List<ProfessorResponse> listar();
    ProfessorResponse buscar(Long id);
}
