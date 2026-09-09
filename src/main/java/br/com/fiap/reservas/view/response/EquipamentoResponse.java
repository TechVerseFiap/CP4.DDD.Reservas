package br.com.fiap.reservas.view.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "EquipamentoResponse", description = "Representação de um equipamento")
public record EquipamentoResponse(
        @Schema(example = "1") Long id,
        @Schema(example = "Notebook Dell Latitude") String nome,
        @Schema(example = "Notebook") String tipo,
        @Schema(description = "Indica se está ativo e disponível para novas reservas", example = "true") boolean ativo) {
}
