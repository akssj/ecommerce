package alledrogo.controller;

import alledrogo.io.request.AddProductRequest;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.springframework.boot.test.context.SpringBootTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static org.hamcrest.Matchers.*;

@SpringBootTest
public class ProductControllerTest {

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

    @Test(priority = 1)
    public void testFindForSaleProducts() {
        RestAssured.given()
                .contentType(ContentType.JSON)
                .when()
                .get("/product/forSale")
                .then()
                .statusCode(200)
                .body("$", notNullValue());
    }

    @Test(priority = 2)
    public void testGetCategoryCollection() {
        RestAssured.given()
                .contentType(ContentType.JSON)
                .when()
                .get("/product/categories")
                .then()
                .statusCode(200)
                .body("$", notNullValue());
    }

    @Test(priority = 3)
    public void testFindProductByCategory() {
        RestAssured.given()
                .contentType(ContentType.JSON)
                .when()
                .get("/product/Laptops/category")
                .then()
                .statusCode(200)
                .body("$", notNullValue());
    }

    @Test(priority = 4)
    public void testFindProductByName() {
        RestAssured.given()
                .contentType(ContentType.JSON)
                .when()
                .get("/product/TestProduct/name")
                .then()
                .statusCode(200)
                .body("$", notNullValue());
    }

    @Test(priority = 5)
    public void testFindMyProducts() {
        RestAssured.given()
                .contentType(ContentType.JSON)
                .cookie("token", authToken)
                .when()
                .get("/product/my-products")
                .then()
                .statusCode(200)
                .body("$", notNullValue());
    }

    @Test(priority = 6)
    public void testFindBoughtProducts() {
        RestAssured.given()
                .contentType(ContentType.JSON)
                .cookie("token", authToken)
                .when()
                .get("/product/bought-products")
                .then()
                .statusCode(200)
                .body("$", notNullValue());
    }

    @Test(priority = 7)
    public void testFindSoldProducts() {
        RestAssured.given()
                .contentType(ContentType.JSON)
                .cookie("token", authToken)
                .when()
                .get("/product/sold")
                .then()
                .statusCode(200)
                .body("$", notNullValue());
    }
}
