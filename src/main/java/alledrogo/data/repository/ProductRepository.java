package alledrogo.data.repository;

import alledrogo.data.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * ProductRepository interface
 */
@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Long> {
    List<ProductEntity> findByName(String name);
    @Query("SELECT p FROM ProductEntity p WHERE p.buyer IS NULL AND p.name LIKE %:name%")
    List<ProductEntity> findProductByName(@Param("name") String name);
    boolean existsById(Long id);
}
