package br.com.geotrack.domain.vehicle;

import br.com.geotrack.domain.shared.DomainException;

/** Lançada quando o status atual do veículo não permite a operação pedida. */
public class InvalidStatusTransitionException extends DomainException {

    private InvalidStatusTransitionException(String message) {
        super(message);
    }

    /** Mudança de status não permitida (ex.: excluído → ativo). */
    public static InvalidStatusTransitionException of(VehicleStatus from, VehicleStatus to) {
        return new InvalidStatusTransitionException(
                "Operação não permitida: veículo com status %s não pode ir para %s.".formatted(from, to));
    }

    /** Alteração de dados de um veículo cujo status não permite mais mudanças. */
    public static InvalidStatusTransitionException cannotUpdate(VehicleStatus current) {
        return new InvalidStatusTransitionException(
                "Operação não permitida: veículo com status %s não pode ser alterado.".formatted(current));
    }
}
