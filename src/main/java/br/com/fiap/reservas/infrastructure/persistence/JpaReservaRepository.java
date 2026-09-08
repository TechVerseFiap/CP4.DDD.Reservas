package br.com.fiap.reservas.infrastructure.persistence;
import br.com.fiap.reservas.domain.model.Reserva;
import br.com.fiap.reservas.domain.repository.ReservaRepository;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import java.time.LocalDateTime;
public interface JpaReservaRepository extends JpaRepository<Reserva,Long>, ReservaRepository {
    @Override
    @Query("select count(r) > 0 from Reserva r join r.equipamentos e where e.id = :equipamentoId and r.retirada < :entrega and r.entrega > :retirada")
    boolean existeConflitoEquipamento(@Param("equipamentoId") Long equipamentoId,@Param("retirada") LocalDateTime retirada,@Param("entrega") LocalDateTime entrega);
    @Override
    @Query("select count(r) > 0 from Reserva r where r.sala.id = :salaId and r.retirada < :entrega and r.entrega > :retirada")
    boolean existeConflitoSala(@Param("salaId") Long salaId,@Param("retirada") LocalDateTime retirada,@Param("entrega") LocalDateTime entrega);
}
