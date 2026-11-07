package br.com.geotrack.domain.vehicle;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import br.com.geotrack.domain.shared.DomainValidationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class RenavamTest {

    @ParameterizedTest
    @ValueSource(strings = {"12345678900", "98765432103", "45678912300", " 12345678900 "})
    void acceptsValidRenavam(String input) {
        assertThat(new Renavam(input).value()).hasSize(11).containsOnlyDigits();
    }

    @Test
    void padsLegacyNineDigitRenavamWithZeros() {
        // 00639884962: RENAVAM antigo de 9 dígitos com zeros à esquerda
        assertThat(new Renavam("639884962").value()).isEqualTo("00639884962");
    }

    @Test
    void rejectsWrongCheckDigit() {
        assertThatThrownBy(() -> new Renavam("12345678901"))
                .isInstanceOf(DomainValidationException.class)
                .hasMessageContaining("dígito verificador inválido");
    }

    @ParameterizedTest
    @ValueSource(strings = {"12345678", "123456789012", "1234567890A", "1234-567890"})
    void rejectsMalformedRenavam(String input) {
        assertThatThrownBy(() -> new Renavam(input))
                .isInstanceOf(DomainValidationException.class)
                .hasMessageContaining("entre 9 e 11 dígitos");
    }
}