package br.com.fiap.reservas.model;

import jakarta.persistence.*;

@Entity
@Table(name = "equipamentos")
public class Equipamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 120)
    private String nome;

    @Column(nullable = false, length = 80)
    private String tipo;

    @Column(nullable = false)
    private boolean ativo;

    protected Equipamento() {
    }

    private Equipamento(String nome, String tipo, boolean ativo) {
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("Nome do equipamento é obrigatório");
        }
        if (tipo == null || tipo.isBlank()) {
            throw new IllegalArgumentException("Tipo do equipamento é obrigatório");
        }
        this.nome = nome.trim();
        this.tipo = tipo.trim();
        this.ativo = ativo;
    }

    public static Equipamento criar(String nome, String tipo, boolean ativo) {
        return new Equipamento(nome, tipo, ativo);
    }

    public void ativar() { this.ativo = true; }
    public void desativar() { this.ativo = false; }

    public Long getId() { return id; }
    public String getNome() { return nome; }
    public String getTipo() { return tipo; }
    public boolean isAtivo() { return ativo; }
}
