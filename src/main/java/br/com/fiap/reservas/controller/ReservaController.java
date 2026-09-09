package br.com.fiap.reservas.controller;

import br.com.fiap.reservas.service.ReservaService;
import br.com.fiap.reservas.view.request.ReservaRequest;
import br.com.fiap.reservas.view.response.ReservaResponse;
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
@RequestMapping("/reservas")
@Tag(name = "Reservas", description = "Criação e consulta de reservas de salas e equipamentos")
public class ReservaController {
    private final ReservaService service;
    public ReservaController(ReservaService service) { this.service = service; }

    @PostMapping
    @Operation(summary = "Criar reserva", description = "Cria uma reserva vinculando professor, curso, sala, período e um ou mais equipamentos. O serviço valida disponibilidade e regras de negócio antes de persistir.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Reserva criada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos", content = @Content(schema = @Schema(implementation = br.com.fiap.reservas.view.response.ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Professor, sala ou equipamento não encontrado", content = @Content(schema = @Schema(implementation = br.com.fiap.reservas.view.response.ErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "Conflito com outra reserva ou regra de negócio", content = @Content(schema = @Schema(implementation = br.com.fiap.reservas.view.response.ErrorResponse.class)))
    })
    public ResponseEntity<ReservaResponse> criar(@Valid @RequestBody ReservaRequest request) {
        ReservaResponse response = service.criar(request);
        return ResponseEntity.created(URI.create("/reservas/" + response.id())).body(response);
    }

    @GetMapping
    @Operation(summary = "Listar reservas", description = "Retorna todas as reservas cadastradas.")
    @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso")
    public ResponseEntity<List<ReservaResponse>> listar() { return ResponseEntity.ok(service.listar()); }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar reserva", description = "Busca uma reserva pelo identificador.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Reserva encontrada"),
            @ApiResponse(responseCode = "404", description = "Reserva não encontrada", content = @Content(schema = @Schema(implementation = br.com.fiap.reservas.view.response.ErrorResponse.class)))
    })
    public ResponseEntity<ReservaResponse> buscar(@Parameter(description = "ID da reserva", example = "1") @PathVariable Long id) {
        return ResponseEntity.ok(service.buscar(id));
    }
}
