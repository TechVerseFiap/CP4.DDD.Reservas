package br.com.fiap.reservas.application.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "SalaResponse")
public record SalaResponse(Long id, String nome) {
}
