package main.controllers;

import main.dto.request.BuyOutProductRequest;
import main.dto.request.DeleteProductRequest;
import main.dto.request.UserStatusRequest;
import main.dto.response.MessageResponse;
import main.entity.ProductEntity;
import main.dto.request.ProductRequest;
import main.entity.UserEntity;
import main.repository.ProductRepository;
import main.repository.UserRepository;
import main.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import main.security.jwt.JwtUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/product")
public class ProductController {
    private final ProductService productService;
    private final ProductRepository productRepository;
    private final JwtUtils jwtUtils;
    private final UserRepository userRepository;

    @Autowired
    public ProductController(ProductService productService, ProductRepository productRepository, JwtUtils jwtUtils,
                             UserRepository userRepository) {
        this.productService = productService;
        this.productRepository = productRepository;
        this.jwtUtils = jwtUtils;
        this.userRepository = userRepository;
    }

    //GET ting items from site /api/product
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



    //POST adding items /api/product/add
    @PostMapping("/add")
    public ProductEntity saveProduct(@RequestBody ProductRequest productRequest) {
        return productRepository.save(
                new ProductEntity(
                        productRequest.getName(),
                        productRequest.getPrice(),
                        productRequest.getDescription(),
                        productRequest.getCreator_username()
                )
        );
    }

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

    //Delete items /api/product/delete/{id}
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteProduct(@RequestBody DeleteProductRequest deleteProductRequest) {

        Optional<ProductEntity> productEntity = productRepository.findById(deleteProductRequest.getItem_id());

        if (productEntity.isPresent()) {

            ProductEntity product = productEntity.get();

            if (product.getCreator().equals(deleteProductRequest.getCreator_username())) {
                productService.deleteProduct(deleteProductRequest.getItem_id());
                return ResponseEntity.ok(true);
            }
        }
        return ResponseEntity.badRequest().body(new MessageResponse("Something went wrong!"));
    }

    //But items /api/product/buyout/{id}
    @PutMapping("/buyout/{id}")
    public ResponseEntity<?> buyoutProduct(@RequestBody BuyOutProductRequest buyOutProductRequest) {

        Optional<ProductEntity> productEntity = productRepository.findById(buyOutProductRequest.getItem_id());
        Optional<UserEntity> userEntity = userRepository.findByUsername(buyOutProductRequest.getBuyer_username());

        if (productEntity.isPresent() && userEntity.isPresent()) {

            ProductEntity product = productEntity.get();
            UserEntity user = userEntity.get();

            if (    !product.getBuyer().equals(buyOutProductRequest.getBuyer_username()) &&
                    !product.getCreator().equals(buyOutProductRequest.getBuyer_username()) &&
                    product.getBuyer().equals("") &&
                    user.getBalance() >= product.getPrice()) {

                product.setBuyer(buyOutProductRequest.getBuyer_username());
                productRepository.save(product);

                user.setBalance(user.getBalance() - product.getPrice());
                userRepository.save(user);

                return ResponseEntity.ok(new MessageResponse("Product bought!"));
            }
        }
        return ResponseEntity.badRequest().body(new MessageResponse("Something went wrong!"));
    }
}
