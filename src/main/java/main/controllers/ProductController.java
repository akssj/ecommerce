package main.controllers;

import main.dto.request.UserStatusRequest;
import main.entity.ProductEntity;
import main.repository.ProductRepository;
import main.repository.UserRepository;
import main.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import main.security.jwt.JwtUtils;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/product")
public class ProductController {
    private final ProductService productService;

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
    @PostMapping("/bought")
    public List<ProductEntity> findBoughtProduct(@RequestBody UserStatusRequest userStatusRequest) {
        List<ProductEntity> allProducts = productService.findAllProduct();
        List<ProductEntity> filteredProducts = new ArrayList<>();

        for (ProductEntity product : allProducts) {
            if (userStatusRequest.getUsername().equals(product.getBuyer())) {
                filteredProducts.add(product);
            }
        }
        return filteredProducts;
    }
}
