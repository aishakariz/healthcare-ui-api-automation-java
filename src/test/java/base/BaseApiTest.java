package base;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.extension.ExtendWith;
import utils.ConfigManager;
import utils.TestListener;

import static io.restassured.RestAssured.given;

@ExtendWith(TestListener.class)
public class BaseApiTest {

    protected static RequestSpecification requestSpec;
    protected static ResponseSpecification responseSpec;
    protected static String sessionId;

    @BeforeAll
    static void setup() {

        RestAssured.baseURI = ConfigManager.getApiBaseUrl();

        // LOGIN → get session
        Response response =
                given()
                        .auth()
                        .preemptive()
                        .basic(
                                ConfigManager.getUsername(),
                                ConfigManager.getPassword()
                        )
                        .when()
                        .get("/session")
                        .then()
                        .statusCode(200)
                        .extract()
                        .response();

        sessionId = response.cookie("JSESSIONID");

        System.out.println("Session ID: " + sessionId);

        // Request specification with authentication cookie
        requestSpec = new RequestSpecBuilder()
                .setBaseUri(RestAssured.baseURI)
                .setContentType("application/json")
                .addCookie("JSESSIONID", sessionId)
                .build();

        responseSpec = new ResponseSpecBuilder()
                .expectStatusCode(200)
                .build();
    }
}


