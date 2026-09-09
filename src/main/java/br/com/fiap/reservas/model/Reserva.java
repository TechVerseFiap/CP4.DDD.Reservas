package br.com.fiap.reservas.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "reservas")
public class Reserva {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
    @JoinTable(
        name = "reserva_equipamento",
        joinColumns = @JoinColumn(name = "reserva_id"),
        inverseJoinColumns = @JoinColumn(name = "equipamento_id")
    )
    private Set<Equipamento> equipamentos = new HashSet<>();

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ReservaStatus status;

    protected Reserva() {
    }

    private Reserva(
            Professor professor,
            String curso,
            Sala sala,
            LocalDateTime retirada,
            LocalDateTime entrega,
            Set<Equipamento> equipamentos) {

        if (professor == null) throw new IllegalArgumentException("Professor é obrigatório");
        if (curso == null || curso.isBlank()) throw new IllegalArgumentException("Curso é obrigatório");
        if (sala == null) throw new IllegalArgumentException("Sala é obrigatória");
        if (retirada == null || entrega == null) throw new IllegalArgumentException("Horários são obrigatórios");
        if (!retirada.isBefore(entrega)) {
            throw new IllegalArgumentException("Horário de retirada deve ser anterior ao horário de entrega");
        }
        if (equipamentos == null || equipamentos.isEmpty()) {
            throw new IllegalArgumentException("Ao menos um equipamento deve ser reservado");
        }

        this.professor = professor;
        this.curso = curso.trim();
        this.sala = sala;
        this.retirada = retirada;
        this.entrega = entrega;
        this.equipamentos = new HashSet<>(equipamentos);
        this.status = ReservaStatus.CONFIRMADA;
    }

    public static Reserva criar(
            Professor professor,
            String curso,
            Sala sala,
            LocalDateTime retirada,
            LocalDateTime entrega,
            Set<Equipamento> equipamentos) {
        return new Reserva(professor, curso, sala, retirada, entrega, equipamentos);
    }

    public Long getId() { return id; }
    public Professor getProfessor() { return professor; }
    public String getCurso() { return curso; }
    public Sala getSala() { return sala; }
    public LocalDateTime getRetirada() { return retirada; }
    public LocalDateTime getEntrega() { return entrega; }
    public Set<Equipamento> getEquipamentos() { return Set.copyOf(equipamentos); }
    public ReservaStatus getStatus() { return status; }
}
