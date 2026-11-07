package br.com.geotrack.domain.shared;

public class DomainValidationException extends DomainException {

    private final String field;

    public DomainValidationException(String field,String message) {        
        super(message);
        this.field = field;
    }
    
    public String Field() {
        return field;
    }
}
