package br.com.fiap.reservas.application.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "ProfessorResponse")
public record ProfessorResponse(Long id, String nome, String email) {
}
