package config;

import com.musala.drone.DroneApplication;
import io.restassured.RestAssured;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@ActiveProfiles("test")
@SpringBootTest(classes = {TestDataSourceConfig.class,
    DroneApplication.class}, webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public abstract class ApplicationTest {

    @LocalServerPort
    protected int port;
    @Autowired
    protected MockMvc mockMvc;

    @BeforeEach
    @SneakyThrows
    void setup() {
        RestAssuredMockMvc.mockMvc(mockMvc);
        RestAssured.port = port;
    }

}
