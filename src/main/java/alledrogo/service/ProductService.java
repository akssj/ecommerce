package alledrogo.service;

import alledrogo.data.entity.ProductEntity;

import java.util.List;
/**
 * ProductService interface
 */
public interface ProductService {
    List<ProductEntity> findForSaleProduct();
    List<ProductEntity> findProductByCategory(String category);
    List<ProductEntity> findProductByName(String name);
    List<ProductEntity> findMyProducts(String username);
    List<ProductEntity> findBoughtProducts(String username);
    List<ProductEntity> findSoldProducts(String username);
    List<ProductEntity> findAllProduct();

    ProductEntity findById(Long id);
    boolean existsById(Long id);
}
