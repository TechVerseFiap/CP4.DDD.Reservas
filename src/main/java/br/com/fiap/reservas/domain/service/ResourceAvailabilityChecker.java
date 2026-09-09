package br.com.fiap.reservas.domain.service;

import br.com.fiap.reservas.domain.exception.EquipmentUnavailableException;
import br.com.fiap.reservas.domain.exception.RoomUnavailableException;
import br.com.fiap.reservas.domain.model.TimeWindow;

import java.util.Collection;

public final class ResourceAvailabilityChecker implements IResourceAvailabilityChecker {

    @Override
    public void ensureAvailable(
            ResourceType resourceType,
            String resourceName,
            TimeWindow requested,
            Collection<TimeWindow> existingWindows) {
        if (existingWindows != null
                && existingWindows.stream().anyMatch(existing -> TimeWindowOverlapPolicy.overlaps(existing, requested))) {
            if (resourceType == ResourceType.ROOM) {
                throw new RoomUnavailableException(resourceName);
            }
            throw new EquipmentUnavailableException(resourceName);
        }
    }
}
