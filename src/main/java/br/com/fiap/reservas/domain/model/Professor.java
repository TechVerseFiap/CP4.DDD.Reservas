package br.com.fiap.reservas.domain.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "professores")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Professor {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length = 120) private String nome;
    @Column(nullable = false, unique = true, length = 160) private String email;
}
