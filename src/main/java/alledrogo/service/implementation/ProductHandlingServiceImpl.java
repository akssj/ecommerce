package alledrogo.service.implementation;

import alledrogo.data.repository.ProductRepository;
import alledrogo.service.ProductHandlingService;
import alledrogo.data.entity.ProductEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * ProductHandlingService Implementation class.
 * Overrides methods from ProductHandlingService.
 */
@Service
public class ProductHandlingServiceImpl implements ProductHandlingService {
    private final ProductRepository productRepository;

    @Autowired
    public ProductHandlingServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public boolean saveProduct(ProductEntity productEntity) {
        try {
            productRepository.save(productEntity);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
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
    public boolean deleteProduct(Long id) {
        if (productRepository.existsById(id)) {
            productRepository.deleteById(id);
            return true;
        }
        return false;
    }

}
