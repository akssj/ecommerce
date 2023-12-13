package alledrogo.tests.controller;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.json.JSONException;
import org.json.JSONObject;
import org.testng.annotations.*;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

/**
 * Tests (API, integration) for /auth/* endpoints.
 * Goal is to verify that each of these endpoints are working as intended (verification scenarios).
 */
public class AuthenticationControllerTest {

    //TODO get make config files with all static variable values such as roles so changing roles does not require changing all variables in all tests
    private static final String TEST_ACCOUNT_USERNAME = "TempTestAccount";
    private static final String TEST_ACCOUNT_PASSWORD = "TempTestAccount";
    private String auth_token;
    private String tokenType;
    private Long id;
    private final JSONObject testJsonRequest = new JSONObject();

    @BeforeClass
    private void beforeClass(){
        try {
            testJsonRequest.put("username", TEST_ACCOUNT_USERNAME);
            testJsonRequest.put("password", TEST_ACCOUNT_PASSWORD);
        } catch (JSONException e) {
            throw new RuntimeException(e);
            //TODO handle it
        }
    }

    /**
     * sends correct POST request to /signup endpoint.
     * expected result: Account is being created. User receive message stating operation is a success.
     */
    @Test(priority = 1)
    protected void registerUserTest() {
        given()
            .contentType(ContentType.JSON)
            .body(testJsonRequest.toString())
        .when()
            .post("http://localhost:8080/auth/signup")
        .then()
            .statusCode(200)
            .body("message", equalTo("User registered successfully!"));
    }

    /**
     * sends already existing test-user data in POST request to /signup endpoint.
     * expected result: Account not is being created. User receive message stating username is not available.
     */
    @Test(priority = 2)
    protected void registerExistingUserTest() {
        given()
            .contentType(ContentType.JSON)
            .body(testJsonRequest.toString())
        .when()
            .post("http://localhost:8080/auth/signup")
        .then()
            .statusCode(400)
            .body("message", equalTo("Username is already taken!"));
    }

    /**
     * sends correct POST request to /login endpoint.
     * Saves login data for next tests inside this class.
     * expected result: Login succeed, user receives token and user-data from server.
     */
    @Test(priority = 3)
    protected void loginUserTest() {
        Response response =
        RestAssured.given()
            .contentType(ContentType.JSON)
            .body(testJsonRequest.toString())
        .when()
            .post("http://localhost:8080/auth/login")
        .then()
            .statusCode(200)
            .body("token", notNullValue())
            .body("type", equalTo("Bearer"))
            .body("id", notNullValue())
            .body("username", equalTo(TEST_ACCOUNT_USERNAME))
            .body("roles.authority", notNullValue())
            .body("balance", notNullValue())
        .extract()
        .response();

        auth_token = response.path("token").toString();
        tokenType = response.path("type").toString();
        id = Long.parseLong(response.path("id").toString());
    }

    /**
     * sends correct GET request to /{id}/ userStatus endpoint.
     * expected result: Respond from server is a success, user receives user-data from server.
     */
    @Test(priority = 4)
    protected void authenticateLoginStatusTest() {
        RestAssured.given()
            .contentType(ContentType.JSON)
            .header("Authorization", tokenType + " " + auth_token)
            .pathParam("id", id)
        .when()
            .get("http://localhost:8080/auth/{id}/userStatus")
        .then()
            .statusCode(200)
            .body("id", notNullValue())
            .body("username", notNullValue())
            .body("roles", notNullValue())
            .body("balance", notNullValue());
    }

    /**
     * sends correct DELETE request to /{id}/delete endpoint.
     * expected result: Account deleted. User receive message stating operation is a success.
     */
    @Test(priority = 5)
    protected void deleteUserTest() {
        RestAssured.given()
            .contentType(ContentType.JSON)
            .header("Authorization", tokenType + " " + auth_token)
            .body(testJsonRequest.toString())
            .pathParam("id", id)
        .when()
            .delete("http://localhost:8080/auth/{id}/delete")
        .then()
            .statusCode(200)
            .body("message", equalTo("User deleted successfully!"));
    }
}
