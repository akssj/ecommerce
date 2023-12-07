package main.controllers;

import jakarta.validation.Valid;
import main.dto.request.BuyProductRequest;
import main.dto.request.DeleteProductRequest;
import main.dto.request.AddProductRequest;
import main.dto.response.MessageResponse;
import main.entity.ProductEntity;
import main.entity.UserEntity;
import main.repository.ProductRepository;
import main.repository.UserRepository;
import main.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/product/handling")
public class ProductHandlerController {
    private final ProductService productService;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    @Autowired
    public ProductHandlerController(ProductService productService, ProductRepository productRepository, UserRepository userRepository) {
        this.productService = productService;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    @PostMapping("/add")
    public ResponseEntity<?> saveProduct(@Valid @RequestBody AddProductRequest addProductRequest) {

        ProductEntity newProduct = new ProductEntity(
            addProductRequest.getName(),
            addProductRequest.getPrice(),
            addProductRequest.getDescription(),
            addProductRequest.getCreator_username()
        );
        productRepository.save(newProduct);

        return ResponseEntity.ok(new MessageResponse("Item added"));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteProduct(@Valid @RequestBody DeleteProductRequest deleteProductRequest) {

        Optional<ProductEntity> productEntity = productRepository.findById(deleteProductRequest.getItem_id());

        if (productEntity.isPresent()) {
            ProductEntity product = productEntity.get();

            if (product.getCreator().equals(deleteProductRequest.getCreator_username())) {
                productService.deleteProduct(deleteProductRequest.getItem_id());
                return ResponseEntity.ok(true);
            }else {
                return ResponseEntity.badRequest().body(new MessageResponse("You do not own this item"));
            }

        }else {
            return ResponseEntity.badRequest().body(new MessageResponse("Item does not exist"));
        }
    }

    @PutMapping("/buy/{id}")
    public ResponseEntity<?> buyoutProduct(@Valid @RequestBody BuyProductRequest buyProductRequest) {

        Optional<ProductEntity> ProductEntity = productRepository.findById(buyProductRequest.getItem_id());
        Optional<UserEntity> BuyerUserEntity = userRepository.findByUsername(buyProductRequest.getBuyer_username());

        if (ProductEntity.isEmpty() && BuyerUserEntity.isEmpty()) {
            return ResponseEntity.badRequest().body(new MessageResponse("Something went wrong!"));
        }

        ProductEntity product = ProductEntity.get();
        UserEntity Buyer = BuyerUserEntity.get();
        Optional<UserEntity> OwnerUserEntity = userRepository.findByUsername(product.getCreator());

        if (!product.getBuyer().equals("")){
            return ResponseEntity.badRequest().body(new MessageResponse("Item is no longer available for sale!"));
        }
        if (product.getCreator().equals(buyProductRequest.getBuyer_username())){
            return ResponseEntity.badRequest().body(new MessageResponse("You can not buy your items!"));
        }
        if (!(Buyer.getBalance() >= product.getPrice())){
            return ResponseEntity.badRequest().body(new MessageResponse("You have insufficient balance!"));
        }
        if (OwnerUserEntity.isEmpty()) {
            return ResponseEntity.badRequest().body(new MessageResponse("Something went wrong!"));
        }

        UserEntity Owner = OwnerUserEntity.get();

        try{
            product.setBuyer(buyProductRequest.getBuyer_username());
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
