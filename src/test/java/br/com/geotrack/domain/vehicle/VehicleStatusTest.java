package br.com.geotrack.domain.vehicle;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import br.com.geotrack.domain.shared.InvalidStatusTransitionException;

class VehicleStatusTest {

    @ParameterizedTest
    @EnumSource(value = VehicleStatus.class, names = "DELETED", mode = EnumSource.Mode.EXCLUDE)
    void nonFinalStatusesCanGoAnywhere(VehicleStatus from) {
        for (var target : VehicleStatus.values()) {
            assertThat(from.transitionTo(target)).isEqualTo(target);
        }
    }

    @ParameterizedTest
    @EnumSource(VehicleStatus.class)
    void deletedIsFinal(VehicleStatus target) {
        assertThatThrownBy(() -> VehicleStatus.DELETED.transitionTo(target))
                .isInstanceOf(InvalidStatusTransitionException.class);
    }

    @Test
    void onlyDeletedIsFinal() {
        assertThat(VehicleStatus.DELETED.isFinal()).isTrue();
        assertThat(VehicleStatus.ACTIVE.isFinal()).isFalse();
        assertThat(VehicleStatus.INACTIVE.isFinal()).isFalse();
        assertThat(VehicleStatus.BLOCKED.isFinal()).isFalse();
    }
}
