package alledrogo.service;

import alledrogo.data.entity.ProductEntity;

import java.util.List;
/**
 * ProductService interface
 */
public interface ProductService {
    List<ProductProjection> findForSaleProduct();
    List<ProductProjection> findProductByCategory(String category);
    List<ProductProjection> findProductByName(String name);
    List<ProductProjection> findMyProducts(String username);
    List<ProductProjection> findBoughtProducts(String username);
    List<ProductProjection> findSoldProducts(String username);

    List<ProductEntity> findAllProduct();
    ProductEntity findById(Long id);
    boolean existsById(Long id);
}
