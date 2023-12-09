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
@RequestMapping("/product/handling")
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
        //TODO verify if save is a success

        return ResponseEntity.ok(new MessageResponse("Item added."));
    }

    @DeleteMapping("/{id}/delete")
    public ResponseEntity<?> deleteProduct(@PathVariable Long id, @RequestHeader(name = "Authorization") String token) {

        Optional<ProductEntity> productEntity = productService.findById(id);

        if (productEntity.isPresent()) {
            ProductEntity product = productEntity.get();

            if (product.getCreator().equals(jwtUtils.getUserNameFromJwtToken(token))) {
                productHandlingService.deleteProduct(id);
                //TODO verify if delete is a success

                return ResponseEntity.ok(new MessageResponse("Item deleted."));
            }else {
                return ResponseEntity.badRequest().body(new MessageResponse("You do not own this item."));
            }
        }else {
            return ResponseEntity.badRequest().body(new MessageResponse("Item does not exist."));
        }
    }

    @PutMapping("/{id}/buy")
    public ResponseEntity<?> buyProduct(@PathVariable Long id, @RequestHeader(name = "Authorization") String token) {

        //TODO fix that

        String buyerUsername = jwtUtils.getUserNameFromJwtToken(token);

        if (!userService.existsByUsername(buyerUsername)) {
            return ResponseEntity.badRequest().body(new MessageResponse("User does not exist"));
        }
        if (!productService.existsById(id)) {
            return ResponseEntity.badRequest().body(new MessageResponse("Item does not exist!"));
        }

        UserEntity userEntityBuyer = userService.findByUsername(buyerUsername);
        ProductEntity productEntity = productService.findById(id).get();
        Integer productPrice = productEntity.getPrice();

        if (!productEntity.getBuyer().equals("")){
            return ResponseEntity.badRequest().body(new MessageResponse("Item is no longer available for sale!"));
        }
        if (productEntity.getCreator().equals(buyerUsername)){
            return ResponseEntity.badRequest().body(new MessageResponse("You can not buy your items!"));
        }
        if (!(userEntityBuyer.getBalance() >= productPrice)){
            return ResponseEntity.badRequest().body(new MessageResponse("You have insufficient funds!"));
        }

        UserEntity userEntityProductOwner = userService.findByUsername(productEntity.getCreator());

        try{
            productEntity.setBuyer(buyerUsername);
            productHandlingService.buyProduct(productEntity);

            userEntityBuyer.setBalance(userEntityBuyer.getBalance() - productPrice);
            userService.updateUser(userEntityBuyer);

            userEntityProductOwner.setBalance(userEntityProductOwner.getBalance() + productPrice);
            userService.updateUser(userEntityProductOwner);

            //TODO verify if operation is a success

            return ResponseEntity.ok(new MessageResponse("Product bought!"));
        } catch(Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse("Something went wrong!." + e));
        }
    }
}
