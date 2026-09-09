package br.com.fiap.reservas.domain.exception;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class InactiveEquipmentException extends DomainException {

    private final Collection<String> equipmentNames;

    public InactiveEquipmentException(Collection<String> equipmentNames) {
        super("inactive-equipment", "Equipamento(s) inativo(s) nao pode(m) ser reservado(s): "
                + equipmentNames.stream().collect(Collectors.joining(", ")));
        this.equipmentNames = List.copyOf(equipmentNames);
    }

    public Collection<String> equipmentNames() {
        return equipmentNames;
    }
}
