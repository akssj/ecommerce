package alledrogo.controller;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.springframework.boot.test.context.SpringBootTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

@SpringBootTest
public class ProductControllerTest {

    private static final String BASE_URL = "http://localhost:8080";

    @BeforeClass
    public void setup() {
        RestAssured.baseURI = BASE_URL;
    }

    private String authToken;

    @Test(priority = 1)
    public void testFindForSaleProducts() {
        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .when()
                .get("/product/forSale");

        response.then()
                .statusCode(200);
    }

    @Test(priority = 2)
    public void testGetCategoryCollection() {
        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .when()
                .get("/product/categories");

        response.then()
                .statusCode(200);
    }

    @Test(priority = 3)
    public void testFindProductByCategory() {
        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .when()
                .get("/product/Laptops/category");

        response.then()
                .statusCode(200);
    }

    @Test(priority = 4)
    public void testFindProductByName() {
        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .when()
                .get("/product/PowerHammer/name");

        response.then()
                .statusCode(200);
    }

    @Test(priority = 5)
    public void testFindMyProducts() {
        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .cookie("token", authToken)
                .when()
                .get("/product/my-products");

        response.then()
                .statusCode(200);
    }

    @Test(priority = 6)
    public void testFindBoughtProducts() {
        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .cookie("token", authToken)
                .when()
                .get("/product/bought-products");

        response.then()
                .statusCode(200);
    }

    @Test(priority = 7)
    public void testFindSoldProducts() {
        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .cookie("token", authToken)
                .when()
                .get("/product/sold");

        response.then()
                .statusCode(200);
    }
}
