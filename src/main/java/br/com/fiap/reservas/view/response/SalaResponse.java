package br.com.fiap.reservas.view.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "SalaResponse", description = "Representação de uma sala")
public record SalaResponse(
        @Schema(example = "1") Long id,
        @Schema(example = "Laboratório 01") String nome) {
}
