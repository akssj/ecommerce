package alledrogo.controller;

import alledrogo.io.request.AddProductRequest;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.springframework.boot.test.context.SpringBootTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static org.hamcrest.Matchers.equalTo;

@SpringBootTest
public class ProductHandlerControllerTest {

    private static final String BASE_URL = "http://localhost:8080";
    private String authToken;
    private String authToken2;

    @BeforeClass
    public void setup() {
        RestAssured.baseURI = BASE_URL;
        authToken = authenticate("test", "test");
        authToken2 = authenticate("test2", "test2");
    }

    private String authenticate(String username, String password) {
        String body = String.format("{\"username\": \"%s\", \"password\": \"%s\"}", username, password);

        Response response = RestAssured.given()
                .contentType("application/json")
                .body(body)
                .post("/auth/login");

        response.then().statusCode(200);
        return response.getCookie("token");
    }


    @Test(priority = 1)
    public void testSaveProductWithValidCategory() {
        AddProductRequest request = new AddProductRequest();
        request.setNewProductName("Valid Product");
        request.setNewProductPrice(150.0f);
        request.setNewProductDescription("Valid Description");
        request.setNewProductCategory("Laptops");

        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .cookie("token", authToken)
                .body(request)
                .post("/product/handling/add");

        response.then()
                .statusCode(200)
                .body("message", equalTo("Item added."));
    }

    @Test(priority = 2)
    public void testDeleteProduct() {
        Response getResponse = RestAssured.given()
                .contentType(ContentType.JSON)
                .when()
                .get("/product/Test Product/name");

        getResponse.then().statusCode(200);

        Long productId = getResponse.jsonPath().getLong("[0].id");

        Response deleteResponse = RestAssured.given()
                .cookie("token", authToken)
                .when()
                .delete("/product/handling/" + productId + "/delete");

        deleteResponse.then()
                .statusCode(200);
    }

    @Test(priority = 3)
    public void testSaveProductWithAnotherValidCategory() {
        AddProductRequest request = new AddProductRequest();
        request.setNewProductName("Valid Product 2");
        request.setNewProductPrice(150.0f);
        request.setNewProductDescription("Valid Description");
        request.setNewProductCategory("PaperProducts");

        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .cookie("token", authToken2)
                .body(request)
                .post("/product/handling/add");

        response.then()
                .statusCode(200)
                .body("message", equalTo("Item added."));
    }

    @Test(priority = 4)
    public void testBuyProduct() {
        Response getResponse = RestAssured.given()
                .contentType(ContentType.JSON)
                .when()
                .get("/product/Valid Product 2/name");

        getResponse.then().statusCode(200);

        Long productId = getResponse.jsonPath().getLong("[0].id");

        Response buyResponse = RestAssured.given()
                .cookie("token", authToken)
                .when()
                .put("/product/handling/" + productId + "/buy");

        buyResponse.then()
                .statusCode(200);
    }
}
