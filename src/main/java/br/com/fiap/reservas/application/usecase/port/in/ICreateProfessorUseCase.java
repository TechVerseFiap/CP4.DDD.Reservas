package br.com.fiap.reservas.application.usecase.port.in;

import br.com.fiap.reservas.application.dto.request.ProfessorRequest;
import br.com.fiap.reservas.application.dto.response.ProfessorResponse;

public interface ICreateProfessorUseCase {

    ProfessorResponse execute(ProfessorRequest request);
}
