package alledrogo.controller;

import alledrogo.io.request.AuthenticationRequest;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.springframework.boot.test.context.SpringBootTest;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
@SpringBootTest
public class AuthenticationControllerTest {

    private static final String BASE_URL = "http://localhost:8080/auth";
    private static final String TEST_ACCOUNT_USERNAME = "TestAccount";
    private static final String TEST_ACCOUNT_PASSWORD = "TestAccount";
    private static final String TEST_ACCOUNT_EMAIL = "TestAccount@email.com";
    private String auth_token;

    @BeforeClass
    public void setup() {
        RestAssured.baseURI = BASE_URL;
    }

    @Test(priority = 5)
    public void testLoginUser() {
        AuthenticationRequest request = new AuthenticationRequest();
        request.setUsername(TEST_ACCOUNT_USERNAME);
        request.setPassword(TEST_ACCOUNT_PASSWORD);
        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post("/login");

        Assert.assertEquals(response.getStatusCode(), 200);
    }

    @Test(priority = 1)
    public void testSignupUser() {
        AuthenticationRequest request = new AuthenticationRequest();
        request.setUsername(TEST_ACCOUNT_USERNAME);
        request.setPassword(TEST_ACCOUNT_PASSWORD);
        request.setConfirmPassword(TEST_ACCOUNT_PASSWORD);
        request.setEmail(TEST_ACCOUNT_EMAIL);
        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post("/signup");

        Assert.assertEquals(response.getStatusCode(), 200);

        String cookieHeader = response.getHeader("Set-Cookie");

        String[] parts = cookieHeader.split(";");
        auth_token = parts[0].split("=")[1];
    }

    @Test(priority = 4)
    public void testLogoutUser() {
        Response response = RestAssured.given()
                .cookie("token", auth_token)
                .contentType(ContentType.JSON)
                .when()
                .post("/logout");

        Assert.assertEquals(response.getStatusCode(), 200);
    }

    @Test(priority = 2)
    public void testUserStatus() {
        Response response = RestAssured.given()
                .cookie("token", auth_token)
                .when()
                .get("/userStatus");

        Assert.assertEquals(response.getStatusCode(), 200);
    }

    @Test(priority = 3)
    public void testUserData() {
        Response response = RestAssured.given()
                .cookie("token", auth_token)
                .when()
                .post("/userData");

        Assert.assertEquals(response.getStatusCode(), 200);
    }

    @Test(priority = 7)
    public void testDeleteUser() {
        AuthenticationRequest request = new AuthenticationRequest();
        request.setUsername(TEST_ACCOUNT_USERNAME);
        request.setPassword("new" + TEST_ACCOUNT_PASSWORD);
        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .cookie("token", auth_token)
                .body(request)
                .when()
                .post("/delete");

        Assert.assertEquals(response.getStatusCode(), 200);
    }

    @Test(priority = 6)
    public void testUpdateUser() {
        AuthenticationRequest request = new AuthenticationRequest();
        request.setPassword(TEST_ACCOUNT_PASSWORD);
        request.setNewPassword("new" + TEST_ACCOUNT_PASSWORD);
        request.setConfirmPassword("new" + TEST_ACCOUNT_PASSWORD);
        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .cookie("token", auth_token)
                .body(request)
                .when()
                .put("/changePassword");

        Assert.assertEquals(response.getStatusCode(), 200);
    }
}
