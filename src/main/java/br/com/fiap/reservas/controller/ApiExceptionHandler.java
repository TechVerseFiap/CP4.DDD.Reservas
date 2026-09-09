package br.com.fiap.reservas.controller;

import br.com.fiap.reservas.exception.RecursoNaoEncontradoException;
import br.com.fiap.reservas.exception.RegraNegocioException;
import br.com.fiap.reservas.view.response.ErrorResponse;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.time.LocalDateTime;
import java.util.stream.Collectors;

@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(RegraNegocioException.class)
    public ResponseEntity<ErroResponse> regra(RegraNegocioException e) {

        return ResponseEntity
                .badRequest()
                .body(new ErroResponse(
                        400,
                        "Regra de negócio: " + e.getMessage(),
                        LocalDateTime.now()
                ));
    }

    @ExceptionHandler(RecursoNaoEncontradoException.class)
    public ResponseEntity<ErroResponse> notFound(RecursoNaoEncontradoException e) {

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ErroResponse(
                        404,
                        e.getMessage(),
                        LocalDateTime.now()
                ));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErroResponse> illegal(IllegalArgumentException e) {

        return ResponseEntity
                .badRequest()
                .body(new ErroResponse(
                        400,
                        e.getMessage(),
                        LocalDateTime.now()
                ));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErroResponse> validation(
            MethodArgumentNotValidException e
    ) {

        String msg = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(x -> x.getField() + ": " + x.getDefaultMessage())
                .collect(Collectors.joining("; "));

        return ResponseEntity
                .badRequest()
                .body(new ErroResponse(
                        400,
                        "Dados inválidos: " + msg,
                        LocalDateTime.now()
                ));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErroResponse> jsonInvalido(
            HttpMessageNotReadableException e
    ) {

        String mensagem = "JSON inválido ou dados em formato incorreto.";

        if (e.getCause() instanceof InvalidFormatException invalidFormat) {

            String campo = invalidFormat.getPath()
                    .stream()
                    .findFirst()
                    .map(ref -> ref.getFieldName())
                    .orElse("campo");

            mensagem = "O campo '" + campo +
                    "' possui um formato inválido. " +
                    "Verifique o valor informado.";
        }

        return ResponseEntity
                .badRequest()
                .body(new ErroResponse(
                        400,
                        mensagem,
                        LocalDateTime.now()
                ));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErroResponse> erroGenerico(Exception e) {

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErroResponse(
                        500,
                        "Ocorreu um erro interno no servidor.",
                        LocalDateTime.now()
                ));
    }

    public record ErroResponse(
            int status,
            String mensagem,
            LocalDateTime timestamp
    ) {
    }
}
