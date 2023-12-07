package main.service;

import main.data.entity.ProductEntity;

public interface ProductHandlingService {
    ProductEntity saveProduct(ProductEntity productEntity);
    ProductEntity updateProduct(ProductEntity productEntity);
    ProductEntity buyProduct(ProductEntity productEntity);
    void deleteProduct(Long id);
}
