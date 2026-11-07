package br.com.geotrack.domain.vehicle;

import java.util.Arrays;

public enum VehicleStatus {
    ACTIVE(1),
    INACTIVE(2),
    BLOCKED(3),
    DELETED(4);

    private final int code;

    VehicleStatus(int code) {
        this.code = code;
    }

    public int code() {
        return code;
    }

    public static VehicleStatus fromCode(int code) {
        return Arrays.stream(values())
                .filter(status -> status.code == code)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Código de status de veículo inválido: " + code));
    }

    public boolean isFinal() {
        return this == DELETED;
    }

    public VehicleStatus transitionTo(VehicleStatus target) {
        if (isFinal()) {
            throw InvalidStatusTransitionException.of(this, target);
        }

        return target;
    }
}
