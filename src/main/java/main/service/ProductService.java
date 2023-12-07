package main.service;

import main.entity.ProductEntity;

import java.util.List;
import java.util.Optional;

public interface ProductService {
    List<ProductEntity> findAllProduct();
    Optional<ProductEntity> findById(Long id);
    List<ProductEntity> findByName(String name);
    ProductEntity saveProduct(ProductEntity productEntity);
    ProductEntity updateProduct(ProductEntity productEntity);
    ProductEntity buyProduct(ProductEntity productEntity);
    void deleteProduct(Long id);

}
