package br.com.geotrack.domain.vehicle;

import br.com.geotrack.domain.shared.DomainException;

public class VehicleNotFoundException extends DomainException {

    public VehicleNotFoundException(Long id) {
        super("Veículo com ID %s não encontrado.".formatted(id));
    }
}
