package br.com.fiap.reservas.domain.model;

import br.com.fiap.reservas.domain.exception.MissingReservationDataException;

public final class Sala {

    private final Long id;
    private final String nome;

    private Sala(Long id, String nome) {
        if (nome == null || nome.isBlank()) {
            throw new MissingReservationDataException("Nome da sala e obrigatorio");
        }
        this.id = id;
        this.nome = nome.trim();
    }

    public static Sala criar(String nome) {
        return new Sala(null, nome);
    }

    public static Sala reconstituir(Long id, String nome) {
        return new Sala(id, nome);
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }
}
