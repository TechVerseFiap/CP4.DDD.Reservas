package br.com.fiap.reservas.view.response;

import br.com.fiap.reservas.model.ReservaStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.List;

@Schema(name = "ReservaResponse", description = "Representação completa de uma reserva")
public record ReservaResponse(
        @Schema(example = "1") Long id,
        @Schema(example = "1") Long professorId,
        @Schema(example = "Severus Snape") String professorNome,
        @Schema(example = "Engenharia de Software") String curso,
        @Schema(example = "1") Long salaId,
        @Schema(example = "Laboratório 01") String salaNome,
        @Schema(example = "2026-09-10T19:00:00") LocalDateTime retirada,
        @Schema(example = "2026-09-10T22:00:00") LocalDateTime entrega,
        List<EquipamentoResponse> equipamentos,
        @Schema(description = "Status atual da reserva", example = "ATIVA") ReservaStatus status) {
}
