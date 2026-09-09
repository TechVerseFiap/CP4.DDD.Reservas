package br.com.fiap.reservas.view.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Schema(name = "ProfessorRequest", description = "Dados necessários para cadastrar um professor")
public record ProfessorRequest(
        @Schema(description = "Nome completo do professor", example = "Severus Snape", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "Nome é obrigatório") String nome,
        @Schema(description = "E-mail institucional", example = "snape@fiap.com.br", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "E-mail é obrigatório")
        @Email(message = "E-mail inválido") String email) {
}
