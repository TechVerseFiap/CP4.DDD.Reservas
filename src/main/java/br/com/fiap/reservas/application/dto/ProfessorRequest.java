package br.com.fiap.reservas.application.dto;
import jakarta.validation.constraints.*;
public record ProfessorRequest(@NotBlank String nome, @Email @NotBlank String email) {}
