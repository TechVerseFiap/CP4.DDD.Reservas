package br.com.fiap.reservas.application.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(name = "SalaRequest", description = "Dados necessarios para cadastrar uma sala")
public record SalaRequest(
        @NotBlank(message = "Nome e obrigatorio")
        @Size(max = 30, message = "Nome deve possuir no maximo 30 caracteres")
        @Schema(example = "204", requiredMode = Schema.RequiredMode.REQUIRED)
        String nome) {
}
