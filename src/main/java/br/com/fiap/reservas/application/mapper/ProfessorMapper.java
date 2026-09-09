package br.com.fiap.reservas.application.mapper;

import br.com.fiap.reservas.application.dto.request.ProfessorRequest;
import br.com.fiap.reservas.application.dto.response.ProfessorResponse;
import br.com.fiap.reservas.domain.model.Professor;

public final class ProfessorMapper {

    private ProfessorMapper() {
    }

    public static Professor toDomain(ProfessorRequest request) {
        return Professor.criar(request.nome(), request.email());
    }

    public static ProfessorResponse toResponse(Professor professor) {
        return new ProfessorResponse(professor.getId(), professor.getNome(), professor.getEmail());
    }
}
