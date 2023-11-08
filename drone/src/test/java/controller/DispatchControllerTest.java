package controller;

import static controller.DispatchApiCalls.getAvailableDronesResponse;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.testcontainers.shaded.org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;

import com.musala.drone.model.dto.DroneDto;
import com.musala.drone.model.dto.LoadRequest;
import com.musala.drone.model.dto.MedicationDto;
import com.musala.drone.model.dto.NewDroneRequest;
import com.musala.drone.model.entity.Medication;
import com.musala.drone.model.enums.DroneModel;
import com.musala.drone.model.enums.DroneState;
import com.musala.drone.repository.DroneRepository;
import com.musala.drone.util.ErrorInfo;
import config.ApplicationTest;
import io.restassured.response.Response;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.sql.DataSource;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
public class DispatchControllerTest extends ApplicationTest {

    @Autowired
    private DroneRepository droneRepository;

    @BeforeAll
    public static void setup(@Autowired DataSource dataSource) throws SQLException {
        Connection conn = dataSource.getConnection();
        ScriptUtils.executeSqlScript(conn, new ClassPathResource("/scripts/add_drones_test_data.sql"));
    }

    @Test
    @DisplayName("Register drone, happy-path")
    void registerDroneResponse() {
        var newSerialNumber = randomAlphanumeric(50);
        var request = NewDroneRequest.builder()
            .serialNumber(newSerialNumber)
            .model(DroneModel.HEAVY_WEIGHT)
            .build();

        var response = DispatchApiCalls.registerResponse(request);

        Assertions.assertEquals(response.getSerialNumber(), newSerialNumber);
        Assertions.assertEquals(response.getModel(), DroneModel.HEAVY_WEIGHT);
        Assertions.assertEquals(response.getBatteryCapacity(), 100);
        Assertions.assertEquals(response.getState(), DroneState.IDLE);

        // check DB entity
        var entityDrone = droneRepository.findById(newSerialNumber).orElseThrow();
        Assertions.assertEquals(entityDrone.getSerialNumber(), newSerialNumber);
        Assertions.assertEquals(entityDrone.getModel(), DroneModel.HEAVY_WEIGHT);
        Assertions.assertEquals(entityDrone.getBatteryCapacity(), 100);
        Assertions.assertEquals(entityDrone.getState(), DroneState.IDLE);

    }

    @Test
    @DisplayName("When register drone with too long serial number, an error occurs")
    void registerDroneWrongSerial() {
        var newSerialNumber = randomAlphanumeric(101);
        var request = NewDroneRequest.builder()
            .serialNumber(newSerialNumber)
            .model(DroneModel.HEAVY_WEIGHT)
            .build();

        var response = DispatchApiCalls.register(request);
        var error = response.then().statusCode(400)
            .extract().as(ErrorInfo.class);

        Assertions.assertEquals(error.getMessage(), "field 'serialNumber' must be less 100 characters");
    }

    @Test
    @DisplayName("getAvailableDrones, happy-path, check response")
    void getAvailableDronesHappyPath() {
        var availableDrones = getAvailableDronesResponse();

        assertThat(availableDrones).flatExtracting(DroneDto::getState)
            .containsOnly(DroneState.IDLE, DroneState.LOADING);
        availableDrones.stream().map(DroneDto::getBatteryCapacity).forEach(o -> assertTrue(o >= 25));
    }

    @Test
    @DisplayName("getDroneInfo, happy-path, check response")
    void getDroneInfo() {
        var serialNumber = "DRONE-TEST-02";
        var response = DispatchApiCalls.getDroneInfoResponse(serialNumber);

        assertThat(response).isNotNull();
        assertThat(response.getSerialNumber()).isEqualTo(serialNumber);
        assertThat(response.getWeightLimit()).isEqualTo(100);
        assertThat(response.getBatteryCapacity()).isEqualTo(100);
        assertThat(response.getModel()).isEqualTo(DroneModel.LIGHT_WEIGHT);
        assertThat(response.getState()).isEqualTo(DroneState.DELIVERING);
    }


    @Test
    @DisplayName("getDroneMedication, happy-path, check response")
    void getDroneMedication() {
        var serialNumber = "DRONE-TEST-02";
        var response = DispatchApiCalls.getDroneMedicationResponse(serialNumber);

        assertThat(response).isNotNull();
        assertThat(response.getDroneSerialNumber()).isEqualTo(serialNumber);
        assertThat(response.getLoadedWeight()).isEqualTo(40);
        assertThat(response.getDroneMaxWeight()).isEqualTo(100);
        assertThat(response.getMedications()).hasSize(4).flatExtracting(MedicationDto::getName)
            .containsOnly("ASPIRIN", "VITAMIN");

        var totalWeight = response.getMedications().stream().map(MedicationDto::getWeight).reduce(0, Integer::sum);
        assertEquals(totalWeight, response.getLoadedWeight());

    }

    @Test
    @DisplayName("LoadDrone, happy-path, check DB and response")
    @Transactional
    void loadDrone() {
        //maxWeight 300
        var serialNumber = "DRONE-TEST-03";

        // total weight must be 260
        var request = LoadRequest.builder()
            .codes(List.of("MEDICATION_TEST_04", "MEDICATION_TEST_05", "MEDICATION_TEST_02"))
            .build();

        var response = DispatchApiCalls.loadResponse(serialNumber, request);

        assertThat(response).isNotNull();
        assertThat(response.getLoadedWeight()).isEqualTo(260);
        assertThat(response.getDroneMaxWeight()).isEqualTo(300);
        assertThat(response.getMedications()).hasSize(3).flatExtracting(MedicationDto::getCode)
            .containsOnly("MEDICATION_TEST_04", "MEDICATION_TEST_05", "MEDICATION_TEST_02");
        assertThat(response.getDroneSerialNumber()).isEqualTo(serialNumber);

        // check DB
        var droneEntity = droneRepository.findById(serialNumber).orElseThrow();

        assertThat(droneEntity.getSerialNumber()).isEqualTo(serialNumber);
        assertThat(droneEntity.getState()).isEqualTo(DroneState.LOADING);
        assertThat(droneEntity.getMedications()).hasSize(3).flatExtracting(Medication::getCode)
            .containsOnly("MEDICATION_TEST_04", "MEDICATION_TEST_05", "MEDICATION_TEST_02");

    }

    @Test
    @DisplayName("When try to load same drone with many treads, and this will lead to overload, only one thread will receive an error")
    void multiThreadingLoadToDrone() {
        //maxWeight 100
        var serialNumber = "DRONE-TEST-09";
        var requests = new ArrayList<LoadRequest>();
        // Weight 50
        var request = LoadRequest.builder()
            .codes(List.of("MEDICATION_TEST_03"))
            .build();
        requests.add(request);
        requests.add(request);
        requests.add(request);

        // try run 3 thead to load same drone, we expect susses for all requests, except one, and DroneState must be LOADED
        List<CompletableFuture<Response>> futures = requests.stream()
            .map(o -> CompletableFuture.supplyAsync(() -> DispatchApiCalls.load(serialNumber, o))).toList();

        var result = futures.stream()
            .map(CompletableFuture::join)
            .collect(Collectors.toCollection(ArrayList::new));

        assertThat(result).hasSize(3)
            .flatExtracting((Function<? super Response, ?>) o -> o.then().extract().statusCode())
            .containsOnlyOnce(400)
            .contains(200);

        var droneEntity = droneRepository.findById(serialNumber);

        assertThat(droneEntity.orElseThrow().getState()).isEqualTo(DroneState.LOADED);
    }
}

