package service;

import static java.util.Collections.emptyList;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.testcontainers.shaded.org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;

import com.musala.drone.exception.BusinessException;
import com.musala.drone.model.dto.LoadRequest;
import com.musala.drone.model.entity.Drone;
import com.musala.drone.model.entity.Medication;
import com.musala.drone.model.enums.DroneModel;
import com.musala.drone.model.enums.DroneState;
import com.musala.drone.repository.DroneRepository;
import com.musala.drone.repository.MedicationRepository;
import com.musala.drone.service.DispatchService;
import config.ApplicationTest;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

public class DispatchServiceTest extends ApplicationTest {
    @MockBean
    private MedicationRepository medicationRepository;
    @MockBean
    private DroneRepository droneRepository;
    @Autowired
    private DispatchService dispatchService;

    @Test
    @DisplayName("When drone not ready for loading, an error occurs")
    void loadNotReadyDrone() {
        var randomSerialNumber = randomAlphanumeric(50);
        var drone = Drone.builder()
            .serialNumber(randomSerialNumber)
            .batteryCapacity(100)
            .medications(new ArrayList<>())
            .model(DroneModel.LIGHT_WEIGHT)
            .weightLimit(100)
            .state(DroneState.RETURNING)
            .build();
        var codes = List.of("MEDICATION_TEST_04", "MEDICATION_TEST_05", "MEDICATION_TEST_02");
        var request = LoadRequest.builder()
            .codes(codes)
            .build();

        when(droneRepository.findByIdForUpdate(randomSerialNumber)).thenReturn(Optional.of(drone));
        when(medicationRepository.findAllById(codes)).thenReturn(
            List.of(Medication.builder().code("MEDICATION_TEST_04").name("TEST").weight(50).build(),
                Medication.builder().code("MEDICATION_TEST_02").name("TEST").weight(50).build(),
                Medication.builder().code("MEDICATION_TEST_05").name("TEST").weight(50).build()));

        var error = assertThrows(BusinessException.class,
            () -> dispatchService.load(randomSerialNumber, request));

        Assertions.assertEquals(
            String.format("Drone with serial number: '%s', state: '%s', not ready for loading now, try another drone",
                randomSerialNumber, "RETURNING"), error.getMessage());
    }
    @Test
    @DisplayName("When try to load drone, witch not present in DB, an error occurs")
    void loadNotExistDrone() {
        var randomSerialNumber = randomAlphanumeric(50);

        when(droneRepository.findByIdForUpdate(randomSerialNumber)).thenReturn(Optional.empty());

        var error = assertThrows(BusinessException.class,
            () -> dispatchService.load(randomSerialNumber, any()));

        Assertions.assertEquals(error.getMessage(),
            String.format("Drone with serial number: %s not found",
                randomSerialNumber));
    }

    @Test
    @DisplayName("getAvailableDrones, when there are no drones to load, an error occurs")
    void getAvailableDronesNotFound() {
        when(droneRepository.getAvailableDrones(any())).thenReturn(emptyList());

        var error = assertThrows(BusinessException.class,
            () -> dispatchService.getAvailableDrones());
        Assertions.assertEquals(error.getMessage(), "Available drones for loading not found");
    }

}
