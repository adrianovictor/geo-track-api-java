package br.com.geotrack.domain.shared;

public final class Guard {

    private Guard() {}

    public static String requireText(String value, String field, int maxLength) {
        if (value == null || value.isBlank()) {
            throw new DomainValidationException(field, "O campo %s é obrigatório".formatted(field));
        }

        var trimmed = value.strip();
        if (trimmed.length() > maxLength) {
            throw new DomainValidationException(
                    field, "O campo %s deve ter no máximo %d caracteres".formatted(field, maxLength));
        }

        return trimmed;
    }

    public static <T> T requireNonNull(T value, String field) {
        if (value == null) {
            throw new DomainValidationException(field, "O campo %s é obrigatório".formatted(field));
        }

        return value;
    }
}
