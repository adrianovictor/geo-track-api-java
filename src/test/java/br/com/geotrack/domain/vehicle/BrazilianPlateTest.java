package br.com.geotrack.domain.vehicle;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import br.com.geotrack.domain.shared.DomainValidationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

class BrazilianPlateTest {

    @ParameterizedTest
    @CsvSource({"ABC1234, ABC1234", "abc-1234, ABC1234", "ABC 1234, ABC1234", "ABC1D23, ABC1D23", "bra2e19, BRA2E19"})
    void normalizesValidPlates(String input, String expected) {
        assertThat(new BrazilianPlate(input).value()).isEqualTo(expected);
    }

    @ParameterizedTest
    @ValueSource(strings = {"AB1234", "ABCD123", "1BC1234", "ABC12345", "ABC1DD3", "ÁBC1234"})
    void rejectsInvalidPlates(String input) {
        assertThatThrownBy(() -> new BrazilianPlate(input))
                .isInstanceOf(DomainValidationException.class)
                .hasMessageContaining("não é uma placa brasileira válida");
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = "   ")
    void rejectsBlankPlates(String input) {
        assertThatThrownBy(() -> new BrazilianPlate(input))
                .isInstanceOf(DomainValidationException.class)
                .hasMessage("A placa é obrigatória.");
    }

    @Test
    void sameNormalizedValueMeansSamePlate() {
        assertThat(new BrazilianPlate("abc-1234")).isEqualTo(new BrazilianPlate("ABC1234"));
    }

    @Test
    void detectsMercosulFormat() {
        assertThat(new BrazilianPlate("ABC1D23").isMercosul()).isTrue();
        assertThat(new BrazilianPlate("ABC1234").isMercosul()).isFalse();
    }
}
