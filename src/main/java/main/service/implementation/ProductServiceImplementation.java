package main.service.implementation;

import main.entity.ProductEntity;
import main.repository.ProductRepository;
import main.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImplementation implements ProductService {

    private final ProductRepository productRepository;

    @Autowired
    public ProductServiceImplementation(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }
    @Override
    public List<ProductEntity> findAllProduct() {
        return productRepository.findAll();
    }
    @Override
    public Optional<ProductEntity> findById(Long id) {return productRepository.findById(id);}
    @Override
    public List<ProductEntity> findByName(String name) {
        return productRepository.findByName(name);
    }
    @Override
    public ProductEntity saveProduct(ProductEntity productEntity) {
        return productRepository.save(productEntity);
    }
    @Override
    public ProductEntity updateProduct(ProductEntity productEntity) {
        return productRepository.save(productEntity);
    }
    @Override
    public ProductEntity buyProduct(ProductEntity productEntity) {
        return productRepository.save(productEntity);
    }
    @Override
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

}
