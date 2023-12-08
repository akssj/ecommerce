package main.controllers;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import main.dataset.TestDataDealer;
import main.data.entity.ProductEntity;
import org.testng.annotations.Test;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class ProductControllerTest {

    private static final TestDataDealer testDataDealer = new TestDataDealer();

    @Test(priority = 1)
    public void findForSaleProductTest() {
        List<ProductEntity> productList =
            given()
                .contentType(ContentType.JSON)
            .when()
                .get("http://localhost:8080/api/product/forSale")
            .then()
                .statusCode(200)
                .extract()
                .body()
                .jsonPath()
                .getList(".", ProductEntity.class);

        assertThat(productList, not(empty()));

        //TODO verify received data
    }

    @Test(priority = 2)
    public void findAllProductTest(){
        List<ProductEntity> productList =
            RestAssured.given()
                .contentType(ContentType.JSON)
                .header("Authorization", testDataDealer.getType() + " " + testDataDealer.getToken())
            .when()
                .get("http://localhost:8080/api/product/all")
            .then()
                .statusCode(200)
                .extract()
                .body()
                .jsonPath()
                .getList(".", ProductEntity.class);

        assertThat(productList, not(empty()));

        //TODO verify received data
    }

    @Test(priority = 3)
    public void findBoughtProductTest(){
        List<ProductEntity> productList =
            given()
                .contentType(ContentType.JSON)
                .header("Authorization", testDataDealer.getType() + " " + testDataDealer.getToken())
            .when()
                .get("http://localhost:8080/api/product/bought")
            .then()
                .statusCode(200)
                .extract()
                .body()
                .jsonPath()
                .getList(".", ProductEntity.class);

        assertThat(productList, not(empty()));

        //TODO verify received data
    }

}
