package alledrogo.controller;

import io.restassured.RestAssured;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import static io.restassured.RestAssured.given;
import static org.mockito.Mockito.when;

@SpringBootTest
public class CustomErrorControllerTest {

    private static final String BASE_URL = "http://localhost:8080";

    @BeforeClass
    public void setup() {
        RestAssured.baseURI = BASE_URL;
    }
    @Mock
    private HttpServletRequest request;

    @BeforeMethod
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testHandleError404() {
        when(request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE)).thenReturn(HttpStatus.NOT_FOUND);

        given()
                .when()
                .get("/error")
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    public void testHandleError500() {
        when(request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE)).thenReturn(HttpStatus.INTERNAL_SERVER_ERROR);

        given()
                .when()
                .get("/error")
                .then()
                .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    @Test
    public void testHandleError401() {
        when(request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE)).thenReturn(HttpStatus.UNAUTHORIZED);

        given()
                .when()
                .get("/error")
                .then()
                .statusCode(HttpStatus.UNAUTHORIZED.value());
    }

    @Test
    public void testHandleError403() {
        when(request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE)).thenReturn(HttpStatus.FORBIDDEN);

        given()
                .when()
                .get("/error")
                .then()
                .statusCode(HttpStatus.FORBIDDEN.value());
    }

    @Test
    public void testHandleErrorNoStatusCode() {
        when(request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE)).thenReturn(null);

        given()
                .when()
                .get("/error")
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }
}
