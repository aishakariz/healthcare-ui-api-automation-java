package api;

import base.BaseApiTest;
import base.BaseTest;
import io.restassured.RestAssured;
import org.junit.jupiter.api.Test;
import utils.ConfigManager;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.hamcrest.Matchers.notNullValue;

public class GetSessionTest extends BaseApiTest {


    @Test
    void shouldAuthenticateUser(){


        given()
                .auth().preemptive().basic(
                        ConfigManager.getUsername(),
                        ConfigManager.getPassword()
                )
        .when()
                .get("/session")
                .then()
                .statusCode(200);


    }
}
