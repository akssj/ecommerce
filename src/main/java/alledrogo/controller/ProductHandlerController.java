package alledrogo.controller;

import alledrogo.data.entity.UserEntity;
import alledrogo.data.entity.ProductEntity;

import alledrogo.io.request.AddProductRequest;
import alledrogo.io.request.OrderRequest;
import alledrogo.io.response.MessageResponse;

import alledrogo.security.jwt.JwtUtils;

import alledrogo.service.ProductHandlingService;
import alledrogo.service.ProductService;
import alledrogo.service.UserService;
import alledrogo.service.OrderService;
import alledrogo.service.ProductCategoryService;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * Api endpoint class, provides /product/handling endpoint to manipulate data in database.
 */
@RestController
@RequestMapping("product/handling")
@PreAuthorize("hasRole('USER') or hasRole('VIP_USER') or hasRole('ADMIN')")
public class ProductHandlerController {
    private final ProductService productService;
    private final ProductHandlingService productHandlingService;
    private final UserService userService;
    private final ProductCategoryService productCategoryService;
    private final JwtUtils jwtUtils;

    @Autowired
    public ProductHandlerController(ProductService productService, ProductHandlingService productHandlingService, UserService userService, ProductCategoryService productCategoryService, JwtUtils jwtUtils) {
        this.productService = productService;
        this.productHandlingService = productHandlingService;
        this.userService = userService;
        this.productCategoryService = productCategoryService;
        this.jwtUtils = jwtUtils;
    }

    /**
     * Creates new ProductEntity object filled with addProductRequest data and saves it into the database
     * @param addProductRequest NotNull obj. with data for ProductEntity
     * @param token token of logged in user.
     * @return ResponseEntity obj. with MessageResponse obj. containing message
     */
    @PostMapping("/add")
    public ResponseEntity<?> saveProduct(@Valid @RequestBody AddProductRequest addProductRequest, @CookieValue(name = "token") String token) {

        if (!productCategoryService.isCategoryValid(addProductRequest.getNewProductCategory())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Invalid category."));
        }

        ProductEntity newProduct = new ProductEntity(
            addProductRequest.getNewProductName(),
            addProductRequest.getNewProductPrice(),
            addProductRequest.getNewProductDescription(),
            addProductRequest.getNewProductCategory(),
            userService.findByUsername(jwtUtils.getUserNameFromJwtToken(token))
        );

        boolean createdProduct = productHandlingService.saveProduct(newProduct);

        if (createdProduct){
            return ResponseEntity.ok(new MessageResponse("Item added."));
        }else {
            return ResponseEntity.ok(new MessageResponse("Something went wrong!"));
        }

    }

    /**
     * Check if current user is owner of the item, if so deletes ProductEntity object from the database.
     * @param id LONG id of the object to delete
     * @param token token of logged in user.
     * @return ResponseEntity obj. with MessageResponse obj. containing message
     */
    @DeleteMapping("/{id}/delete")
    public ResponseEntity<?> deleteProduct(@PathVariable Long id, @CookieValue(name = "token") String token){

        ProductEntity productEntity = productService.findById(id);

        if (!productEntity.getSeller().getUsername().equals(jwtUtils.getUserNameFromJwtToken(token))) {
            return ResponseEntity.badRequest().body(new MessageResponse("You do not own this item."));
        }

        boolean deleteProduct = productHandlingService.deleteProduct(id);

        if (deleteProduct){
            return ResponseEntity.ok(new MessageResponse("Item deleted."));
        }else {
            return ResponseEntity.ok(new MessageResponse("Something went wrong!"));
        }
    }

    /**
     * Checks if user can buy item and if so "buys" item for the current user and transfers funds between accounts.
     * @param id LONG id of the object to buy
     * @param token token of logged in user.
     * @return ResponseEntity obj. with MessageResponse obj. containing message
     */
    @PutMapping("/{id}/buy")
    public ResponseEntity<?> buyProduct(@PathVariable Long id, @CookieValue(name = "token") String token) throws Exception{
        String buyerUsername = jwtUtils.getUserNameFromJwtToken(token);
        UserEntity userBuyer = userService.findByUsername(buyerUsername);
        ProductEntity productEntity = productService.findById(id);

        if (productEntity.getBuyer() != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageResponse("Item is no longer available for sale!"));
        }
        if (productEntity.getSeller().getUsername().equals(buyerUsername)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new MessageResponse("You cannot buy your own items!"));
        }

        //TODO paying for products

        productEntity.setBuyer(userBuyer);
        productEntity.setSold(true);
        productHandlingService.buyProduct(productEntity);

        return ResponseEntity.ok(new MessageResponse("Product bought!"));
    }

    @PutMapping("/{id}/update")
public ResponseEntity<?> updateProduct(@PathVariable Long id, @RequestBody ProductEntity updatedData,
                                       @CookieValue(name = "token") String token) {
    String username = jwtUtils.getUserNameFromJwtToken(token);
    ProductEntity product = productService.findById(id);

    if (!product.getSeller().getUsername().equals(username)) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new MessageResponse("Nie masz uprawnień do edycji tego produktu."));
    }

    product.setName(updatedData.getName());
    product.setPrice(updatedData.getPrice());
    product.setDescription(updatedData.getDescription());
    product.setCategory(updatedData.getCategory());

    boolean updated = productHandlingService.saveProduct(product);
    if (updated) {
        return ResponseEntity.ok(new MessageResponse("Produkt zaktualizowany."));
    } else {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MessageResponse("Nie udało się zaktualizować produktu."));
    }
}

@Autowired
private OrderService orderService;

@PostMapping("/order")
@PreAuthorize("hasRole('USER') or hasRole('VIP_USER') or hasRole('ADMIN')")
public ResponseEntity<?> placeOrder(@RequestBody OrderRequest request, @CookieValue(name = "token") String token) {
    try {
        String buyerUsername = jwtUtils.getUserNameFromJwtToken(token);
        orderService.placeOrder(request, buyerUsername);
        return ResponseEntity.ok(new MessageResponse("Zamówienie zostało zapisane!"));
    } catch (Exception e) {
        return ResponseEntity.badRequest().body(new MessageResponse("Błąd podczas składania zamówienia: " + e.getMessage()));
    }
}




}
