package br.com.fiap.reservas.view.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(name = "SalaRequest", description = "Dados necessários para cadastrar uma sala")
public record SalaRequest(
        @Schema(description = "Nome ou identificação da sala", example = "Laboratório 01", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "Nome é obrigatório") String nome) {
}
