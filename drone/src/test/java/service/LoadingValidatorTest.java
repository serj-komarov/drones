package service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.testcontainers.shaded.org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;

import com.musala.drone.exception.BusinessException;
import com.musala.drone.model.entity.Drone;
import com.musala.drone.model.entity.Medication;
import com.musala.drone.model.enums.DroneModel;
import com.musala.drone.model.enums.DroneState;
import com.musala.drone.validators.LoadingValidator;
import config.ApplicationTest;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@RequiredArgsConstructor
public class LoadingValidatorTest extends ApplicationTest {
    @Autowired
    private LoadingValidator loadingValidator;

    @Test
    @DisplayName("When drone don't have enough free space for loading, an error occurs")
    void validateLoadingToDrone() {
        var randomSerialNumber = randomAlphanumeric(50);
        var drone = Drone.builder()
            .serialNumber(randomSerialNumber)
            .batteryCapacity(100)
            .medications(new ArrayList<>())
            .model(DroneModel.LIGHT_WEIGHT)
            .weightLimit(100)
            .state(DroneState.LOADING)
            .build();
        var medications = List.of(
            Medication.builder().image(null).weight(500).code("TEST_CODE").name("BARALGIN").build());

        var error = assertThrows(BusinessException.class,
            () -> loadingValidator.validateLoading(drone, medications));

        assertEquals(error.getMessage(), String.format(
            "Can't load to drone with serial number: '%s'! Overweight is '400'. Try to use another drone or reduce weight",
            randomSerialNumber));
    }

    @Test
    @DisplayName("When drone don't have enough battery capacity for loading, an error occurs")
    void loadToLowBatteryToDrone() {
        var randomSerialNumber = randomAlphanumeric(50);
        var drone = Drone.builder()
            .serialNumber(randomSerialNumber)
            .batteryCapacity(20)
            .medications(new ArrayList<>())
            .model(DroneModel.LIGHT_WEIGHT)
            .weightLimit(100)
            .state(DroneState.LOADING)
            .build();
        var medications = List.of(
            Medication.builder().image(null).weight(50).code("TEST-CODE").name("BARALGIN").build());

        var error = assertThrows(BusinessException.class,
            () -> loadingValidator.validateLoading(drone, medications));

        Assertions.assertEquals(error.getMessage(),
            String.format(
                "Can't load to drone with serial number: '%s' battery level is '%s' percent! it's to low, try to use another drone",
                randomSerialNumber, "20"));
    }

}
