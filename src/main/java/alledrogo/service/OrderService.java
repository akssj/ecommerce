package alledrogo.service;

import alledrogo.data.entity.OrderEntity;
import alledrogo.io.request.OrderRequest;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface OrderService {



    List<OrderEntity> getOrdersForUser(String username);


    public void placeOrder(OrderRequest request, String buyerUsername) throws Exception;

    List<OrderEntity> getOrdersForBuyer(String buyerUsername);

    List<OrderProjection> getOrderProjectionsForUser(String username);

}
