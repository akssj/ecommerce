package alledrogo.controller;

import alledrogo.data.entity.UserEntity;
import alledrogo.io.request.AddProductRequest;
import alledrogo.io.response.MessageResponse;
import alledrogo.security.jwt.JwtUtils;
import alledrogo.service.ProductHandlingService;
import alledrogo.service.ProductService;
import alledrogo.service.UserService;
import jakarta.validation.Valid;
import alledrogo.data.entity.ProductEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Api endpoint class, provides /product/handling endpoint to manipulate products in database.
 */
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

    /**
     * Creates new ProductEntity object filled with addProductRequest data and puts it into the database
     * @param addProductRequest NotNull obj. with data for ProductEntity
     * @param token token of logged in user.
     * @return ResponseEntity obj. with MessageResponse obj. containing message
     */
    @PostMapping("/add")
    public ResponseEntity<?> saveProduct(@Valid @RequestBody AddProductRequest addProductRequest, @RequestHeader(name = "Authorization") String token) {

        ProductEntity newProduct = new ProductEntity(
            addProductRequest.getName(),
            addProductRequest.getPrice(),
            addProductRequest.getDescription(),
            jwtUtils.getUserNameFromJwtToken(token)
        );

        ProductEntity createdProduct = productHandlingService.saveProduct(newProduct);

        if (productService.existsById(createdProduct.getId())){
            return ResponseEntity.ok(new MessageResponse("Item added."));
        }else {
            return ResponseEntity.ok(new MessageResponse("Something went wrong!"));
        }

    }

    /**
     * Check if current user is owner of the item if so deletes ProductEntity object from the database.
     * @param id LONG id of the object to delete
     * @param token token of logged in user.
     * @return ResponseEntity obj. with MessageResponse obj. containing message
     */
    @DeleteMapping("/{id}/delete")
    public ResponseEntity<?> deleteProduct(@PathVariable Long id, @RequestHeader(name = "Authorization") String token){

        ProductEntity productEntity = productService.findById(id);

        if (!productEntity.getCreator().equals(jwtUtils.getUserNameFromJwtToken(token))) {
            return ResponseEntity.badRequest().body(new MessageResponse("You do not own this item."));
        }

        productHandlingService.deleteProduct(id);

        if (!productService.existsById(productEntity.getId())){
            return ResponseEntity.ok(new MessageResponse("Item deleted."));
        }else {
            return ResponseEntity.ok(new MessageResponse("Something went wrong!"));
        }
    }

    /**
     * Checks if user can buy item and if so sets "buys" item for the current user and transfers funds between accounts.
     * @param id LONG id of the object to buy
     * @param token token of logged in user.
     * @return ResponseEntity obj. with MessageResponse obj. containing message
     */
    @PutMapping("/{id}/buy")
    public ResponseEntity<?> buyProduct(@PathVariable Long id, @RequestHeader(name = "Authorization") String token){

        String buyerUsername = jwtUtils.getUserNameFromJwtToken(token);
        System.out.println(buyerUsername);
        UserEntity userEntityBuyer = userService.findByUsername(buyerUsername);
        ProductEntity productEntity = productService.findById(id);
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

        UserEntity userEntitySeller = userService.findByUsername(productEntity.getCreator());

        try{
            productEntity.setBuyer(buyerUsername);
            productHandlingService.buyProduct(productEntity);

            userEntityBuyer.setBalance(userEntityBuyer.getBalance() - productPrice);
            userService.updateUser(userEntityBuyer);

            userEntitySeller.setBalance(userEntitySeller.getBalance() + productPrice);
            userService.updateUser(userEntitySeller);

            //TODO verify if operation is a success

            return ResponseEntity.ok(new MessageResponse("Product bought!"));
        } catch(Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse("Something went wrong!." + e));
        }
    }
}
