package br.com.fiap.reservas.web;

import br.com.fiap.reservas.application.exception.*;
import org.springframework.http.*;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.stream.Collectors;

@RestControllerAdvice
public class ApiExceptionHandler {
    @ExceptionHandler(RegraNegocioException.class)
    ResponseEntity<ErroResponse> regra(RegraNegocioException e){ return ResponseEntity.badRequest().body(new ErroResponse(400,e.getMessage(),LocalDateTime.now())); }
    @ExceptionHandler(RecursoNaoEncontradoException.class)
    ResponseEntity<ErroResponse> notFound(RecursoNaoEncontradoException e){ return ResponseEntity.status(404).body(new ErroResponse(404,e.getMessage(),LocalDateTime.now())); }
    @ExceptionHandler(IllegalArgumentException.class)
    ResponseEntity<ErroResponse> illegal(IllegalArgumentException e){ return ResponseEntity.badRequest().body(new ErroResponse(400,e.getMessage(),LocalDateTime.now())); }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<ErroResponse> validation(MethodArgumentNotValidException e){
        String msg=e.getBindingResult().getFieldErrors().stream().map(x->x.getField()+": "+x.getDefaultMessage()).collect(Collectors.joining("; "));
        return ResponseEntity.badRequest().body(new ErroResponse(400,msg,LocalDateTime.now()));
    }
    public record ErroResponse(int status,String mensagem,LocalDateTime timestamp){}
}
