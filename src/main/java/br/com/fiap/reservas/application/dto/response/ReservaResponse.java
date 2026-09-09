package br.com.fiap.reservas.application.dto.response;

import br.com.fiap.reservas.domain.model.ReservaStatus;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.List;

@Schema(name = "ReservaResponse", description = "Representacao completa de uma reserva")
public record ReservaResponse(
        Long id,
        Long professorId,
        String professorNome,
        String curso,
        Long salaId,
        String salaNome,
        LocalDateTime retirada,
        LocalDateTime entrega,
        List<EquipamentoResponse> equipamentos,
        @Schema(description = "Status atual da reserva", example = "CONFIRMADA") ReservaStatus status) {
}
