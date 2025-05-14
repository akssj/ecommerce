package alledrogo.controller;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.springframework.boot.test.context.SpringBootTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.nio.file.Files;
import java.nio.file.Paths;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@SpringBootTest
public class StaticResourceControllerTest {

    private static final String BASE_URL = "http://localhost:8080";

    @BeforeClass
    public void setup() {
        RestAssured.baseURI = BASE_URL;
    }

    @Test(priority = 1)
    public void testGetStyleFile() throws Exception {
        String expectedContent = new String(Files.readAllBytes(Paths.get("src/main/resources/static/styles.css")));

        Response response = given()
                .contentType(ContentType.JSON)
                .when()
                .get("/static/styles.css");

        response.then()
                .statusCode(200)
                .body(notNullValue())
                .body(equalTo(expectedContent));
    }

    @Test(priority = 2)
    public void testGetModuleJsFile() throws Exception {
        String fileName = "module.js";
        String expectedContent = new String(Files.readAllBytes(Paths.get("src/main/resources/static/" + fileName)));

        Response response = given()
                .contentType(ContentType.JSON)
                .when()
                .get("/static/scripts/" + fileName);

        response.then()
                .statusCode(200)
                .body(notNullValue())
                .body(equalTo(expectedContent));
    }

    @Test(priority = 3)
    public void testGetAuthJsFile() throws Exception {
        String fileName = "auth.js";
        String expectedContent = new String(Files.readAllBytes(Paths.get("src/main/resources/static/scripts/" + fileName)));

        Response response = given()
                .contentType(ContentType.JSON)
                .when()
                .get("/static/scripts/" + fileName);

        response.then()
                .statusCode(200)
                .body(notNullValue())
                .body(equalTo(expectedContent));
    }

    @Test(priority = 4)
    public void testGetProductJsFile() throws Exception {
        String fileName = "product.js";
        String expectedContent = new String(Files.readAllBytes(Paths.get("src/main/resources/static/scripts/" + fileName)));

        Response response = given()
                .contentType(ContentType.JSON)
                .when()
                .get("/static/scripts/" + fileName);

        response.then()
                .statusCode(200)
                .body(notNullValue())
                .body(equalTo(expectedContent));
    }
}
