package br.com.fiap.reservas.infrastructure.exception;

import br.com.fiap.reservas.application.exception.ResourceNotFoundException;
import br.com.fiap.reservas.domain.exception.DomainException;
import br.com.fiap.reservas.domain.exception.EquipmentUnavailableException;
import br.com.fiap.reservas.domain.exception.InactiveEquipmentException;
import br.com.fiap.reservas.domain.exception.InvalidTimeRangeException;
import br.com.fiap.reservas.domain.exception.MinimumAdvanceNoticeNotMetException;
import br.com.fiap.reservas.domain.exception.ReservationAlreadyCancelledException;
import br.com.fiap.reservas.domain.exception.RoomUnavailableException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.net.URI;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private final Clock clock;

    public GlobalExceptionHandler(Clock clock) {
        this.clock = clock;
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ProblemDetail notFound(ResourceNotFoundException exception) {
        return problem(HttpStatus.NOT_FOUND, "resource-not-found", "Resource not found", exception.getMessage());
    }

    @ExceptionHandler(DomainException.class)
    public ProblemDetail domain(DomainException exception) {
        HttpStatus status = statusFor(exception);
        ProblemDetail problem = problem(status, exception.code(), titleFor(exception), exception.getMessage());
        if (exception instanceof MinimumAdvanceNoticeNotMetException advance) {
            problem.setProperty("missingDays", advance.missingDays());
        }
        if (exception instanceof InactiveEquipmentException inactive) {
            problem.setProperty("equipment", inactive.equipmentNames());
        }
        return problem;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail validation(MethodArgumentNotValidException exception) {
        ProblemDetail problem = problem(
                HttpStatus.BAD_REQUEST,
                "validation-error",
                "Validation failed",
                "Um ou mais campos possuem valores invalidos");
        Map<String, String> errors = exception.getBindingResult().getFieldErrors().stream()
                .collect(Collectors.toMap(
                        FieldError::getField,
                        error -> error.getDefaultMessage() == null ? "Valor invalido" : error.getDefaultMessage(),
                        (first, ignored) -> first));
        problem.setProperty("errors", errors);
        return problem;
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ProblemDetail malformedJson(HttpMessageNotReadableException exception) {
        String detail = "JSON invalido ou dados em formato incorreto";
        if (exception.getCause() instanceof InvalidFormatException invalidFormat
                && !invalidFormat.getPath().isEmpty()) {
            String field = invalidFormat.getPath().getFirst().getFieldName();
            detail = "O campo '" + field + "' possui um formato invalido";
        }
        return problem(HttpStatus.BAD_REQUEST, "malformed-json", "Malformed request", detail);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ProblemDetail typeMismatch(MethodArgumentTypeMismatchException exception) {
        return problem(HttpStatus.BAD_REQUEST, "invalid-parameter", "Invalid parameter",
                "O parametro '" + exception.getName() + "' possui um formato invalido");
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ProblemDetail endpointNotFound(NoResourceFoundException exception) {
        return problem(HttpStatus.NOT_FOUND, "endpoint-not-found", "Endpoint not found",
                "Endpoint nao encontrado");
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ProblemDetail dataIntegrity(DataIntegrityViolationException exception) {
        return problem(HttpStatus.CONFLICT, "data-conflict", "Data conflict",
                "O recurso informado ja existe ou esta em conflito com outro registro");
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ProblemDetail illegalArgument(IllegalArgumentException exception) {
        return problem(HttpStatus.BAD_REQUEST, "invalid-argument", "Invalid argument", exception.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ProblemDetail> generic(Exception exception) {
        ProblemDetail problem = problem(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "internal-error",
                "Internal server error",
                "Ocorreu um erro interno no servidor");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(problem);
    }

    private ProblemDetail problem(HttpStatus status, String type, String title, String detail) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(status, detail);
        problem.setTitle(title);
        problem.setType(URI.create("https://reservas.fiapt.example/problems/" + type));
        problem.setProperty("timestamp", LocalDateTime.now(clock));
        return problem;
    }

    private HttpStatus statusFor(DomainException exception) {
        if (exception instanceof InvalidTimeRangeException) {
            return HttpStatus.BAD_REQUEST;
        }
        if (exception instanceof EquipmentUnavailableException
                || exception instanceof RoomUnavailableException
                || exception instanceof InactiveEquipmentException
                || exception instanceof MinimumAdvanceNoticeNotMetException
                || exception instanceof ReservationAlreadyCancelledException) {
            return HttpStatus.CONFLICT;
        }
        return HttpStatus.BAD_REQUEST;
    }

    private String titleFor(DomainException exception) {
        return switch (exception.code()) {
            case "equipment-unavailable" -> "Equipment unavailable";
            case "room-unavailable" -> "Room unavailable";
            case "inactive-equipment" -> "Inactive equipment";
            case "minimum-advance-not-met" -> "Minimum advance notice not met";
            case "invalid-time-range" -> "Invalid time range";
            default -> "Business rule violation";
        };
    }
}
