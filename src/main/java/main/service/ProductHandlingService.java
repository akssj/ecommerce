package main.service;

import main.data.entity.ProductEntity;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface ProductHandlingService {
    ProductEntity saveProduct(ProductEntity productEntity);
    ProductEntity updateProduct(ProductEntity productEntity);
    ProductEntity buyProduct(ProductEntity productEntity);
    void deleteProduct(Long id);
}
