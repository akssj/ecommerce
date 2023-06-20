package main.service;

import main.entity.ProductEntity;
import main.dto.request.ProductRequest;
import main.dto.response.ProductResponse;

import java.util.List;
import java.util.Optional;

public interface ProductService {
    List<ProductEntity> findAllProduct();
    Optional<ProductEntity> findById(Long id);
    List<ProductEntity> findByName(String name);
    ProductEntity saveProduct(ProductEntity productEntity);
    void deleteProduct(Long id);

}
