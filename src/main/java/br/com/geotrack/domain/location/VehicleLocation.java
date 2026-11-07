package br.com.geotrack.domain.location;

import static br.com.geotrack.domain.shared.Guard.requireNonNull;

import java.time.Instant;
import java.time.LocalDateTime;

/**
 * Posição GPS reportada por um veículo.
 *
 * <p>É um agregado próprio que referencia o veículo apenas pelo id: um veículo pode acumular milhões de
 * posições, então carregá-las junto do veículo (como no projeto original) não escala.
 *
 * @param positionId identificador da posição no equipamento rastreador; único por veículo
 * @param recordedAt data/hora local informada pelo equipamento
 * @param recordedAtUtc o mesmo instante em UTC
 */
public record VehicleLocation(
        Long id,
        Long vehicleId,
        Long positionId,
        Coordinates coordinates,
        LocalDateTime recordedAt,
        Instant recordedAtUtc,
        Instant createdAt) {

    public VehicleLocation {
        requireNonNull(vehicleId, "vehicleId");
        requireNonNull(positionId, "positionId");
        requireNonNull(coordinates, "coordinates");
        requireNonNull(recordedAt, "date");
        requireNonNull(recordedAtUtc, "dateUtc");
        requireNonNull(createdAt, "createdAt");
    }

    public static VehicleLocation record(
            Long vehicleId, Long positionId, Coordinates coordinates, LocalDateTime recordedAt, Instant recordedAtUtc) {
        return new VehicleLocation(null, vehicleId, positionId, coordinates, recordedAt, recordedAtUtc, Instant.now());
    }
}
