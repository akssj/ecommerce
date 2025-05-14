package alledrogo.data.repository;

import alledrogo.data.entity.OrderEntity;
import alledrogo.data.entity.UserEntity;
import alledrogo.service.OrderProjection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, Long> {
    List<OrderEntity> findByBuyer(UserEntity buyer);
    List<OrderProjection> findAllByBuyer(UserEntity buyer);

}
