package br.com.fiap.reservas.infrastructure.persistence.repository;

import br.com.fiap.reservas.infrastructure.persistence.entity.ReservaJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface IReservaJpaRepository extends JpaRepository<ReservaJpaEntity, Long> {

    @Query("""
            select r.retirada as pickup, r.entrega as returnTime
            from ReservaJpaEntity r
            join r.equipamentos item
            where item.equipamento.id = :equipmentId
              and r.status = br.com.fiap.reservas.domain.model.ReservaStatus.CONFIRMADA
              and r.retirada < :dayEnd
              and r.entrega > :dayStart
            """)
    List<ITimeWindowProjection> findEquipmentWindows(
            @Param("equipmentId") Long equipmentId,
            @Param("dayStart") LocalDateTime dayStart,
            @Param("dayEnd") LocalDateTime dayEnd);

    @Query("""
            select r.retirada as pickup, r.entrega as returnTime
            from ReservaJpaEntity r
            where r.sala.id = :roomId
              and r.status = br.com.fiap.reservas.domain.model.ReservaStatus.CONFIRMADA
              and r.retirada < :dayEnd
              and r.entrega > :dayStart
            """)
    List<ITimeWindowProjection> findRoomWindows(
            @Param("roomId") Long roomId,
            @Param("dayStart") LocalDateTime dayStart,
            @Param("dayEnd") LocalDateTime dayEnd);
}
