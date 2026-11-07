package br.com.geotrack.domain.shared;

public abstract class DomainException extends RuntimeException {

    public DomainException(String message) {
        super(message);
    }
}
