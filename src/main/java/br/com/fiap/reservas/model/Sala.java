package br.com.fiap.reservas.model;

import jakarta.persistence.*;

@Entity
@Table(name = "salas")
public class Sala {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 30)
    private String nome;

    protected Sala() {
    }

    private Sala(String nome) {
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("Nome da sala é obrigatório");
        }
        this.nome = nome.trim();
    }

    public static Sala criar(String nome) {
        return new Sala(nome);
    }

    public Long getId() { return id; }
    public String getNome() { return nome; }
}
