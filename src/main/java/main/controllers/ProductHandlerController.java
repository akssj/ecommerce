package main.controllers;

import jakarta.validation.Valid;
import main.io.request.AddProductRequest;
import main.io.response.MessageResponse;
import main.data.entity.ProductEntity;
import main.data.entity.UserEntity;
import main.security.jwt.JwtUtils;
import main.service.ProductHandlingService;
import main.service.ProductService;
import main.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/product/handling")
public class ProductHandlerController {
    private final ProductService productService;
    private final ProductHandlingService productHandlingService;
    private final UserService userService;
    private final JwtUtils jwtUtils;

    @Autowired
    public ProductHandlerController(ProductService productService, ProductHandlingService productHandlingService, UserService userService, JwtUtils jwtUtils) {
        this.productService = productService;
        this.productHandlingService = productHandlingService;
        this.userService = userService;
        this.jwtUtils = jwtUtils;
    }

    @PostMapping("/add")
    public ResponseEntity<?> saveProduct(@Valid @RequestBody AddProductRequest addProductRequest, @RequestHeader(name = "Authorization") String token) {

        ProductEntity newProduct = new ProductEntity(
            addProductRequest.getName(),
            addProductRequest.getPrice(),
            addProductRequest.getDescription(),
            jwtUtils.getUserNameFromJwtToken(token)
        );
        productHandlingService.saveProduct(newProduct);

        return ResponseEntity.ok(new MessageResponse("Item added"));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable Long id, @RequestHeader(name = "Authorization") String token) {

        Optional<ProductEntity> productEntity = productService.findById(id);

        if (productEntity.isPresent()) {
            ProductEntity product = productEntity.get();

            if (product.getCreator().equals(jwtUtils.getUserNameFromJwtToken(token))) {
                productHandlingService.deleteProduct(id);
                return ResponseEntity.ok(true);
            }else {
                return ResponseEntity.badRequest().body(new MessageResponse("You do not own this item"));
            }
        }else {
            return ResponseEntity.badRequest().body(new MessageResponse("Item does not exist"));
        }
    }

    @PutMapping("/buy/{id}")
    public ResponseEntity<?> buyProduct(@PathVariable Long id, @RequestHeader(name = "Authorization") String token) {

        //TODO fix that

        if (!userService.existsByUsername(jwtUtils.getUserNameFromJwtToken(token))) {
            return ResponseEntity.badRequest().body(new MessageResponse("User does not exist"));
        }
        if (!productService.existsById(id)) {
            return ResponseEntity.badRequest().body(new MessageResponse("Item does not exist!"));
        }

        UserEntity userEntityBuyer = userService.findByUsername(jwtUtils.getUserNameFromJwtToken(token)).get();
        ProductEntity productEntity = productService.findById(id).get();

        if (!productEntity.getBuyer().equals("")){
            return ResponseEntity.badRequest().body(new MessageResponse("Item is no longer available for sale!"));
        }
        if (productEntity.getCreator().equals(jwtUtils.getUserNameFromJwtToken(token))){
            return ResponseEntity.badRequest().body(new MessageResponse("You can not buy your items!"));
        }
        if (!(userEntityBuyer.getBalance() >= productEntity.getPrice())){
            return ResponseEntity.badRequest().body(new MessageResponse("You have insufficient funds!"));
        }

        if (userService.findByUsername(productEntity.getCreator()).isEmpty()) {
            return ResponseEntity.badRequest().body(new MessageResponse("Seller no longer exists"));
            //TODO delete all items that exist without active creator
        }

        UserEntity userEntityProductOwner = userService.findByUsername(productEntity.getCreator()).get();

        try{
            productEntity.setBuyer(jwtUtils.getUserNameFromJwtToken(token));
            productHandlingService.buyProduct(productEntity);

            userEntityBuyer.setBalance(userEntityBuyer.getBalance() - productEntity.getPrice());
            userService.createUser(userEntityBuyer);

            userEntityProductOwner.setBalance(userEntityProductOwner.getBalance() + productEntity.getPrice());
            userService.createUser(userEntityProductOwner);

            return ResponseEntity.ok(new MessageResponse("Product bought!"));
        } catch(Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse("Something went wrong!"));
        }
    }
}
