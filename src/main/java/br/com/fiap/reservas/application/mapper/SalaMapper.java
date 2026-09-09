package br.com.fiap.reservas.application.mapper;

import br.com.fiap.reservas.application.dto.request.SalaRequest;
import br.com.fiap.reservas.application.dto.response.SalaResponse;
import br.com.fiap.reservas.domain.model.Sala;

public final class SalaMapper {

    private SalaMapper() {
    }

    public static Sala toDomain(SalaRequest request) {
        return Sala.criar(request.nome());
    }

    public static SalaResponse toResponse(Sala sala) {
        return new SalaResponse(sala.getId(), sala.getNome());
    }
}
