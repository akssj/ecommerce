package main.controllers;

import main.dto.response.MessageResponse;
import main.entity.ProductEntity;
import main.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import main.security.jwt.JwtUtils;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/product")
public class ProductController {
    private final ProductService productService;
    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/forSale")
    public List<ProductEntity> findForSaleProduct() {
        List<ProductEntity> allProducts = productService.findAllProduct();
        List<ProductEntity> filteredProducts = new ArrayList<>();

        for (ProductEntity product : allProducts) {
            if ("".equals(product.getBuyer())) {
                filteredProducts.add(product);
            }
        }
        return filteredProducts;
    }

    @GetMapping("/all")
    public List<ProductEntity> findAllProduct(){return productService.findAllProduct();}

    //TODO change it to GET as its unnecessary to receive data from user here
    @GetMapping("/bought")
    public  ResponseEntity<?> findBoughtProduct(@RequestHeader(name = "Authorization") String token) {
        try {
            List<ProductEntity> allProducts = productService.findAllProduct();
            List<ProductEntity> filteredProducts = new ArrayList<>();

            String usernameFromToken = jwtUtils.getUserNameFromJwtToken(token);

            for (ProductEntity product : allProducts) {
                if (usernameFromToken.equals(product.getBuyer())) {
                    filteredProducts.add(product);
                }
            }
            return ResponseEntity.ok(filteredProducts);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error retrieving products"));
        }
    }

}
