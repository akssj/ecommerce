package main.controller;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.json.JSONException;
import org.json.JSONObject;
import org.testng.annotations.*;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class AuthenticationControllerTest {

    //TODO get make config files with all static variable values such as roles so changing roles does not require changing all variables in all tests
    private static final String TEST_ACCOUNT_USERNAME = "TempTestAccount";
    private static final String TEST_ACCOUNT_PASSWORD = "TempTestAccount";
    private String auth_token;
    private String tokenType;
    private Long id;
    private final JSONObject testJsonRequest = new JSONObject();

    @BeforeClass
    public void beforeClass(){
        try {
            testJsonRequest.put("username", TEST_ACCOUNT_USERNAME);
            testJsonRequest.put("password", TEST_ACCOUNT_PASSWORD);
        } catch (JSONException e) {
            throw new RuntimeException(e);
            //TODO handle it
        }
    }

    @Test(priority = 1, description = "Checks if Signup endpoint is working")
    public void registerUserTest() {
        given()
            .contentType(ContentType.JSON)
            .body(testJsonRequest.toString())
        .when()
            .post("http://localhost:8080/auth/signup")
        .then()
            .statusCode(200)
            .body("message", equalTo("User registered successfully!"));
    }
    @Test(priority = 2, description = "Checks if Signup endpoint is working if data is already in database")
    public void registerExistingUserTest() {
        given()
            .contentType(ContentType.JSON)
            .body(testJsonRequest.toString())
        .when()
            .post("http://localhost:8080/auth/signup")
        .then()
            .statusCode(400)
            .body("message", equalTo("Username is already taken!"));
    }
    @Test(priority = 3, description = "Checks if login endpoint is working")
    public void loginUserTest() {
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
    @Test(priority = 4, description = "Checks if userStatus endpoint is working")
    public void authenticateLoginStatusTest() {
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

    @Test(priority = 5, description = "Checks if deleteUser endpoint is working")
    public void deleteUserTest() {
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
