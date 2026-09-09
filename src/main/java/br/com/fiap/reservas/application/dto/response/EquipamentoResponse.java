package br.com.fiap.reservas.application.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "EquipamentoResponse")
public record EquipamentoResponse(
        Long id,
        String nome,
        String tipo,
        @Schema(description = "Indica se o equipamento esta ativo") boolean ativo) {
}
