package br.com.fiap.reservas.infrastructure.persistence.adapter;

import br.com.fiap.reservas.domain.model.Sala;
import br.com.fiap.reservas.domain.repository.ISalaRepository;
import br.com.fiap.reservas.infrastructure.persistence.entity.SalaJpaEntity;
import br.com.fiap.reservas.infrastructure.persistence.repository.ISalaJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class JpaSalaRepository implements ISalaRepository {

    private final ISalaJpaRepository repository;

    public JpaSalaRepository(ISalaJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    public Sala save(Sala sala) {
        SalaJpaEntity saved = repository.save(toEntity(sala));
        return toDomain(saved);
    }

    @Override
    public Optional<Sala> findById(Long id) {
        return repository.findById(id).map(this::toDomain);
    }

    @Override
    public List<Sala> findAll() {
        return repository.findAll().stream().map(this::toDomain).toList();
    }

    private SalaJpaEntity toEntity(Sala sala) {
        if (sala.getId() == null) {
            return new SalaJpaEntity(sala.getNome());
        }
        return repository.findById(sala.getId())
                .orElseGet(() -> new SalaJpaEntity(sala.getNome()));
    }

    private Sala toDomain(SalaJpaEntity sala) {
        return Sala.reconstituir(sala.getId(), sala.getNome());
    }
}
