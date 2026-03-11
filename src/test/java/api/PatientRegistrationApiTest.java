package api;

import base.BaseApiTest;
import io.qameta.allure.*;
import org.junit.jupiter.api.*;
import utils.ConfigManager;

import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@Epic("Patient Management")
@Feature("US04 - Patient Registration")
@Tag("api")
@Tag("regression")
@Tag("us04")
@DisplayName("US04 - API: Register Patient")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PatientRegistrationApiTest extends BaseApiTest {

    private static final String USERNAME = ConfigManager.getUsername();
    private static final String PASSWORD = ConfigManager.getPassword();

    private static String identifierTypeUuid;
    private static String locationUuid;

    // Tracks every patient UUID created during the test run — all deleted in @AfterAll
    private static final List<String> createdPatientUuids = new ArrayList<>();

    // OpenMRS Luhn Mod-30 character set
    private static final String LUHN_CHARS = "0123456789ACDEFGHJKLMNPRTUVWXY";

    // ═════════════════════════════════════════════════════════════════════════
    // SETUP & TEARDOWN
    // ═════════════════════════════════════════════════════════════════════════

    @BeforeAll
    static void setUp() {
        identifierTypeUuid = fetchFirstUuid("/patientidentifiertype");
        locationUuid       = fetchFirstUuid("/location");
        System.out.println("identifierTypeUuid = " + identifierTypeUuid);
        System.out.println("locationUuid       = " + locationUuid);
    }

    /**
     * Deletes every patient created during this test run.
     * Uses ?purge=true for a hard delete so records don't linger on the shared server.
     */
    @AfterAll
    static void cleanUp() {
        System.out.println("Cleaning up " + createdPatientUuids.size() + " patient(s)...");
        for (String uuid : createdPatientUuids) {
            given()
                    .auth().preemptive().basic(USERNAME, PASSWORD)
                    .queryParam("purge", "true")
                    .when()
                    .delete("/patient/" + uuid)
                    .then()
                    .statusCode(anyOf(is(200), is(204)));
            System.out.println("Deleted patient: " + uuid);
        }
    }

    // ═════════════════════════════════════════════════════════════════════════
    // HELPERS
    // ═════════════════════════════════════════════════════════════════════════

    /** Calls any list endpoint and returns the UUID of the first result. */
    private static String fetchFirstUuid(String endpoint) {
        return given()
                .auth().preemptive().basic(USERNAME, PASSWORD)
                .contentType("application/json")
                .when()
                .get(endpoint)
                .then()
                .statusCode(200)
                .extract()
                .path("results[0].uuid");
    }

    /**
     * Generates a unique patient identifier that passes OpenMRS Luhn Mod-30 validation.
     *
     * How it works:
     *   1. Build a 6-digit numeric base using nanoTime (guarantees uniqueness per call)
     *   2. Walk the base right-to-left, doubling every other digit starting with the rightmost
     *   3. Check digit = (30 - sum % 30) % 30, mapped to LUHN_CHARS
     *   4. Return base + check digit  e.g. "278854G"
     *
     * NOTE: doubleIt starts TRUE — the rightmost digit must always be doubled in Luhn Mod-30.
     */
    private static String generateLuhnId() {
        String base = String.valueOf(100000 + (System.nanoTime() % 900000));

        int sum = 0;
        boolean doubleIt = true;   // ← MUST be true: rightmost digit is always doubled
        for (int i = base.length() - 1; i >= 0; i--) {
            int value = LUHN_CHARS.indexOf(base.charAt(i));
            if (doubleIt) {
                value = value * 2;
                if (value >= 30) value -= 29;
            }
            sum += value;
            doubleIt = !doubleIt;
        }

        char checkDigit = LUHN_CHARS.charAt((30 - (sum % 30)) % 30);
        return base + checkDigit;
    }

    /** Builds a minimal patient JSON payload — name, gender, DOB, identifier. */
    private String basicPatient(String firstName, String lastName, String gender, String dob) {
        return """
                {
                  "person": {
                    "names": [{ "givenName": "%s", "familyName": "%s", "preferred": true }],
                    "gender":    "%s",
                    "birthdate": "%s",
                    "birthdateEstimated": false
                  },
                  "identifiers": [{
                    "identifier":     "%s",
                    "identifierType": "%s",
                    "location":       "%s",
                    "preferred": true
                  }]
                }
                """.formatted(firstName, lastName, gender, dob,
                generateLuhnId(), identifierTypeUuid, locationUuid);
    }

    /** Builds a patient JSON payload that includes a physical address. */
    private String patientWithAddress(String firstName, String lastName, String gender, String dob,
                                      String address, String city, String province, String country) {
        return """
                {
                  "person": {
                    "names": [{ "givenName": "%s", "familyName": "%s", "preferred": true }],
                    "gender":    "%s",
                    "birthdate": "%s",
                    "birthdateEstimated": false,
                    "addresses": [{
                      "address1":      "%s",
                      "cityVillage":   "%s",
                      "stateProvince": "%s",
                      "country":       "%s",
                      "preferred": true
                    }]
                  },
                  "identifiers": [{
                    "identifier":     "%s",
                    "identifierType": "%s",
                    "location":       "%s",
                    "preferred": true
                  }]
                }
                """.formatted(firstName, lastName, gender, dob,
                address, city, province, country,
                generateLuhnId(), identifierTypeUuid, locationUuid);
    }

    /** Returns a pre-configured request with admin auth and JSON content type. */
    private io.restassured.specification.RequestSpecification asAdmin() {
        return given()
                .auth().preemptive().basic(USERNAME, PASSWORD)
                .contentType("application/json");
    }

    // ═════════════════════════════════════════════════════════════════════════
    // TESTS
    // ═════════════════════════════════════════════════════════════════════════

    @Test @Order(1) @Tag("smoke") @Story("ORION-344")
    @DisplayName("AC1 - Register patient with basic info only")
    @Description("ORION-344: John QA01 | Male | DOB 1995-01-01")
    void ac1_registerPatientWithBasicInfo() {
        String uuid = asAdmin()
                .body(basicPatient("John", "QA01", "M", "1995-01-01"))
                .when()
                .post("/patient")
                .then()
                .statusCode(201)
                .body("uuid", notNullValue())
                .body("person.display", containsString("John"))
                .extract().path("uuid");

        createdPatientUuids.add(uuid);
    }

    @Test @Order(2) @Story("ORION-345")
    @DisplayName("AC2 - Register patient, then retrieve by UUID")
    @Description("ORION-345: SarahQA_02 | Female | DOB 1996-02-02 | Verify retrievable after creation")
    void ac2_registerPatientAndRetrieveByUuid() {
        // Step 1: Create patient
        String uuid = asAdmin()
                .body(basicPatient("SarahQA_02", "Test", "F", "1996-02-02"))
                .when()
                .post("/patient")
                .then()
                .statusCode(201)
                .body("person.display", containsString("SarahQA_02"))
                .extract().path("uuid");

        createdPatientUuids.add(uuid);

        // Step 2: Confirm retrievable by UUID
        asAdmin()
                .when()
                .get("/patient/" + uuid)
                .then()
                .statusCode(200)
                .body("uuid", equalTo(uuid));
    }

    @Test @Order(3) @Story("ORION-346")
    @DisplayName("AC3 - Register patient with contact details")
    @Description("ORION-346: MarkQA_03 | Male | DOB 1994-03-03 | 123 Main St, Toronto, Ontario")
    void ac3_registerPatientWithContactDetails() {
        // Step 1: Create the patient
        String uuid = asAdmin()
                .body(patientWithAddress(
                        "MarkQA_03", "Test", "M", "1994-03-03",
                        "123 Main St", "Toronto", "Ontario", "Canada"))
                .when()
                .post("/patient")
                .then()
                .statusCode(201)
                .body("uuid", notNullValue())
                .extract().path("uuid");

        createdPatientUuids.add(uuid);

        // Step 2: Fetch full representation to verify address was saved
        asAdmin()
                .queryParam("v", "full")
                .when()
                .get("/patient/" + uuid)
                .then()
                .statusCode(200)
                .body("person.preferredAddress.cityVillage",   equalTo("Toronto"))
                .body("person.preferredAddress.stateProvince", equalTo("Ontario"));
    }

    @Test @Order(4) @Story("ORION-335")
    @DisplayName("AC5 - Search for patient by name")
    @Description("ORION-335 AC5: Search 'John' returns at least one result with a valid UUID")
    void ac5_searchPatientByName() {
        asAdmin()
                .queryParam("q", "John")
                .when()
                .get("/patient")
                .then()
                .statusCode(200)
                .body("results",         not(empty()))
                .body("results[0].uuid", notNullValue());
    }

    @Test @Order(5) @Tag("regression") @Story("ORION-335")
    @DisplayName("NEG - Reject registration with non-existent identifier type")
    @Description("Negative: using a fake identifierType UUID must return 400/404/500")
    void neg_rejectInvalidIdentifierType() {
        String fakeUuid = "00000000-0000-0000-0000-000000000000";

        String payload = """
                {
                  "person": {
                    "names": [{ "givenName": "Bad", "familyName": "Patient", "preferred": true }],
                    "gender": "M", "birthdate": "1990-01-01", "birthdateEstimated": false
                  },
                  "identifiers": [{
                    "identifier":     "%s",
                    "identifierType": "%s",
                    "location":       "%s",
                    "preferred": true
                  }]
                }
                """.formatted(generateLuhnId(), fakeUuid, locationUuid);

        // This patient is intentionally rejected — no UUID returned, no cleanup needed
        asAdmin()
                .body(payload)
                .when()
                .post("/patient")
                .then()
                .statusCode(anyOf(is(400), is(404), is(500)));
    }
}
/*
setUp() runs
  → identifierTypeUuid = "05a29f94-..."
  → locationUuid = "1ce1b7d4-..."

ac1 runs → patient created → uuid = "7066bf3c-..."
  createdPatientUuids.add("7066bf3c-...")
  → list is now: ["7066bf3c-..."]

ac2 runs → patient created → uuid = "abc12345-..."
  createdPatientUuids.add("abc12345-...")
  → list is now: ["7066bf3c-...", "abc12345-..."]

ac3 runs → patient created → uuid = "def67890-..."
  createdPatientUuids.add("def67890-...")
  → list is now: ["7066bf3c-...", "abc12345-...", "def67890-..."]

ac5 runs → no patient created → nothing added

neg runs  → patient rejected  → nothing added

cleanUp() runs
  → DELETE /patient/7066bf3c-...?purge=true
  → DELETE /patient/abc12345-...?purge=tru
  → DELETE /patient/def67890-...?purge=true
 */