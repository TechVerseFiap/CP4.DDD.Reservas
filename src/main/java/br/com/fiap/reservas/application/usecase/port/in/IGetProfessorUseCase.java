package br.com.fiap.reservas.application.usecase.port.in;

import br.com.fiap.reservas.application.dto.response.ProfessorResponse;

public interface IGetProfessorUseCase {

    ProfessorResponse execute(Long id);
}
