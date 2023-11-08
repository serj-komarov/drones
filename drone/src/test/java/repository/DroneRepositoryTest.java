package repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.musala.drone.model.entity.Drone;
import com.musala.drone.model.enums.DroneModel;
import com.musala.drone.model.enums.DroneState;
import com.musala.drone.repository.DroneRepository;
import config.ApplicationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.TransactionSystemException;


public class DroneRepositoryTest extends ApplicationTest {
    @Autowired
    DroneRepository droneRepository;

    @ParameterizedTest
    @ValueSource(ints = {-100, 600, 0})
    @DisplayName("Validate constraints for field 'weightLimit' entity Drone")
    public void saveDroneBadMaxWeight(Integer weight) {
        var error = assertThrows(TransactionSystemException.class,
            () -> droneRepository.save(
                Drone.builder().serialNumber("TEST_01").weightLimit(weight).state(DroneState.IDLE)
                    .model(DroneModel.LIGHT_WEIGHT).build()));

        assertThat(error.getCause().getCause().getMessage()).containsAnyOf(
            "must be greater than 0", "must be less than or equal to 500");
    }

}
