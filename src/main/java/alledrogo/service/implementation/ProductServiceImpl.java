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

    /**
     * Returns not sold products
     * @return list of products where String Buyer field equals ""
     */
    @Override
    public List<ProductEntity> findForSaleProduct() {
        List<ProductEntity> allProducts = productRepository.findAll();
        List<ProductEntity> filteredProducts = new ArrayList<>();

        for (ProductEntity product : allProducts) {
            if (product.getBuyer() == null) {
                filteredProducts.add(product);
            }
        }
        return filteredProducts;
    }

    /**
     * Returns not sold products from single category
     * @param category String name of category
     * @return list of products where String Buyer field equals "" and category equals provided category
     */
    @Override
    public List<ProductEntity> findProductByCategory(String category) {
        List<ProductEntity> allProducts = productRepository.findAll();
        List<ProductEntity> filteredProducts = new ArrayList<>();

        for (ProductEntity product : allProducts) {
            if (product.getBuyer() == null && category.equals(product.getCategory())) {
                filteredProducts.add(product);
            }
        }
        return filteredProducts;
    }

    /**
     * Returns not sold products with similar name
     * @param name String name
     * @return list of products where name is similar to provided string
     */
    @Override
    public List<ProductEntity> findProductByName(String name) {
        return productRepository.findProductByName(name);
    }

    /**
     * Returns products created by the user
     * @param username string
     * @return list of products where Creator field equals "username"
     */
    @Override
    public List<ProductEntity> findMyProducts(String username) {
        List<ProductEntity> allProducts = productRepository.findAll();
        List<ProductEntity> filteredProducts = new ArrayList<>();

        for (ProductEntity product : allProducts) {
            if (username.equals(product.getCreator().getUsername())) {
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
            if (username.equals(product.getCreator().getUsername())) {
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
            if (username.equals(product.getCreator().getUsername()) && product.getCreator() != null) {
                filteredProducts.add(product);
            }
        }
        return filteredProducts;
    }

    @Override
    public List<ProductEntity> findAllProduct() {
        return productRepository.findAll();
    }

    @Override
    public ProductEntity findById(Long id) {
        return productRepository.findById(id).orElseThrow(() ->
            new EntityNotFoundException("Product not found with id: " + id));}

    @Override
    public boolean existsById(Long id) {
        return productRepository.existsById(id);
    }
}
