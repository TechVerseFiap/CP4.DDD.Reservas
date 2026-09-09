package br.com.fiap.reservas.presentation.controller;

import br.com.fiap.reservas.application.dto.request.EquipamentoRequest;
import br.com.fiap.reservas.application.dto.response.EquipamentoResponse;
import br.com.fiap.reservas.application.usecase.port.in.IChangeEquipmentStatusUseCase;
import br.com.fiap.reservas.application.usecase.port.in.ICreateEquipmentUseCase;
import br.com.fiap.reservas.application.usecase.port.in.IGetEquipmentUseCase;
import br.com.fiap.reservas.application.usecase.port.in.IListEquipmentsUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/equipamentos")
@Tag(name = "Equipamentos", description = "Cadastro, consulta e ativacao de equipamentos")
public class EquipamentoController {

    private final ICreateEquipmentUseCase createEquipment;
    private final IListEquipmentsUseCase listEquipments;
    private final IGetEquipmentUseCase getEquipment;
    private final IChangeEquipmentStatusUseCase changeEquipmentStatus;

    public EquipamentoController(
        ICreateEquipmentUseCase createEquipment,
        IListEquipmentsUseCase listEquipments,
        IGetEquipmentUseCase getEquipment,
        IChangeEquipmentStatusUseCase changeEquipmentStatus
    ) {
        this.createEquipment = createEquipment;
        this.listEquipments = listEquipments;
        this.getEquipment = getEquipment;
        this.changeEquipmentStatus = changeEquipmentStatus;
    }

    @PostMapping
    @Operation(summary = "Cadastrar equipamento")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Equipamento criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados invalidos"),
            @ApiResponse(responseCode = "409", description = "Equipamento ja cadastrado")
    })
    public ResponseEntity<EquipamentoResponse> criar(@Valid @RequestBody EquipamentoRequest request) {
        EquipamentoResponse response = createEquipment.execute(request);
        return ResponseEntity.created(URI.create("/equipamentos/" + response.id())).body(response);
    }

    @GetMapping
    @Operation(summary = "Listar equipamentos")
    @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso")
    public ResponseEntity<List<EquipamentoResponse>> listar() {
        return ResponseEntity.ok(listEquipments.execute());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar equipamento")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Equipamento encontrado"),
            @ApiResponse(responseCode = "404", description = "Equipamento nao encontrado")
    })
    public ResponseEntity<EquipamentoResponse> buscar(@PathVariable Long id) {
        return ResponseEntity.ok(getEquipment.execute(id));
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "Alterar status do equipamento")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Status alterado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Equipamento nao encontrado")
    })
    public ResponseEntity<EquipamentoResponse> alterarStatus(
            @PathVariable Long id,
            @RequestParam boolean ativo) {
        return ResponseEntity.ok(changeEquipmentStatus.execute(id, ativo));
    }
}
