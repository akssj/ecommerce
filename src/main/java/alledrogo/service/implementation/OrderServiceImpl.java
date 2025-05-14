package alledrogo.service.implementation;

import alledrogo.data.entity.OrderEntity;
import alledrogo.data.entity.ProductEntity;
import alledrogo.data.entity.UserEntity;
import alledrogo.data.repository.OrderRepository;
import alledrogo.data.repository.ProductRepository;
import alledrogo.data.repository.UserRepository;
import alledrogo.io.request.OrderRequest;
import alledrogo.service.OrderProjection;
import alledrogo.service.OrderService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository, ProductRepository productRepository,
            UserRepository userRepository) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void placeOrder(OrderRequest request, String username) throws Exception {
        UserEntity buyer = userRepository.findFirstByUsername(username)
                .orElseThrow(() -> new RuntimeException("Nie znaleziono użytkownika o nazwie: " + username));

        ProductEntity product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new Exception("Produkt nie istnieje"));

        if (product.getBuyer() != null) {
            throw new Exception("Produkt został już kupiony");
        }

        OrderEntity order = new OrderEntity();
        order.setProduct(product);
        order.setBuyer(buyer);
        order.setCountry(request.getCountry());
        order.setCity(request.getCity());
        order.setStreet(request.getStreet());
        order.setPostalCode(request.getPostalCode());
        order.setPaymentMethod(request.getPaymentMethod());
        order.setOrderDate(new Date());
        order.setBuyerUsername(buyer.getUsername());
        order.setSellerUsername(product.getSeller().getUsername());
        order.setProductName(product.getName());

        product.setBuyer(buyer);
        product.setSold(true);

        productRepository.save(product);
        orderRepository.save(order);
    }

    @Override
    public List<OrderEntity> getOrdersForBuyer(String username) {
        UserEntity buyer = userRepository.findFirstByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));

        return orderRepository.findByBuyer(buyer);
    }

    @Override
    public List<OrderEntity> getOrdersForUser(String username) {
        UserEntity buyer = userRepository.findFirstByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));
        return orderRepository.findByBuyer(buyer);
    }

    @Override
    public List<OrderProjection> getOrderProjectionsForUser(String username) {
        UserEntity buyer = userRepository.findFirstByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));
        return orderRepository.findAllByBuyer(buyer);
    }

}
