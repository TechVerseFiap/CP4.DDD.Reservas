package br.com.fiap.reservas.presentation.controller;

import br.com.fiap.reservas.application.dto.request.ProfessorRequest;
import br.com.fiap.reservas.application.dto.response.ProfessorResponse;
import br.com.fiap.reservas.application.usecase.port.in.ICreateProfessorUseCase;
import br.com.fiap.reservas.application.usecase.port.in.IGetProfessorUseCase;
import br.com.fiap.reservas.application.usecase.port.in.IListProfessorsUseCase;
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
@RequestMapping("/professores")
@Tag(name = "Professores", description = "Operacoes de cadastro e consulta de professores")
public class ProfessorController {

    private final ICreateProfessorUseCase createProfessor;
    private final IListProfessorsUseCase listProfessors;
    private final IGetProfessorUseCase getProfessor;

    public ProfessorController(
            ICreateProfessorUseCase createProfessor,
            IListProfessorsUseCase listProfessors,
            IGetProfessorUseCase getProfessor) {
        this.createProfessor = createProfessor;
        this.listProfessors = listProfessors;
        this.getProfessor = getProfessor;
    }

    @PostMapping
    @Operation(summary = "Cadastrar professor")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Professor criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados invalidos"),
            @ApiResponse(responseCode = "409", description = "E-mail ja cadastrado")
    })
    public ResponseEntity<ProfessorResponse> criar(@Valid @RequestBody ProfessorRequest request) {
        ProfessorResponse response = createProfessor.execute(request);
        return ResponseEntity.created(URI.create("/professores/" + response.id())).body(response);
    }

    @GetMapping
    @Operation(summary = "Listar professores")
    @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso")
    public ResponseEntity<List<ProfessorResponse>> listar() {
        return ResponseEntity.ok(listProfessors.execute());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar professor")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Professor encontrado"),
            @ApiResponse(responseCode = "404", description = "Professor nao encontrado")
    })
    public ResponseEntity<ProfessorResponse> buscar(@PathVariable Long id) {
        return ResponseEntity.ok(getProfessor.execute(id));
    }
}
