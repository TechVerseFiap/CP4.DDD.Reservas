package br.com.fiap.reservas.infrastructure.persistence.repository;

import java.time.LocalDateTime;

public interface ITimeWindowProjection {

    LocalDateTime getPickup();

    LocalDateTime getReturnTime();
}
