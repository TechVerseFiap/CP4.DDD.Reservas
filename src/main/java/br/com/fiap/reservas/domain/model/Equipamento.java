package br.com.fiap.reservas.domain.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "equipamentos")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Equipamento {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 120)
    private String nome;

    @Column(nullable = false, length = 80)
    private String tipo;

    @Column(nullable = false)
    private Boolean ativo = true;
}
