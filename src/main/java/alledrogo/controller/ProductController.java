package alledrogo.controller;

import alledrogo.security.jwt.JwtUtils;
import alledrogo.data.entity.ProductEntity;
import alledrogo.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Api endpoint class, provides /product endpoint to search database.
 */
@RestController
@RequestMapping("/product")
public class ProductController {
    private final ProductService productService;
    private final JwtUtils jwtUtils;

    @Autowired
    public ProductController(ProductService productService, JwtUtils jwtUtils) {
        this.productService = productService;
        this.jwtUtils = jwtUtils;
    }
    @GetMapping("/forSale")
    public List<ProductEntity> findForSaleProducts() {
        return productService.findForSaleProduct();
    }
    @GetMapping("/category/{category}")
    public List<ProductEntity> findFilteredProducts( @PathVariable String category) {
        return productService.findFilteredProducts(category);
    }
    @GetMapping("/bought")
    public  ResponseEntity<?> findBoughtProducts(@CookieValue(name = "token") String token) {
        return ResponseEntity.ok(productService.findBoughtProducts(jwtUtils.getUserNameFromJwtToken(token)));
    }
    @GetMapping("/sold")
    public  ResponseEntity<?> findSoldProducts(@CookieValue(name = "token") String token) {
        return ResponseEntity.ok(productService.findSoldProducts(jwtUtils.getUserNameFromJwtToken(token)));
    }
    @GetMapping("/all")
    public List<ProductEntity> findAllProducts(){
        return productService.findAllProduct();
    }
}
