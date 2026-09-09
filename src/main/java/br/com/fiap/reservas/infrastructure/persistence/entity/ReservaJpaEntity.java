package br.com.fiap.reservas.infrastructure.persistence.entity;

import br.com.fiap.reservas.domain.model.ReservaStatus;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "reservas")
public class ReservaJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "professor_id", nullable = false)
    private ProfessorJpaEntity professor;

    @Column(nullable = false, length = 120)
    private String curso;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "sala_id", nullable = false)
    private SalaJpaEntity sala;

    @Column(nullable = false)
    private LocalDateTime retirada;

    @Column(nullable = false)
    private LocalDateTime entrega;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ReservaStatus status;

    @OneToMany(mappedBy = "reserva", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ReservaEquipamentoJpaEntity> equipamentos = new LinkedHashSet<>();

    public ReservaJpaEntity(
            ProfessorJpaEntity professor,
            String curso,
            SalaJpaEntity sala,
            LocalDateTime retirada,
            LocalDateTime entrega,
            ReservaStatus status
    ) {
        this.professor = professor;
        this.curso = curso;
        this.sala = sala;
        this.retirada = retirada;
        this.entrega = entrega;
        this.status = status;
    }

    public void replaceEquipment(Set<ReservaEquipamentoJpaEntity> equipment) {
        equipamentos.clear();
        equipment.forEach(item -> {
            item.attachTo(this);
            equipamentos.add(item);
        });
    }

    public void update(
            ProfessorJpaEntity professor,
            String curso,
            SalaJpaEntity sala,
            LocalDateTime retirada,
            LocalDateTime entrega,
            ReservaStatus status,
            Set<ReservaEquipamentoJpaEntity> equipment
    ) {
        this.professor = professor;
        this.curso = curso;
        this.sala = sala;
        this.retirada = retirada;
        this.entrega = entrega;
        this.status = status;
        replaceEquipment(equipment);
    }

    public void updateStatus(ReservaStatus status) {
        this.status = status;
    }
}
