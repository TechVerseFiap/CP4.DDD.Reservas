package br.com.fiap.reservas.domain.model;

import br.com.fiap.reservas.domain.exception.MissingReservationDataException;

public final class Professor {

    private final Long id;
    private final String nome;
    private final String email;

    private Professor(Long id, String nome, String email) {
        this.id = id;
        this.nome = requireText(nome, "Nome do professor e obrigatorio");
        this.email = requireText(email, "E-mail do professor e obrigatorio");
    }

    public static Professor criar(String nome, String email) {
        return new Professor(null, nome, email);
    }

    public static Professor reconstituir(Long id, String nome, String email) {
        return new Professor(id, nome, email);
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }

    private static String requireText(String value, String message) {
        if (value == null || value.isBlank()) {
            throw new MissingReservationDataException(message);
        }
        return value.trim();
    }
}
