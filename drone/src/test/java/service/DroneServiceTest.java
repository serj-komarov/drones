package service;


import static com.musala.drone.util.Constants.MAX_FLEET_COUNT;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
import static org.testcontainers.shaded.org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;

import com.musala.drone.exception.BusinessException;
import com.musala.drone.model.dto.NewDroneRequest;
import com.musala.drone.model.enums.DroneModel;
import com.musala.drone.repository.DroneRepository;
import com.musala.drone.service.DroneService;
import com.musala.drone.util.ErrorInfo;
import config.ApplicationTest;
import controller.DispatchApiCalls;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

public class DroneServiceTest extends ApplicationTest {
    @MockBean
    private DroneRepository droneRepository;
    @Autowired
    private DroneService droneService;

    @Test
    @DisplayName("When register drone with same serial number, an error occurs")
    void registerDrone() {
        var existSerialNumber = randomAlphanumeric(50);

        when(droneRepository.existsById(existSerialNumber)).thenReturn(true);

        var error = assertThrows(BusinessException.class,
            () -> droneService.register(
                NewDroneRequest.builder().serialNumber(existSerialNumber).model(DroneModel.LIGHT_WEIGHT).build()));

        Assertions.assertEquals(error.getMessage(),
            String.format("Drone with serial number: '%s' already registered", existSerialNumber));
    }

    @Test
    @DisplayName("When already register MAX_FLEET_COUNT drones, and we try to register another one, an error occurs")
    void registerDroneToManyDrones() {
        when(droneRepository.count()).thenReturn(MAX_FLEET_COUNT);

        var error = assertThrows(BusinessException.class,
            () -> droneService.register(
                NewDroneRequest.builder().serialNumber(randomAlphanumeric(50)).model(DroneModel.LIGHT_WEIGHT).build()));

        Assertions.assertEquals(error.getMessage(),
            String.format("The limit of the number of drones(%s) has been reached",
                MAX_FLEET_COUNT));
    }

    @Test
    @DisplayName("getDroneInfo, when drone not found, an error occurs")
    void getDroneInfoNotFound() {
        var randomSerialNumber = randomAlphanumeric(50);

        var response = DispatchApiCalls.getDroneInfo(randomSerialNumber);

        var error = response.then().statusCode(404)
            .extract().as(ErrorInfo.class);
        Assertions.assertEquals(error.getMessage(),
            String.format("Drone with serial number: %s not found", randomSerialNumber));

    }

    @Test
    @DisplayName("getDroneByIdFetchMedications, when drone not found, an error occurs")
    void getDroneMedicationNotFoundDrone() {
        var randomSerialNumber = randomAlphanumeric(50);

        when(droneRepository.findByIdWithMedications(randomSerialNumber)).thenReturn(Optional.empty());

        var error = assertThrows(BusinessException.class,
            () -> droneService.getDroneByIdFetchMedications(randomSerialNumber));

        Assertions.assertEquals(error.getMessage(),
            String.format("Drone with serial number: %s not found",
                randomSerialNumber));
    }

}
