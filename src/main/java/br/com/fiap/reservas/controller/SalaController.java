package br.com.fiap.reservas.controller;

import br.com.fiap.reservas.service.SalaService;
import br.com.fiap.reservas.view.request.SalaRequest;
import br.com.fiap.reservas.view.response.SalaResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/salas")
@Tag(name = "Salas", description = "Operações de cadastro e consulta de salas")
public class SalaController {
    private final SalaService service;
    public SalaController(SalaService service) { this.service = service; }

    @PostMapping
    @Operation(summary = "Cadastrar sala", description = "Cria uma nova sala para utilização nas reservas.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Sala criada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos", content = @Content(schema = @Schema(implementation = br.com.fiap.reservas.view.response.ErrorResponse.class)))
    })
    public ResponseEntity<SalaResponse> criar(@Valid @RequestBody SalaRequest request) {
        SalaResponse response = service.criar(request);
        return ResponseEntity.created(URI.create("/salas/" + response.id())).body(response);
    }

    @GetMapping
    @Operation(summary = "Listar salas", description = "Retorna todas as salas cadastradas.")
    @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso")
    public ResponseEntity<List<SalaResponse>> listar() { return ResponseEntity.ok(service.listar()); }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar sala", description = "Busca uma sala pelo identificador.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Sala encontrada"),
            @ApiResponse(responseCode = "404", description = "Sala não encontrada", content = @Content(schema = @Schema(implementation = br.com.fiap.reservas.view.response.ErrorResponse.class)))
    })
    public ResponseEntity<SalaResponse> buscar(@Parameter(description = "ID da sala", example = "1") @PathVariable Long id) {
        return ResponseEntity.ok(service.buscar(id));
    }
}
