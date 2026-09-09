package br.com.fiap.reservas.application.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(name = "ProfessorRequest", description = "Dados necessarios para cadastrar um professor")
public record ProfessorRequest(
        @NotBlank(message = "Nome e obrigatorio")
        @Size(max = 120, message = "Nome deve possuir no maximo 120 caracteres")
        @Schema(example = "Maria Souza", requiredMode = Schema.RequiredMode.REQUIRED)
        String nome,
        @NotBlank(message = "E-mail e obrigatorio")
        @Email(message = "E-mail deve ser valido")
        @Size(max = 160, message = "E-mail deve possuir no maximo 160 caracteres")
        @Schema(example = "maria@fiap.com.br", requiredMode = Schema.RequiredMode.REQUIRED)
        String email) {
}
