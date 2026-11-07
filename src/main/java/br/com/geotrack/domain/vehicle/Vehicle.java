package br.com.geotrack.domain.vehicle;

import static br.com.geotrack.domain.shared.Guard.requireNonNull;
import static br.com.geotrack.domain.shared.Guard.requireText;

import java.time.Instant;
import java.time.Year;
import java.util.UUID;

import br.com.geotrack.domain.shared.DomainValidationException;
import br.com.geotrack.domain.shared.InvalidStatusTransitionException;

public final class Vehicle {

    static final int MIN_YEAR = 1900;
    private static final int TEXT_MAX_LENGTH = 50;

    private final Long id;
    private final UUID uniqueId;
    private Renavam renavam;
    private BrazilianPlate plate;
    private String model;
    private String brand;
    private int year;
    private VehicleStatus status;
    private final Long version;
    private final Instant createdAt;
    private Instant updatedAt;

    private Vehicle(
            Long id,
            UUID uniqueId,
            Renavam renavam,
            BrazilianPlate plate,
            String model,
            String brand,
            int year,
            VehicleStatus status,
            Long version,
            Instant createdAt,
            Instant updatedAt) {
        this.id = id;
        this.uniqueId = requireNonNull(uniqueId, "uniqueId");
        this.renavam = requireNonNull(renavam, "renavam");
        this.plate = requireNonNull(plate, "plate");
        this.model = requireText(model, "model", TEXT_MAX_LENGTH);
        this.brand = requireText(brand, "brand", TEXT_MAX_LENGTH);
        this.year = validateYear(year);
        this.status = requireNonNull(status, "status");
        this.version = version;
        this.createdAt = requireNonNull(createdAt, "createdAt");
        this.updatedAt = updatedAt;
    }

    /** Cria um novo veículo, ainda não persistido, com status ativo. */
    public static Vehicle register(Renavam renavam, BrazilianPlate plate, String model, String brand, int year) {
        return new Vehicle(
                null,
                UUID.randomUUID(),
                renavam,
                plate,
                model,
                brand,
                year,
                VehicleStatus.ACTIVE,
                null,
                Instant.now(),
                null);
    }

    /** Reconstrói um veículo a partir do estado persistido. */
    public static Vehicle restore(
            Long id,
            UUID uniqueId,
            Renavam renavam,
            BrazilianPlate plate,
            String model,
            String brand,
            int year,
            VehicleStatus status,
            Long version,
            Instant createdAt,
            Instant updatedAt) {
        return new Vehicle(id, uniqueId, renavam, plate, model, brand, year, status, version, createdAt, updatedAt);
    }

    public void update(Renavam renavam, BrazilianPlate plate, String model, String brand, int year) {
        if (status.isFinal()) {
            throw InvalidStatusTransitionException.cannotUpdate(status);
        }
        this.renavam = requireNonNull(renavam, "renavam");
        this.plate = requireNonNull(plate, "plate");
        this.model = requireText(model, "model", TEXT_MAX_LENGTH);
        this.brand = requireText(brand, "brand", TEXT_MAX_LENGTH);
        this.year = validateYear(year);
        touch();
    }    

    public void activate() {
        changeStatus(VehicleStatus.ACTIVE);
    }

    public void inactivate() {
        changeStatus(VehicleStatus.INACTIVE);
    }

    public void block() {
        changeStatus(VehicleStatus.BLOCKED);
    }

    /** Exclusão lógica: o histórico de posições é preservado. */
    public void delete() {
        changeStatus(VehicleStatus.DELETED);
    }

    public boolean isDeleted() {
        return status.isFinal();
    }

    /** A regra da transição vem do VehicleSdo veículo. */
    private void changeStatus(VehicleStatus target) {
        var next = status.transitionTo(target);
        if (next == status) {
            return; // idempotente: repetir da
        }
        status = next;
        touch();
    }

private void touch() {
        updatedAt = Instant.now();
    }

    private static int validateYear(int year) {
        // O ano-modelo pode ser o seguinte ao corrente, prática comum no mercado brasileiro.
        int maxYear = Year.now().getValue() + 1;
        if (year < MIN_YEAR || year > maxYear) {
            throw new DomainValidationException("year", "O ano deve estar entre %d e %d.".formatted(MIN_YEAR, maxYear));
        }
        return year;
    }

    public Long id() {
        return id;
    }

    public UUID uniqueId() {
        return uniqueId;
    }

    public Renavam renavam() {
        return renavam;
    }

    public BrazilianPlate plate() {
        return plate;
    }

    public String model() {
        return model;
    }

    public String brand() {
        return brand;
    }

    public int year() {
        return year;
    }

    public VehicleStatus status() {
        return status;
    }

    public Long version() {
        return version;
    }

    public Instant createdAt() {
        return createdAt;
    }

    public Instant updatedAt() {
        return updatedAt;
    }    
}
