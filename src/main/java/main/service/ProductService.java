package main.service;

import main.data.entity.ProductEntity;

import java.util.List;
import java.util.Optional;

public interface ProductService {
    List<ProductEntity> findAllProduct();
    List<ProductEntity> findForSaleProduct();
    List<ProductEntity> findBoughtProducts(String username);
    List<ProductEntity> findSoldProducts(String username);
    Optional<ProductEntity> findById(Long id);
    List<ProductEntity> findByName(String name);
    boolean existsById(Long id);
}
