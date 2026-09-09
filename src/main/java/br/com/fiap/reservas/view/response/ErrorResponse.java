package br.com.fiap.reservas.view.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

@Schema(name = "ErrorResponse", description = "Formato padrão de erro da API")
public record ErrorResponse(
        @Schema(example = "404") int status,
        @Schema(example = "Professor não encontrado") String mensagem,
        @Schema(example = "2026-09-08T21:50:00") LocalDateTime timestamp) {
}
