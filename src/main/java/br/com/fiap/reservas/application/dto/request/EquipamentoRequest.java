package br.com.fiap.reservas.application.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(name = "EquipamentoRequest", description = "Dados necessarios para cadastrar um equipamento")
public record EquipamentoRequest(
        @NotBlank(message = "Nome e obrigatorio")
        @Size(max = 120, message = "Nome deve possuir no maximo 120 caracteres")
        @Schema(example = "Datashow 01", requiredMode = Schema.RequiredMode.REQUIRED)
        String nome,
        @NotBlank(message = "Tipo e obrigatorio")
        @Size(max = 80, message = "Tipo deve possuir no maximo 80 caracteres")
        @Schema(example = "Datashow", requiredMode = Schema.RequiredMode.REQUIRED)
        String tipo,
        @Schema(example = "true", description = "Quando omitido, o equipamento inicia ativo")
        Boolean ativo
) { }
