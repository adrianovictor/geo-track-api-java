package br.com.geotrack.domain.vehicle;

import br.com.geotrack.domain.shared.DomainValidationException;
import java.util.Locale;
import java.util.regex.Pattern;

public record BrazilianPlate(String value) {

    private static final Pattern OLD_FORMAT = Pattern.compile("^[A-Z]{3}[0-9]{4}$");
    private static final Pattern MERCOSUL_FORMAT = Pattern.compile("^[A-Z]{3}[0-9][A-Z][0-9]{2}$");

    public BrazilianPlate {
        if (value == null || value.isBlank()) {
            throw new DomainValidationException("plate", "A placa é obrigatória.");
        }

        value = normalize(value);
        if (!OLD_FORMAT.matcher(value).matches()
                && !MERCOSUL_FORMAT.matcher(value).matches()) {
            throw new DomainValidationException(
                    "plate", "A placa '%s' não é uma placa brasileira válida.".formatted(value));
        }
    }

    public static String normalize(String value) {
        return value.replaceAll("[\\s-]", "").toUpperCase(Locale.ROOT);
    }

    public boolean isMercosul() {
        return MERCOSUL_FORMAT.matcher(value).matches();
    }

    @Override
    public String toString() {
        return value;
    }
}
