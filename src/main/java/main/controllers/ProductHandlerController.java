package main.controllers;

import jakarta.validation.Valid;
import main.dto.request.AddProductRequest;
import main.dto.response.MessageResponse;
import main.entity.ProductEntity;
import main.entity.UserEntity;
import main.repository.ProductRepository;
import main.repository.UserRepository;
import main.security.jwt.JwtUtils;
import main.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api/product/handling")
public class ProductHandlerController {
    private final ProductService productService;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    public ProductHandlerController(ProductService productService, ProductRepository productRepository, UserRepository userRepository) {
        this.productService = productService;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    @PostMapping("/add")
    public ResponseEntity<?> saveProduct(@Valid @RequestBody AddProductRequest addProductRequest, @RequestHeader(name = "Authorization") String token) {

        ProductEntity newProduct = new ProductEntity(
            addProductRequest.getName(),
            addProductRequest.getPrice(),
            addProductRequest.getDescription(),
            jwtUtils.getUserNameFromJwtToken(token)
        );
        productRepository.save(newProduct);

        return ResponseEntity.ok(new MessageResponse("Item added"));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable Long id, @RequestHeader(name = "Authorization") String token) {

        Logger logger = Logger.getLogger("DevLog ");
        logger.setLevel(Level.INFO);

        logger.info("=======");
        logger.info("token: " + token);
        logger.info("username: " + jwtUtils.getUserNameFromJwtToken(token));
        logger.info("item id: " + id);
        logger.info("=======");

        Optional<ProductEntity> productEntity = productRepository.findById(id);

        if (productEntity.isPresent()) {
            ProductEntity product = productEntity.get();

            if (product.getCreator().equals(jwtUtils.getUserNameFromJwtToken(token))) {
                productService.deleteProduct(id);
                return ResponseEntity.ok(true);
            }else {
                return ResponseEntity.badRequest().body(new MessageResponse("You do not own this item"));
            }

        }else {
            return ResponseEntity.badRequest().body(new MessageResponse("Item does not exist"));
        }
    }

    @PutMapping("/buy/{id}")
    public ResponseEntity<?> buyoutProduct(@PathVariable Long id, @RequestHeader(name = "Authorization") String token) {

        Optional<ProductEntity> ProductEntity = productRepository.findById(id);
        Optional<UserEntity> BuyerUserEntity = userRepository.findByUsername(jwtUtils.getUserNameFromJwtToken(token));

        if (ProductEntity.isEmpty()) {
            return ResponseEntity.badRequest().body(new MessageResponse("Item does not exist!"));
        }
        if (BuyerUserEntity.isEmpty()) {
            return ResponseEntity.badRequest().body(new MessageResponse("User does not exist"));
        }

        ProductEntity product = ProductEntity.get();
        UserEntity Buyer = BuyerUserEntity.get();
        Optional<UserEntity> OwnerUserEntity = userRepository.findByUsername(product.getCreator());

        if (!product.getBuyer().equals("")){
            return ResponseEntity.badRequest().body(new MessageResponse("Item is no longer available for sale!"));
        }
        if (product.getCreator().equals(jwtUtils.getUserNameFromJwtToken(token))){
            return ResponseEntity.badRequest().body(new MessageResponse("You can not buy your items!"));
        }
        if (!(Buyer.getBalance() >= product.getPrice())){
            return ResponseEntity.badRequest().body(new MessageResponse("You have insufficient balance!"));
        }
        if (OwnerUserEntity.isEmpty()) {
            return ResponseEntity.badRequest().body(new MessageResponse("Seller no longer exist!"));
            //TODO delete all items that exist without active creator
        }

        UserEntity Owner = OwnerUserEntity.get();

        try{
            product.setBuyer(jwtUtils.getUserNameFromJwtToken(token));
            productRepository.save(product);

            Buyer.setBalance(Buyer.getBalance() - product.getPrice());
            userRepository.save(Buyer);

            Owner.setBalance(Owner.getBalance() + product.getPrice());
            userRepository.save(Owner);

            return ResponseEntity.ok(new MessageResponse("Product bought!"));
        } catch(Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse("Something went wrong!"));
        }
    }
}
