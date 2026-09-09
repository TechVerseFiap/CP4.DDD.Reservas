package br.com.fiap.reservas.infrastructure.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "reserva_equipamento", uniqueConstraints = @UniqueConstraint(columnNames = {"reserva_id", "equipamento_id"}))
public class ReservaEquipamentoJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "reserva_id", nullable = false)
    private ReservaJpaEntity reserva;

    @Getter
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "equipamento_id", nullable = false)
    private EquipamentoJpaEntity equipamento;

    @Getter
    @Column(nullable = false)
    private int quantidade;

    public ReservaEquipamentoJpaEntity(EquipamentoJpaEntity equipamento, int quantidade) {
        this.equipamento = equipamento;
        this.quantidade = quantidade;
    }

    public void attachTo(ReservaJpaEntity reserva) {
        this.reserva = reserva;
    }
}
