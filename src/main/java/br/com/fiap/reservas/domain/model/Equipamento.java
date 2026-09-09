package br.com.fiap.reservas.domain.model;

import br.com.fiap.reservas.domain.exception.MissingReservationDataException;

public final class Equipamento {

    private final Long id;
    private final String nome;
    private final String tipo;
    private boolean ativo;

    private Equipamento(Long id, String nome, String tipo, boolean ativo) {
        if (nome == null || nome.isBlank()) {
            throw new MissingReservationDataException("Nome do equipamento e obrigatorio");
        }
        if (tipo == null || tipo.isBlank()) {
            throw new MissingReservationDataException("Tipo do equipamento e obrigatorio");
        }
        this.id = id;
        this.nome = nome.trim();
        this.tipo = tipo.trim();
        this.ativo = ativo;
    }

    public static Equipamento criar(String nome, String tipo, boolean ativo) {
        return new Equipamento(null, nome, tipo, ativo);
    }

    public static Equipamento reconstituir(Long id, String nome, String tipo, boolean ativo) {
        return new Equipamento(id, nome, tipo, ativo);
    }

    public void ativar() {
        ativo = true;
    }

    public void desativar() {
        ativo = false;
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getTipo() {
        return tipo;
    }

    public boolean isAtivo() {
        return ativo;
    }
}
