package br.com.fiap.reservas.application.dto;
import jakarta.validation.constraints.*;
public record EquipamentoRequest(@NotBlank String nome, @NotBlank String tipo, Boolean ativo) {}
