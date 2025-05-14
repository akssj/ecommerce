package alledrogo.controller;

import alledrogo.io.request.AddProductRequest;
import alledrogo.security.jwt.JwtUtils;
import alledrogo.service.ProductCategoryService;
import alledrogo.service.ProductHandlingService;
import alledrogo.service.ProductService;
import alledrogo.service.UserService;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest
public class ProductHandlerControllerTest {

    private static final String BASE_URL = "http://localhost:8080";

    @BeforeClass
    public void setup() {
        RestAssured.baseURI = BASE_URL;
    }

    @Mock
    private ProductService productService;

    @Mock
    private ProductHandlingService productHandlingService;

    @Mock
    private UserService userService;

    @Mock
    private ProductCategoryService productCategoryService;

    @Mock
    private JwtUtils jwtUtils;

    @InjectMocks
    private ProductHandlerController productHandlerController;

    private String authToken;

    @BeforeMethod
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test(priority = 1)
    public void testSaveProduct() {
        AddProductRequest request = new AddProductRequest();
        request.setNewProductName("Test Product");
        request.setNewProductPrice(99.99f);
        request.setNewProductDescription("Test Description");
        request.setNewProductCategory("Test Category");

        when(productCategoryService.isCategoryValid(anyString())).thenReturn(true);
        when(jwtUtils.getUserNameFromJwtToken(anyString())).thenReturn("testUser");

        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(request)
                .cookie("token", authToken)
                .when()
                .post("/product/handling/add");

        response.then()
                .statusCode(200);
    }

    @Test(priority = 2)
    public void testDeleteProduct() {
        Long productId = 1L;

        when(jwtUtils.getUserNameFromJwtToken(anyString())).thenReturn("testUser");

        Response response = RestAssured.given()
                .cookie("token", authToken)
                .when()
                .delete("/product/handling/" + productId + "/delete");

        response.then()
                .statusCode(200);
    }

    @Test(priority = 3)
    public void testBuyProduct() {
        Long productId = 1L;

        when(jwtUtils.getUserNameFromJwtToken(anyString())).thenReturn("testUser");

        Response response = RestAssured.given()
                .cookie("token", authToken)
                .when()
                .put("/product/handling/" + productId + "/buy");

        response.then()
                .statusCode(200);
    }
}
