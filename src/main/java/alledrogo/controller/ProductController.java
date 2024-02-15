package alledrogo.controller;

import alledrogo.data.entity.CategoryEntity;
import alledrogo.security.jwt.JwtUtils;
import alledrogo.data.entity.ProductEntity;
import alledrogo.service.ProductCategoryService;
import alledrogo.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;

/**
 * Api endpoint class, provides /product endpoint to search database.
 */
@RestController
@RequestMapping("/product")
public class ProductController {
    private final ProductService productService;
    private final ProductCategoryService productCategoryService;
    private final JwtUtils jwtUtils;

    @Autowired
    public ProductController(ProductService productService, ProductCategoryService productCategoryService, JwtUtils jwtUtils) {
        this.productService = productService;
        this.productCategoryService = productCategoryService;
        this.jwtUtils = jwtUtils;
    }
    @GetMapping("/forSale")
    public List<ProductEntity> findForSaleProducts() {
        return productService.findForSaleProduct();
    }
    @GetMapping("/categories")
    public Collection<CategoryEntity> getCategoryCollection() {
        return productCategoryService.getCategories();
    }
    @GetMapping("/{category}/category")
    public List<ProductEntity> findProductByCategory(@PathVariable String category) {
        return productService.findProductByCategory(category);
    }
    @GetMapping("/{name}/name")
    public List<ProductEntity> findProductByName(@PathVariable String name) {
        return productService.findProductByName(name);
    }
    @GetMapping("/my-products")
    public List<ProductEntity> findMyProducts(@CookieValue(name = "token") String token) {
        return productService.findMyProducts(jwtUtils.getUserNameFromJwtToken(token));
    }
    @GetMapping("/bought-products")
    public List<ProductEntity> findBoughtProducts(@CookieValue(name = "token") String token) {
        return productService.findBoughtProducts(jwtUtils.getUserNameFromJwtToken(token));
    }
    @GetMapping("/sold")
    public List<ProductEntity> findSoldProducts(@CookieValue(name = "token") String token) {
        return productService.findSoldProducts(jwtUtils.getUserNameFromJwtToken(token));
    }

}
