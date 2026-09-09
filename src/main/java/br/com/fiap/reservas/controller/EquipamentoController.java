package br.com.fiap.reservas.controller;

import br.com.fiap.reservas.service.EquipamentoService;
import br.com.fiap.reservas.view.request.EquipamentoRequest;
import br.com.fiap.reservas.view.response.EquipamentoResponse;
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
@RequestMapping("/equipamentos")
@Tag(name = "Equipamentos", description = "Cadastro, consulta e ativação de equipamentos")
public class EquipamentoController {
    private final EquipamentoService service;
    public EquipamentoController(EquipamentoService service) { this.service = service; }

    @PostMapping
    @Operation(summary = "Cadastrar equipamento", description = "Cria um equipamento e permite informar se ele está ativo. Por padrão, o serviço pode assumir o equipamento como ativo.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Equipamento criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos", content = @Content(schema = @Schema(implementation = br.com.fiap.reservas.view.response.ErrorResponse.class)))
    })
    public ResponseEntity<EquipamentoResponse> criar(@Valid @RequestBody EquipamentoRequest request) {
        EquipamentoResponse response = service.criar(request);
        return ResponseEntity.created(URI.create("/equipamentos/" + response.id())).body(response);
    }

    @GetMapping
    @Operation(summary = "Listar equipamentos", description = "Retorna todos os equipamentos cadastrados.")
    @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso")
    public ResponseEntity<List<EquipamentoResponse>> listar() { return ResponseEntity.ok(service.listar()); }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar equipamento", description = "Busca um equipamento pelo identificador.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Equipamento encontrado"),
            @ApiResponse(responseCode = "404", description = "Equipamento não encontrado", content = @Content(schema = @Schema(implementation = br.com.fiap.reservas.view.response.ErrorResponse.class)))
    })
    public ResponseEntity<EquipamentoResponse> buscar(@Parameter(description = "ID do equipamento", example = "1") @PathVariable Long id) {
        return ResponseEntity.ok(service.buscar(id));
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "Alterar status do equipamento", description = "Ativa ou desativa um equipamento. Equipamentos inativos não devem ser utilizados em novas reservas.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Status alterado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Equipamento não encontrado", content = @Content(schema = @Schema(implementation = br.com.fiap.reservas.view.response.ErrorResponse.class)))
    })
    public ResponseEntity<EquipamentoResponse> alterarStatus(
            @Parameter(description = "ID do equipamento", example = "1") @PathVariable Long id,
            @Parameter(description = "Novo estado do equipamento", example = "true") @RequestParam boolean ativo) {
        return ResponseEntity.ok(service.alterarStatus(id, ativo));
    }
}
