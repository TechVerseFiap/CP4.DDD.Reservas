package br.com.fiap.reservas.application.dto;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;
import java.util.Set;
public record ReservaRequest(
        @NotNull Long professorId,
        @NotBlank String curso,
        @NotNull Long salaId,
        @NotNull LocalDateTime retirada,
        @NotNull LocalDateTime entrega,
        @NotEmpty Set<Long> equipamentosIds) {}
