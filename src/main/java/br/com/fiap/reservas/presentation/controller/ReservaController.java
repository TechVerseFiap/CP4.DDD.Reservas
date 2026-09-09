package br.com.fiap.reservas.presentation.controller;

import br.com.fiap.reservas.application.dto.request.ReservaRequest;
import br.com.fiap.reservas.application.dto.response.ReservaResponse;
import br.com.fiap.reservas.application.usecase.port.in.ICancelReservationUseCase;
import br.com.fiap.reservas.application.usecase.port.in.ICreateReservationUseCase;
import br.com.fiap.reservas.application.usecase.port.in.IGetReservationUseCase;
import br.com.fiap.reservas.application.usecase.port.in.IListReservationsUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/reservas")
@Tag(name = "Reservas", description = "Criacao, consulta e cancelamento de reservas")
public class ReservaController {

    private final ICreateReservationUseCase createReservation;
    private final IListReservationsUseCase listReservations;
    private final IGetReservationUseCase getReservation;
    private final ICancelReservationUseCase cancelReservation;

    public ReservaController(
            ICreateReservationUseCase createReservation,
            IListReservationsUseCase listReservations,
            IGetReservationUseCase getReservation,
            ICancelReservationUseCase cancelReservation) {
        this.createReservation = createReservation;
        this.listReservations = listReservations;
        this.getReservation = getReservation;
        this.cancelReservation = cancelReservation;
    }

    @PostMapping
    @Operation(summary = "Criar reserva", description = "Cria uma reserva validando antecedencia, horarios, equipamentos e sala.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Reserva criada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados invalidos ou horario invalido"),
            @ApiResponse(responseCode = "404", description = "Professor, sala ou equipamento nao encontrado"),
            @ApiResponse(responseCode = "409", description = "Regra de negocio ou conflito de disponibilidade")
    })
    public ResponseEntity<ReservaResponse> criar(@Valid @RequestBody ReservaRequest request) {
        ReservaResponse response = createReservation.execute(request);
        return ResponseEntity.created(URI.create("/reservas/" + response.id())).body(response);
    }

    @GetMapping
    @Operation(summary = "Listar reservas")
    @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso")
    public ResponseEntity<List<ReservaResponse>> listar() {
        return ResponseEntity.ok(listReservations.execute());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar reserva")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Reserva encontrada"),
            @ApiResponse(responseCode = "404", description = "Reserva nao encontrada")
    })
    public ResponseEntity<ReservaResponse> buscar(@PathVariable Long id) {
        return ResponseEntity.ok(getReservation.execute(id));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Cancelar reserva")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Reserva cancelada"),
            @ApiResponse(responseCode = "404", description = "Reserva nao encontrada"),
            @ApiResponse(responseCode = "409", description = "Reserva ja cancelada")
    })
    public ResponseEntity<Void> cancelar(@PathVariable Long id) {
        cancelReservation.execute(id);
        return ResponseEntity.noContent().build();
    }
}
