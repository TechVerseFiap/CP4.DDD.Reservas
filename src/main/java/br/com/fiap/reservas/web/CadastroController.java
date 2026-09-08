package br.com.fiap.reservas.web;

import br.com.fiap.reservas.application.CadastroApplicationService;
import br.com.fiap.reservas.application.dto.*;
import br.com.fiap.reservas.domain.model.*;
import jakarta.validation.Valid;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.net.URI;

@RestController
@RequestMapping("/cadastros")
public class CadastroController {
    private final CadastroApplicationService service;
    public CadastroController(CadastroApplicationService service){this.service=service;}
    @PostMapping("/equipamentos") public ResponseEntity<Equipamento> equipamento(@Valid @RequestBody EquipamentoRequest r){var x=service.criarEquipamento(r); return ResponseEntity.created(URI.create("/cadastros/equipamentos/"+x.getId())).body(x);}
    @GetMapping("/equipamentos") public Object equipamentos(){return service.listarEquipamentos();}
    @PatchMapping("/equipamentos/{id}/status") public Object status(@PathVariable Long id, @RequestParam boolean ativo){return service.alterarStatus(id, ativo);}
    @PostMapping("/professores") public ResponseEntity<Professor> professor(@Valid @RequestBody ProfessorRequest r){var x=service.criarProfessor(r); return ResponseEntity.status(HttpStatus.CREATED).body(x);}
    @GetMapping("/professores") public Object professores(){return service.listarProfessores();}
    @PostMapping("/salas") public ResponseEntity<Sala> sala(@Valid @RequestBody SalaRequest r){var x=service.criarSala(r); return ResponseEntity.status(HttpStatus.CREATED).body(x);}
    @GetMapping("/salas") public Object salas(){return service.listarSalas();}
}
