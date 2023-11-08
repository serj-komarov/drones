package repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.musala.drone.model.entity.Medication;
import com.musala.drone.repository.MedicationRepository;
import config.ApplicationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.TransactionSystemException;


public class MedicationRepositoryTest extends ApplicationTest {
    @Autowired
    MedicationRepository medicationRepository;

    @ParameterizedTest
    @ValueSource(strings = {"TEST-01", "test", "test_01"})
    @DisplayName("Validate constraints for field 'code' entity medications")
    public void saveMedicationBadCode(String code) {
        var error = assertThrows(TransactionSystemException.class,
            () -> medicationRepository.save(
                Medication.builder().code(code).weight(0).image(null).name("TEST").build()));

        assertThat(error.getCause().getCause().getMessage()).contains(
            "allowed only upper case letters, underscore and numbers");
    }

    @ParameterizedTest
    @ValueSource(strings = {"!!TEST-01", "test~", "test@01"})
    @DisplayName("Validate constraints for field 'name' entity medications")
    public void saveMedicationBadName(String name) {
        var error = assertThrows(TransactionSystemException.class,
            () -> medicationRepository.save(
                Medication.builder().code("TEST_01").weight(0).image(null).name(name).build()));

        assertThat(error.getCause().getCause().getMessage()).contains(
            "allowed only letters, numbers, ‘-‘, ‘_’");
    }
}
