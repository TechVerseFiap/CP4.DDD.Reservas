package br.com.fiap.reservas.web;

import br.com.fiap.reservas.application.ReservaApplicationService;
import br.com.fiap.reservas.application.dto.ReservaRequest;
import br.com.fiap.reservas.domain.model.Reserva;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.net.URI;

@RestController
@RequestMapping("/reservas")
public class ReservaController {
    private final ReservaApplicationService service;
    public ReservaController(ReservaApplicationService service){this.service=service;}
    @PostMapping public ResponseEntity<Reserva> criar(@Valid @RequestBody ReservaRequest request){ Reserva r=service.criar(request); return ResponseEntity.created(URI.create("/reservas/"+r.getId())).body(r); }
    @GetMapping public ResponseEntity<?> listar(){ return ResponseEntity.ok(service.listar()); }
    @GetMapping("/{id}") public ResponseEntity<Reserva> buscar(@PathVariable Long id){ return ResponseEntity.ok(service.buscar(id)); }
}
