package br.com.fiap.reservas.view.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "ProfessorResponse", description = "Representação de um professor")
public record ProfessorResponse(
        @Schema(example = "1") Long id,
        @Schema(example = "Severus Snape") String nome,
        @Schema(example = "snape@fiap.com.br") String email) {
}
