package br.com.fiap.reservas.infrastructure.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "equipamentos")
public class EquipamentoJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 120)
    private String nome;

    @Column(nullable = false, length = 80)
    private String tipo;

    @Setter
    @Column(nullable = false)
    private boolean ativo;

    public EquipamentoJpaEntity(String nome, String tipo, boolean ativo) {
        this.nome = nome;
        this.tipo = tipo;
        this.ativo = ativo;
    }
}
