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

    @BeforeClass
    public void setup() {
        RestAssured.baseURI = BASE_URL;
    }

    @Test
    public void testMainPage() {
        Response response = RestAssured.get("/main");
        Assert.assertEquals(response.getStatusCode(), 200);
    }

    @Test
    public void testBoughtProducts() {
        Response response = RestAssured.given()
                .get("/bought-products");
        Assert.assertEquals(response.getStatusCode(), 401);
    }

    @Test
    public void testMyProducts() {
        Response response = RestAssured.given()
                .get("/my-products");
        Assert.assertEquals(response.getStatusCode(), 401);
    }

    @Test
    public void testMyProfile() {
        Response response = RestAssured.given()
                .get("/my-profile");
        Assert.assertEquals(response.getStatusCode(), 401);
    }

    @Test
    public void testFilteredProducts() {
        Response response = RestAssured.get("/category/someCategory");
        Assert.assertEquals(response.getStatusCode(), 200);
    }
}
