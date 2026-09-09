package br.com.fiap.reservas.domain.exception;

public class EquipmentUnavailableException extends DomainException {

    public EquipmentUnavailableException(String equipmentName) {
        super("equipment-unavailable", "Equipamento " + equipmentName
                + " ja esta reservado no periodo solicitado");
    }
}
