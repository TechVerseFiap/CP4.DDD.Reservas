package br.com.fiap.reservas.domain.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.*;

@Entity
@Table(name = "reservas")
@Getter @NoArgsConstructor
public class Reserva {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Professor professor;

    @Column(nullable = false, length = 120)
    private String curso;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Sala sala;

    @Column(nullable = false)
    private LocalDateTime retirada;

    @Column(nullable = false)
    private LocalDateTime entrega;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "reserva_equipamento",
        joinColumns = @JoinColumn(name = "reserva_id"),
        inverseJoinColumns = @JoinColumn(name = "equipamento_id"))
    private Set<Equipamento> equipamentos = new HashSet<>();

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ReservaStatus status;

    public Reserva(Professor professor, String curso, Sala sala,
                   LocalDateTime retirada, LocalDateTime entrega,
                   Set<Equipamento> equipamentos) {
        if (professor == null) throw new IllegalArgumentException("Professor é obrigatório");
        if (curso == null || curso.isBlank()) throw new IllegalArgumentException("Curso é obrigatório");
        if (sala == null) throw new IllegalArgumentException("Sala é obrigatória");
        if (retirada == null || entrega == null) throw new IllegalArgumentException("Horários são obrigatórios");
        if (!retirada.isBefore(entrega)) throw new IllegalArgumentException("Horário de retirada deve ser anterior ao horário de entrega");
        if (equipamentos == null || equipamentos.isEmpty()) throw new IllegalArgumentException("Ao menos um equipamento deve ser reservado");
        this.professor = professor;
        this.curso = curso.trim();
        this.sala = sala;
        this.retirada = retirada;
        this.entrega = entrega;
        this.equipamentos = new HashSet<>(equipamentos);
        this.status = ReservaStatus.CONFIRMADA;
    }
}
