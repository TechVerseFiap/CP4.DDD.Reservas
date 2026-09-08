package br.com.fiap.reservas.application.dto;
import jakarta.validation.constraints.NotBlank;
public record SalaRequest(@NotBlank String nome) {}
