package controller;

import static com.musala.drone.util.Constants.DISPATCHER_API;
import static io.restassured.RestAssured.given;

import com.musala.drone.model.dto.DroneDto;
import com.musala.drone.model.dto.DroneMedicationDto;
import com.musala.drone.model.dto.LoadRequest;
import com.musala.drone.model.dto.NewDroneRequest;
import io.restassured.common.mapper.TypeRef;
import io.restassured.response.Response;
import java.util.List;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DispatchApiCalls {
    public static Response register(NewDroneRequest request) {
        return given()
            .contentType("application/json")
            .accept("application/json")
            .body(request)
            .when()
            .post(DISPATCHER_API + "/drone/register")
            .thenReturn();
    }

    public static DroneDto registerResponse(NewDroneRequest request) {
        return register(request).then()
            .statusCode(200)
            .extract()
            .body()
            .as(DroneDto.class);
    }

    public static Response load(String id, LoadRequest request) {
        return given()
            .contentType("application/json")
            .accept("application/json")
            .body(request)
            .when()
            .patch(DISPATCHER_API + "/drone/load/" + id)
            .thenReturn();
    }

    public static DroneMedicationDto loadResponse(String id, LoadRequest request) {
        return load(id, request).then()
            .statusCode(200)
            .extract()
            .body()
            .as(DroneMedicationDto.class);
    }

    public static Response getAvailableDrones() {
        return given()
            .contentType("application/json")
            .accept("application/json")
            .when()
            .get(DISPATCHER_API + "/check/drone/available-for-loading")
            .thenReturn();
    }

    public static List<DroneDto> getAvailableDronesResponse() {
        return getAvailableDrones().then()
            .statusCode(200)
            .extract()
            .body()
            .as(new TypeRef<>() {
            });
    }

    public static Response getDroneInfo(String id) {
        return given()
            .contentType("application/json")
            .accept("application/json")
            .when()
            .get(DISPATCHER_API + "/check/drone/" + id)
            .thenReturn();
    }

    public static DroneDto getDroneInfoResponse(String id) {
        return getDroneInfo(id).then()
            .statusCode(200)
            .extract()
            .body()
            .as(DroneDto.class);
    }

    public static Response getDroneMedication(String id) {
        return given()
            .contentType("application/json")
            .accept("application/json")
            .when()
            .get(DISPATCHER_API + "/check/drone/medication/" + id)
            .thenReturn();
    }

    public static DroneMedicationDto getDroneMedicationResponse(String id) {
        return getDroneMedication(id).then()
            .statusCode(200)
            .extract()
            .body()
            .as(DroneMedicationDto.class);
    }
}
