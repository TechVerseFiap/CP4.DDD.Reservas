package br.com.fiap.reservas.controller;

import br.com.fiap.reservas.service.ProfessorService;
import br.com.fiap.reservas.view.request.ProfessorRequest;
import br.com.fiap.reservas.view.response.ProfessorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
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
@RequestMapping("/professores")
@Tag(name = "Professores", description = "Operações de cadastro e consulta de professores")
public class ProfessorController {

    private final ProfessorService service;

    public ProfessorController(ProfessorService service) { this.service = service; }

    @PostMapping
    @Operation(summary = "Cadastrar professor", description = "Cria um professor com nome e e-mail válidos.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Professor criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos", content = @Content(schema = @Schema(implementation = br.com.fiap.reservas.view.response.ErrorResponse.class)))
    })
    public ResponseEntity<ProfessorResponse> criar(@Valid @RequestBody ProfessorRequest request) {
        ProfessorResponse response = service.criar(request);
        return ResponseEntity.created(URI.create("/professores/" + response.id())).body(response);
    }

    @GetMapping
    @Operation(summary = "Listar professores", description = "Retorna todos os professores cadastrados.")
    @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso")
    public ResponseEntity<List<ProfessorResponse>> listar() { return ResponseEntity.ok(service.listar()); }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar professor", description = "Busca um professor pelo identificador.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Professor encontrado"),
            @ApiResponse(responseCode = "404", description = "Professor não encontrado", content = @Content(schema = @Schema(implementation = br.com.fiap.reservas.view.response.ErrorResponse.class)))
    })
    public ResponseEntity<ProfessorResponse> buscar(
            @Parameter(description = "ID do professor", example = "1") @PathVariable Long id) {
        return ResponseEntity.ok(service.buscar(id));
    }
}
