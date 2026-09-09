package br.com.fiap.reservas.application.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import jakarta.validation.Valid;
import jakarta.validation.constraints.AssertTrue;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.List;

@Schema(name = "ReservaRequest", description = "Dados necessarios para criar uma reserva")
public record ReservaRequest(
        @NotNull(message = "Professor e obrigatorio")
        @Schema(example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
        Long professorId,
        @NotBlank(message = "Curso e obrigatorio")
        @Size(max = 120, message = "Curso deve possuir no maximo 120 caracteres")
        @Schema(example = "Engenharia de Software", requiredMode = Schema.RequiredMode.REQUIRED)
        String curso,
        @NotNull(message = "Sala e obrigatoria")
        @Schema(example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
        Long salaId,
        @NotNull(message = "Horario de retirada e obrigatorio")
        @Schema(example = "2026-09-10T19:00:00", requiredMode = Schema.RequiredMode.REQUIRED)
        LocalDateTime retirada,
        @NotNull(message = "Horario de entrega e obrigatorio")
        @Schema(example = "2026-09-10T22:00:00", requiredMode = Schema.RequiredMode.REQUIRED)
        LocalDateTime entrega,
        @Schema(example = "[1, 2]", requiredMode = Schema.RequiredMode.REQUIRED)
        Set<@NotNull(message = "ID do equipamento e obrigatorio") @Positive(message = "ID do equipamento deve ser positivo") Long> equipamentosIds,
        @Schema(description = "Formato alternativo que permite informar quantidade por equipamento")
        List<@Valid EquipamentoReservaRequest> equipamentos) {

    public ReservaRequest(
            Long professorId,
            String curso,
            Long salaId,
            LocalDateTime retirada,
            LocalDateTime entrega,
            Set<Long> equipamentosIds) {
        this(professorId, curso, salaId, retirada, entrega, equipamentosIds, null);
    }

    @AssertTrue(message = "Ao menos um equipamento deve ser informado")
    public boolean hasEquipmentSelection() {
        return (equipamentosIds != null && !equipamentosIds.isEmpty())
                || (equipamentos != null && !equipamentos.isEmpty());
    }

    public Set<Long> equipmentIds() {
        if (equipamentos != null && !equipamentos.isEmpty()) {
            return equipamentos.stream().map(EquipamentoReservaRequest::equipamentoId).collect(java.util.stream.Collectors.toUnmodifiableSet());
        }
        return equipamentosIds == null ? Set.of() : Set.copyOf(equipamentosIds);
    }
}
