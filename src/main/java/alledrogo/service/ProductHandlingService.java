package alledrogo.service;

import alledrogo.data.entity.ProductEntity;
import org.springframework.transaction.annotation.Transactional;

/**
 * ProductHandlingService interface
 */
@Transactional
public interface ProductHandlingService {
    boolean saveProduct(ProductEntity productEntity);
    ProductEntity updateProduct(ProductEntity productEntity);
    ProductEntity buyProduct(ProductEntity productEntity);
    boolean deleteProduct(Long id);
}
