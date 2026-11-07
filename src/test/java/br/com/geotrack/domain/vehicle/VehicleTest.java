package br.com.geotrack.domain.vehicle;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import br.com.geotrack.domain.shared.DomainValidationException;
import java.time.Year;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class VehicleTest {

    private static final Renavam RENAVAM = new Renavam("12345678900");
    private static final BrazilianPlate PLATE = new BrazilianPlate("ABC1D23");

    private static Vehicle newVehicle() {
        return Vehicle.register(RENAVAM, PLATE, "FH 540", "Volvo", 2023);
    }

    @Nested
    class Register {

        @Test
        void createsActiveVehicleWithIdentity() {
            var vehicle = newVehicle();

            assertThat(vehicle.id()).isNull();
            assertThat(vehicle.uniqueId()).isNotNull();
            assertThat(vehicle.status()).isEqualTo(VehicleStatus.ACTIVE);
            assertThat(vehicle.createdAt()).isNotNull();
            assertThat(vehicle.updatedAt()).isNull();
        }

        @Test
        void trimsTextFields() {
            var vehicle = Vehicle.register(RENAVAM, PLATE, "  FH 540 ", " Volvo ", 2023);

            assertThat(vehicle.model()).isEqualTo("FH 540");
            assertThat(vehicle.brand()).isEqualTo("Volvo");
        }

        @Test
        void acceptsNextYearAsModelYear() {
            int nextYear = Year.now().getValue() + 1;

            assertThat(Vehicle.register(RENAVAM, PLATE, "FH", "Volvo", nextYear).year())
                    .isEqualTo(nextYear);
        }

        @ParameterizedTest
        @ValueSource(ints = {0, -1, 1899})
        void rejectsYearBefore1900(int year) {
            assertThatThrownBy(() -> Vehicle.register(RENAVAM, PLATE, "FH", "Volvo", year))
                    .isInstanceOf(DomainValidationException.class)
                    .extracting("field")
                    .isEqualTo("year");
        }

        @Test
        void rejectsYearTooFarInTheFuture() {
            int year = Year.now().getValue() + 2;

            assertThatThrownBy(() -> Vehicle.register(RENAVAM, PLATE, "FH", "Volvo", year))
                    .isInstanceOf(DomainValidationException.class);
        }

        @Test
        void rejectsBlankModel() {
            assertThatThrownBy(() -> Vehicle.register(RENAVAM, PLATE, " ", "Volvo", 2023))
                    .isInstanceOf(DomainValidationException.class)
                    .extracting("field")
                    .isEqualTo("model");
        }

        @Test
        void rejectsTooLongBrand() {
            var brand = "x".repeat(51);

            assertThatThrownBy(() -> Vehicle.register(RENAVAM, PLATE, "FH", brand, 2023))
                    .isInstanceOf(DomainValidationException.class)
                    .hasMessageContaining("no máximo 50");
        }
    }

    @Nested
    class Update {

        @Test
        void changesDataAndTouchesUpdatedAt() {
            var vehicle = newVehicle();

            vehicle.update(new Renavam("98765432103"), new BrazilianPlate("BRA2E19"), "Actros", "Mercedes", 2022);

            assertThat(vehicle.renavam().value()).isEqualTo("98765432103");
            assertThat(vehicle.plate().value()).isEqualTo("BRA2E19");
            assertThat(vehicle.model()).isEqualTo("Actros");
            assertThat(vehicle.brand()).isEqualTo("Mercedes");
            assertThat(vehicle.year()).isEqualTo(2022);
            assertThat(vehicle.updatedAt()).isNotNull();
        }

        @Test
        void cannotUpdateDeletedVehicle() {
            var vehicle = newVehicle();
            vehicle.delete();

            assertThatThrownBy(() -> vehicle.update(RENAVAM, PLATE, "FH", "Volvo", 2023))
                    .isInstanceOf(InvalidStatusTransitionException.class);
        }
    }

    @Nested
    class StatusTransitions {

        @Test
        void allowsTransitionsBetweenNonDeletedStatuses() {
            var vehicle = newVehicle();

            vehicle.block();
            assertThat(vehicle.status()).isEqualTo(VehicleStatus.BLOCKED);

            vehicle.inactivate();
            assertThat(vehicle.status()).isEqualTo(VehicleStatus.INACTIVE);

            vehicle.activate();
            assertThat(vehicle.status()).isEqualTo(VehicleStatus.ACTIVE);
        }

        @Test
        void sameStatusTransitionIsIdempotent() {
            var vehicle = newVehicle();

            vehicle.activate();

            assertThat(vehicle.status()).isEqualTo(VehicleStatus.ACTIVE);
            assertThat(vehicle.updatedAt()).isNull();
        }

        @Test
        void deletedIsTerminal() {
            var vehicle = newVehicle();
            vehicle.delete();

            assertThat(vehicle.isDeleted()).isTrue();
            assertThatThrownBy(vehicle::activate)
                    .isInstanceOf(InvalidStatusTransitionException.class)
                    .hasMessageContaining("DELETED");
            assertThatThrownBy(vehicle::delete).isInstanceOf(InvalidStatusTransitionException.class);
        }
    }
}
