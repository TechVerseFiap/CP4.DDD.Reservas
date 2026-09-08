package br.com.fiap.reservas.domain.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "salas")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Sala {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true, length = 30) private String nome;
}
