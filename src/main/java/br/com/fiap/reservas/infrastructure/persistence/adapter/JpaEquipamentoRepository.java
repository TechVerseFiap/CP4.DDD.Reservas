package br.com.fiap.reservas.infrastructure.persistence.adapter;

import br.com.fiap.reservas.domain.model.Equipamento;
import br.com.fiap.reservas.domain.repository.IEquipamentoRepository;
import br.com.fiap.reservas.infrastructure.persistence.entity.EquipamentoJpaEntity;
import br.com.fiap.reservas.infrastructure.persistence.repository.IEquipamentoJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class JpaEquipamentoRepository implements IEquipamentoRepository {

    private final IEquipamentoJpaRepository repository;

    public JpaEquipamentoRepository(IEquipamentoJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    public Equipamento save(Equipamento equipamento) {
        EquipamentoJpaEntity entity = toEntity(equipamento);
        EquipamentoJpaEntity saved = repository.save(entity);
        return toDomain(saved);
    }

    @Override
    public Optional<Equipamento> findById(Long id) {
        return repository.findById(id).map(this::toDomain);
    }

    @Override
    public List<Equipamento> findAll() {
        return repository.findAll().stream().map(this::toDomain).toList();
    }

    private EquipamentoJpaEntity toEntity(Equipamento equipamento) {
        if (equipamento.getId() == null) {
            return new EquipamentoJpaEntity(equipamento.getNome(), equipamento.getTipo(), equipamento.isAtivo());
        }
        EquipamentoJpaEntity entity = repository.findById(equipamento.getId())
                .orElseGet(() -> new EquipamentoJpaEntity(
                        equipamento.getNome(), equipamento.getTipo(), equipamento.isAtivo()));
        entity.setAtivo(equipamento.isAtivo());
        return entity;
    }

    private Equipamento toDomain(EquipamentoJpaEntity equipamento) {
        return Equipamento.reconstituir(
                equipamento.getId(), equipamento.getNome(), equipamento.getTipo(), equipamento.isAtivo());
    }
}
