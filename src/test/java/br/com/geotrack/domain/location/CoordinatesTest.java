package br.com.geotrack.domain.location;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import br.com.geotrack.domain.shared.DomainValidationException;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class CoordinatesTest {

    @Test
    void roundsToSixDecimalPlaces() {
        var coordinates = new Coordinates(new BigDecimal("-23.55052012"), new BigDecimal("-46.6333"));

        assertThat(coordinates.latitude()).isEqualByComparingTo("-23.550520");
        assertThat(coordinates.latitude().scale()).isEqualTo(6);
        assertThat(coordinates.longitude()).isEqualByComparingTo("-46.633300");
    }

    @ParameterizedTest
    @CsvSource({"90, 180", "-90, -180", "0, 0"})
    void acceptsBoundaries(String lat, String lon) {
        var coordinates = new Coordinates(new BigDecimal(lat), new BigDecimal(lon));

        assertThat(coordinates.latitude()).isEqualByComparingTo(lat);
    }

    @ParameterizedTest
    @CsvSource({"90.000001, 0, latitude", "-91, 0, latitude", "0, 180.1, longitude", "0, -181, longitude"})
    void rejectsOutOfRange(String lat, String lon, String field) {
        assertThatThrownBy(() -> new Coordinates(new BigDecimal(lat), new BigDecimal(lon)))
                .isInstanceOf(DomainValidationException.class)
                .extracting("field")
                .isEqualTo(field);
    }

    @Test
    void rejectsNull() {
        assertThatThrownBy(() -> new Coordinates(null, BigDecimal.ONE)).isInstanceOf(DomainValidationException.class);
    }
}
