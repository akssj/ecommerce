package alledrogo.service.implementation;

import alledrogo.data.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import alledrogo.data.entity.ProductEntity;
import alledrogo.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public List<ProductEntity> findAllProduct() {
        return productRepository.findAll();
    }
    @Override
    public List<ProductEntity> findForSaleProduct() {
        List<ProductEntity> allProducts = productRepository.findAll();
        List<ProductEntity> filteredProducts = new ArrayList<>();

        for (ProductEntity product : allProducts) {
            if ("".equals(product.getBuyer())) {
                filteredProducts.add(product);
            }
        }
        return filteredProducts;
    }
    @Override
    public List<ProductEntity> findBoughtProducts(String username) {
        List<ProductEntity> allProducts = productRepository.findAll();
        List<ProductEntity> filteredProducts = new ArrayList<>();

        for (ProductEntity product : allProducts) {
            if (username.equals(product.getBuyer())) {
                filteredProducts.add(product);
            }
        }
        return filteredProducts;
    }
    @Override
    public List<ProductEntity> findSoldProducts(String username) {
        List<ProductEntity> allProducts = productRepository.findAll();
        List<ProductEntity> filteredProducts = new ArrayList<>();

        for (ProductEntity product : allProducts) {
            if (username.equals(product.getCreator()) && !product.getBuyer().isEmpty()) {
                filteredProducts.add(product);
            }
        }
        return filteredProducts;
    }
    @Override
    public ProductEntity findById(Long id) {
        return productRepository.findById(id).orElseThrow(() ->
            new EntityNotFoundException("Product not found with id: " + id));}
    @Override
    public List<ProductEntity> findByName(String name) {
        return productRepository.findByName(name);
    }
    @Override
    public boolean existsById(Long id) {
        return productRepository.existsById(id);
    }
}
