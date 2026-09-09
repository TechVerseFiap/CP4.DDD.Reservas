package br.com.fiap.reservas.application.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Schema(name = "EquipamentoReservaRequest", description = "Equipamento e quantidade solicitados na reserva")
public record EquipamentoReservaRequest(
        @NotNull(message = "ID do equipamento e obrigatorio")
        @Schema(example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
        Long equipamentoId,
        @Positive(message = "A quantidade deve ser positiva")
        @Schema(example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
        int quantidade) {
}
