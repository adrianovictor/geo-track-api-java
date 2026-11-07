package br.com.geotrack.domain.vehicle;

import br.com.geotrack.domain.shared.DomainException;

public class VehicleAlreadyExistsException extends DomainException {

    private VehicleAlreadyExistsException(String message) {
        super(message);
    }

    public static VehicleAlreadyExistsException withRenavam(Renavam renavam) {
        return new VehicleAlreadyExistsException("Veículo com RENAVAM %s já existe.".formatted(renavam));
    }

    public static VehicleAlreadyExistsException withPlate(BrazilianPlate plate) {
        return new VehicleAlreadyExistsException("Veículo com placa %s já existe.".formatted(plate));
    }
}
