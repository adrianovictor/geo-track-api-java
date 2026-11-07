package br.com.geotrack.domain.location;

import br.com.geotrack.domain.shared.DomainValidationException;
import java.math.BigDecimal;
import java.math.RoundingMode;

public record Coordinates(BigDecimal latitude, BigDecimal longitude) {

    private static final int SCALE = 6;
    private static final BigDecimal MAX_LATITUDE = BigDecimal.valueOf(90);
    private static final BigDecimal MAX_LONGITUDE = BigDecimal.valueOf(180);

    public Coordinates {
        latitude = validate(latitude, "latitude", MAX_LATITUDE);
        longitude = validate(longitude, "longitude", MAX_LONGITUDE);
    }

    private static BigDecimal validate(BigDecimal value, String field, BigDecimal limit) {
        if (value == null) {
            throw new DomainValidationException(field, "O campo '%s' é obrigatório.".formatted(field));
        }
        if (value.abs().compareTo(limit) > 0) {
            throw new DomainValidationException(
                    field, "O campo '%s' deve estar entre -%s e %s.".formatted(field, limit, limit));
        }
        return value.setScale(SCALE, RoundingMode.HALF_UP);
    }
}
