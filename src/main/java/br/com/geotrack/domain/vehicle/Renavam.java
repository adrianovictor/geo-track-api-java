package br.com.geotrack.domain.vehicle;

import br.com.geotrack.domain.shared.DomainValidationException;

public record Renavam(String value) {

    private static final int LENGTH = 11;
    private static final int[] WEIGHTS = {3, 2, 9, 8, 7, 6, 5, 4, 3, 2};

    public Renavam {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("O valor do RENAVAM não pode ser nulo ou vazio");
        }

        value = value.strip();
        if (!value.matches("\\d{9,11}")) {
            throw new DomainValidationException("renavam", "O RENAVAM deve conter entre 9 e 11 dígitos numéricos.");
        }
        value = "0".repeat(LENGTH - value.length()) + value;
        if (!hasValidCheckDigit(value)) {
            throw new DomainValidationException(
                    "renavam", "O RENAVAM '%s' possui dígito verificador inválido.".formatted(value));
        }
    }

    private static boolean hasValidCheckDigit(String renavam) {
        int sum = 0;
        for (int i = 0; i < WEIGHTS.length; i++) {
            sum += Character.getNumericValue(renavam.charAt(i)) * WEIGHTS[i];
        }
        int digit = (sum * 10) % 11;
        if (digit == 10) {
            digit = 0;
        }
        return digit == Character.getNumericValue(renavam.charAt(LENGTH - 1));
    }

    @Override
    public String toString() {
        return value;
    }
}
