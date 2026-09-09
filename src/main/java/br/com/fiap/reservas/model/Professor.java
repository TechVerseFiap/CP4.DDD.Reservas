package br.com.fiap.reservas.model;

import jakarta.persistence.*;

@Entity
@Table(name = "professores")
public class Professor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 120)
    private String nome;

    @Column(nullable = false, unique = true, length = 160)
    private String email;

    protected Professor() {
    }

    private Professor(String nome, String email) {
        this.nome = requireText(nome, "Nome do professor é obrigatório");
        this.email = requireText(email, "E-mail do professor é obrigatório");
    }

    public static Professor criar(String nome, String email) {
        return new Professor(nome.trim(), email.trim());
    }

    public Long getId() { return id; }
    public String getNome() { return nome; }
    public String getEmail() { return email; }

    private static String requireText(String value, String message) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(message);
        }
        return value;
    }
}
