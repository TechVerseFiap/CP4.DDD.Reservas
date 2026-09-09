package br.com.fiap.reservas.application.usecase.port.in;

import br.com.fiap.reservas.application.dto.response.ProfessorResponse;

import java.util.List;

public interface IListProfessorsUseCase {

    List<ProfessorResponse> execute();
}
