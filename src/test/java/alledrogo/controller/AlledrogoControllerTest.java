package alledrogo.controller;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.springframework.boot.test.context.SpringBootTest;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

@SpringBootTest
public class AlledrogoControllerTest {

    private static final String BASE_URL = "http://localhost:8080";
    private String authToken;

    @BeforeClass
    public void setup() {
        RestAssured.baseURI = BASE_URL;
        authenticate();
    }

    private void authenticate() {
        Response response = RestAssured.given()
                .contentType("application/json")
                .body("{\"username\": \"test\", \"password\": \"test\"}")
                .post("/auth/login");

        response.then().statusCode(200);
        authToken = response.getCookie("token");
    }

    @Test
    public void testMainPage() {
        Response response = RestAssured.get("/");
        Assert.assertEquals(response.getStatusCode(), 200);
    }

    @Test
    public void testBoughtProducts_Unauthorized() {
        Response response = RestAssured.get("/bought-products");
        Assert.assertEquals(response.getStatusCode(), 401);
    }

    @Test
    public void testBoughtProducts_Authorized() {
        Response response = RestAssured.given()
                .cookie("token", authToken)
                .get("/bought-products.html");

        Assert.assertEquals(response.getStatusCode(), 200);
    }

    @Test
    public void testMyProducts_Authorized() {
        Response response = RestAssured.given()
                .cookie("token", authToken)
                .get("/my-products.html");

        Assert.assertEquals(response.getStatusCode(), 200);
    }

    @Test
    public void testMyProfile_Authorized() {
        Response response = RestAssured.given()
                .cookie("token", authToken)
                .get("/account.html");

        Assert.assertEquals(response.getStatusCode(), 200);
    }

    @Test
    public void testFilteredProducts() {
        Response response = RestAssured.get("/product/Laptops/category");
        Assert.assertEquals(response.getStatusCode(), 200);
    }
}
