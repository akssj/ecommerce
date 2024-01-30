package alledrogo.service;

import alledrogo.data.entity.ProductEntity;

import java.util.List;
/**
 * ProductService interface
 */
public interface ProductService {
    List<ProductEntity> findForSaleProduct();
    List<ProductEntity> findFilteredProducts(String category);
    List<ProductEntity> findBoughtProducts(String username);
    List<ProductEntity> findSoldProducts(String username);
    List<ProductEntity> findAllProduct();

    ProductEntity findById(Long id);
    List<ProductEntity> findByName(String name);
    boolean existsById(Long id);
}
