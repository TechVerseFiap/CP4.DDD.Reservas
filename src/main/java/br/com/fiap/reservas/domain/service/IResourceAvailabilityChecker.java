package br.com.fiap.reservas.domain.service;

import br.com.fiap.reservas.domain.model.TimeWindow;

import java.util.Collection;

public interface IResourceAvailabilityChecker {

    void ensureAvailable(
            ResourceType resourceType,
            String resourceName,
            TimeWindow requested,
            Collection<TimeWindow> existingWindows);
}
