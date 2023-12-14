package alledrogo.service.implementation;

import alledrogo.data.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import alledrogo.data.entity.ProductEntity;
import alledrogo.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
/**
 * ProductService Implementation class.
 * Overrides methods from UserService introducing further logic.
 */
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

    /**
     * Returns not sold products
     * @return list of products where String Buyer field equals ""
     */
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

    /**
     * Returns products bought by the user
     * @param username string
     * @return list of products where Buyer field equals "username"
     */
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

    /**
     * Returns products sold by the user
     * @param username string
     * @return list of products where creator field equals "username" and buyer field is not null
     */
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
    public List<ProductEntity> findByName(String username) {
        return productRepository.findByName(username);
    }
    @Override
    public boolean existsById(Long id) {
        return productRepository.existsById(id);
    }
}
