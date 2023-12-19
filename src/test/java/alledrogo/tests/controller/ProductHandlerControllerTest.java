package alledrogo.tests.controller;

import alledrogo.tests.dataset.TestDataDealer;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.json.JSONException;
import org.json.JSONObject;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

/**
 * Tests (API, integration) for /product/handling/* endpoints.
 * Goal is to verify that each of these endpoints are working as intended (verification scenarios).
 */
public class ProductHandlerControllerTest {
    private static final TestDataDealer testDataDealer = new TestDataDealer();

    private final JSONObject testJsonRequest = new JSONObject();
    private Long id;
    @BeforeClass
    private void beforeClass(){
        try {
            testJsonRequest.put("name", "test_name");
            testJsonRequest.put("price", 9999);
            testJsonRequest.put("description", "test_description");
        } catch (JSONException e) {
            throw new RuntimeException(e);
            //TODO handle it
        }
    }

    /**
     * sends correct GET request to /add endpoint.
     * expected result: Respond from server is a success, user receives message from the server alongside created item id.
     */
    @Test(priority = 1)
    public void saveProductTest() {
        Response response =
        RestAssured.given()
            .contentType(ContentType.JSON)
            .header("Authorization", testDataDealer.getType() + " " + testDataDealer.getToken())
            .body(testJsonRequest.toString())
        .when()
            .post("http://localhost:8080/product/handling/add")
        .then()
            .statusCode(200)
            .body("message", equalTo("Item added."))
        .extract()
        .response();

        id = Long.parseLong(response.path("id").toString());
    }

    /**
     * sends correct DELETE request to /{id}/delete endpoint.
     * expected result: Respond from server is a success, item is deleted.
     */
    @Test(priority = 3)
    public void deleteProductTest() {
        given()
            .contentType(ContentType.JSON)
            .header("Authorization", testDataDealer.getType() + " " + testDataDealer.getToken())
        .when()
            .delete("http://localhost:8080/product/handling/" + id + "/delete")
        .then()
            .statusCode(200)
            .body("message", equalTo("Item deleted."));
    }

    //TODO make another test user to buy this product
    /**
     * sends correct PUT request to /{id}/buy endpoint.
     * expected result: Respond from server is a success, item is bought.
     */
    @Test(priority = 2)
    public void buyProductTest() {
        given()
            .contentType(ContentType.JSON)
            .header("Authorization", testDataDealer.getType() + " " + testDataDealer.getToken())
        .when()
            .put("http://localhost:8080/product/handling/" + id + "/buy")
        .then()
            .statusCode(200)
            .body("message", equalTo("Product bought!"));
    }

}
