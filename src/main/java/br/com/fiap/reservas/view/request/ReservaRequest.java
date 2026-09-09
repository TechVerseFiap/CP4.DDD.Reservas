package br.com.fiap.reservas.view.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Set;

@Schema(name = "ReservaRequest", description = "Dados necessários para criar uma reserva")
public record ReservaRequest(
        @Schema(description = "ID do professor responsável pela reserva", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull(message = "Professor é obrigatório") Long professorId,
        @Schema(description = "Curso associado à reserva", example = "Engenharia de Software", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "Curso é obrigatório") String curso,
        @Schema(description = "ID da sala que será utilizada", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull(message = "Sala é obrigatória") Long salaId,
        @Schema(description = "Data e hora de início da reserva no formato ISO-8601", example = "2026-09-10T19:00:00", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull(message = "Horário de retirada é obrigatório") LocalDateTime retirada,
        @Schema(description = "Data e hora de término da reserva no formato ISO-8601", example = "2026-09-10T22:00:00", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull(message = "Horário de entrega é obrigatório") LocalDateTime entrega,
        @Schema(description = "IDs dos equipamentos que serão utilizados", example = "[1, 2]", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotEmpty(message = "Ao menos um equipamento deve ser informado") Set<Long> equipamentosIds) {
}
