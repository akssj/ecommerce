package alledrogo.tests.controller;

import alledrogo.tests.dataset.TestDataDealer;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import alledrogo.data.entity.ProductEntity;
import org.testng.annotations.Test;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * Tests (API, integration) for /product/* endpoints.
 * Goal is to verify that each of these endpoints are working as intended (verification scenarios).
 */
public class ProductControllerTest {

    private static final TestDataDealer testDataDealer = new TestDataDealer();

    /**
     * sends correct GET request to /forSale endpoint.
     * expected result: Receive list of non-sold items.
     */
    @Test(priority = 1)
    public void findForSaleProductTest() {
        List<ProductEntity> productList =
            given()
                .contentType(ContentType.JSON)
            .when()
                .get("http://localhost:8080/product/forSale")
            .then()
                .statusCode(200)
                .extract()
                .body()
                .jsonPath()
                .getList(".", ProductEntity.class);

        assertThat(productList, not(empty()));

        //TODO verify received data
    }

    /**
     * sends already logged-in user data (testDataDealer.token) in GET request to /all endpoint.
     * expected result: Receive list of all items.
     */
    @Test(priority = 2)
    public void findAllProductTest(){
        List<ProductEntity> productList =
            RestAssured.given()
                .contentType(ContentType.JSON)
                .header("Authorization", testDataDealer.getType() + " " + testDataDealer.getToken())
            .when()
                .get("http://localhost:8080/product/all")
            .then()
                .statusCode(200)
                .extract()
                .body()
                .jsonPath()
                .getList(".", ProductEntity.class);

        assertThat(productList, not(empty()));

        //TODO verify received data
    }

    /**
     * sends already logged-in user data (testDataDealer.token) in GET request to /bought endpoint.
     * expected result: Receive list of items bought by current user.
     */
    @Test(priority = 3)
    public void findBoughtProductTest(){
        List<ProductEntity> productList =
            given()
                .contentType(ContentType.JSON)
                .header("Authorization", testDataDealer.getType() + " " + testDataDealer.getToken())
            .when()
                .get("http://localhost:8080/product/bought")
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
