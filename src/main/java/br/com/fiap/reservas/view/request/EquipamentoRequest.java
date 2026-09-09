package br.com.fiap.reservas.view.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(name = "EquipamentoRequest", description = "Dados necessários para cadastrar um equipamento")
public record EquipamentoRequest(
        @Schema(description = "Nome do equipamento", example = "Notebook Dell Latitude", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "Nome é obrigatório") String nome,
        @Schema(description = "Tipo/categoria do equipamento", example = "Notebook", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "Tipo é obrigatório") String tipo,
        @Schema(description = "Indica se o equipamento está disponível para novas reservas", example = "true")
        Boolean ativo) {
}
