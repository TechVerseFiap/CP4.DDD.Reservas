package br.com.fiap.reservas.domain.model;

import br.com.fiap.reservas.domain.exception.MissingReservationDataException;

public record Curso(String nome) {

    public Curso {
        if (nome == null || nome.isBlank()) {
            throw new MissingReservationDataException("Curso e obrigatorio");
        }
        nome = nome.trim();
    }
}
