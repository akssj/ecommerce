package alledrogo.service;

import alledrogo.data.entity.ProductEntity;

import java.util.List;

public interface ProductService {
    List<ProductEntity> findAllProduct();
    List<ProductEntity> findForSaleProduct();
    List<ProductEntity> findBoughtProducts(String username);
    List<ProductEntity> findSoldProducts(String username);
    ProductEntity findById(Long id);
    List<ProductEntity> findByName(String name);
    boolean existsById(Long id);
}
