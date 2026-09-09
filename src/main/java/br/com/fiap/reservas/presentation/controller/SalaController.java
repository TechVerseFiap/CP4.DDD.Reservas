package br.com.fiap.reservas.presentation.controller;

import br.com.fiap.reservas.application.dto.request.SalaRequest;
import br.com.fiap.reservas.application.dto.response.SalaResponse;
import br.com.fiap.reservas.application.usecase.port.in.ICreateRoomUseCase;
import br.com.fiap.reservas.application.usecase.port.in.IGetRoomUseCase;
import br.com.fiap.reservas.application.usecase.port.in.IListRoomsUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/salas")
@Tag(name = "Salas", description = "Operacoes de cadastro e consulta de salas")
public class SalaController {

    private final ICreateRoomUseCase createRoom;
    private final IListRoomsUseCase listRooms;
    private final IGetRoomUseCase getRoom;

    public SalaController(
            ICreateRoomUseCase createRoom,
            IListRoomsUseCase listRooms,
            IGetRoomUseCase getRoom) {
        this.createRoom = createRoom;
        this.listRooms = listRooms;
        this.getRoom = getRoom;
    }

    @PostMapping
    @Operation(summary = "Cadastrar sala")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Sala criada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados invalidos"),
            @ApiResponse(responseCode = "409", description = "Sala ja cadastrada")
    })
    public ResponseEntity<SalaResponse> criar(@Valid @RequestBody SalaRequest request) {
        SalaResponse response = createRoom.execute(request);
        return ResponseEntity.created(URI.create("/salas/" + response.id())).body(response);
    }

    @GetMapping
    @Operation(summary = "Listar salas")
    @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso")
    public ResponseEntity<List<SalaResponse>> listar() {
        return ResponseEntity.ok(listRooms.execute());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar sala")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Sala encontrada"),
            @ApiResponse(responseCode = "404", description = "Sala nao encontrada")
    })
    public ResponseEntity<SalaResponse> buscar(@PathVariable Long id) {
        return ResponseEntity.ok(getRoom.execute(id));
    }
}
