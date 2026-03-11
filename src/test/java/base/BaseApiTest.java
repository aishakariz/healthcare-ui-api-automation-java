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
import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.is;

@ExtendWith(TestListener.class)
public class BaseApiTest {

    protected static RequestSpecification requestSpec;
    protected static ResponseSpecification responseSpec;
    protected static String sessionId;

    @BeforeAll
    static void setup() {

        // Set API base URL
        RestAssured.baseURI = ConfigManager.getApiBaseUrl();

        // Authenticate and create session
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

        // Extract session cookie
        sessionId = response.cookie("JSESSIONID");

        System.out.println("Session ID: " + sessionId);

        // Request specification (used by all API tests)
        requestSpec = new RequestSpecBuilder()
                .setBaseUri(RestAssured.baseURI)
                .setContentType("application/json")
                .addCookie("JSESSIONID", sessionId)
                .build();

        // Response specification (common success codes)
        responseSpec = new ResponseSpecBuilder()
                .expectStatusCode(anyOf(
                        is(200),
                        is(201),
                        is(204)
                ))
                .build();
    }
}
