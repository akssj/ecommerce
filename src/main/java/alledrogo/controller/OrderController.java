package alledrogo.controller;

import alledrogo.io.request.OrderRequest;
import alledrogo.io.response.MessageResponse;
import alledrogo.security.jwt.JwtUtils;
import alledrogo.service.OrderProjection;
import alledrogo.service.OrderService;
import alledrogo.service.ProductService;
import alledrogo.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;
    private final JwtUtils jwtUtils;

    @Autowired
    public OrderController(OrderService orderService, JwtUtils jwtUtils,
                           ProductService productService, UserService userService) {
        this.orderService = orderService;
        this.jwtUtils = jwtUtils;
    }

    @GetMapping("/bought")
    @PreAuthorize("hasRole('USER') or hasRole('VIP_USER') or hasRole('ADMIN')")
    public ResponseEntity<List<OrderProjection>> getBoughtOrders(@CookieValue(name = "token") String token) {
        String username = jwtUtils.getUserNameFromJwtToken(token);
        return ResponseEntity.ok(orderService.getOrderProjectionsForUser(username));
    }

    @PostMapping
@PreAuthorize("hasRole('USER') or hasRole('VIP_USER') or hasRole('ADMIN')")
public ResponseEntity<?> placeOrder(@Valid @RequestBody OrderRequest orderRequest,
                                    @CookieValue(name = "token") String token) {
    String username = jwtUtils.getUserNameFromJwtToken(token);
    try {
        orderService.placeOrder(orderRequest, username);
        return ResponseEntity.ok(new MessageResponse("Order placed successfully"));
    } catch (Exception e) {
        return ResponseEntity.badRequest().body(new MessageResponse("Order failed: " + e.getMessage()));
    }
}

}
