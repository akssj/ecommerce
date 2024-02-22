package alledrogo.service.implementation;

import alledrogo.data.entity.UserEntity;
import alledrogo.data.repository.ProductRepository;
import alledrogo.service.ProductProjection;
import jakarta.persistence.EntityNotFoundException;
import alledrogo.data.entity.ProductEntity;
import alledrogo.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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

    private ProductProjection mapToProjection(ProductEntity product) {
        return new ProductProjection() {
            @Override
            public Long getId() {
                return product.getId();
            }
            @Override
            public String getName() {
                return product.getName();
            }
            @Override
            public Float getPrice() {
                return product.getPrice();
            }
            @Override
            public String getDescription() {
                return product.getDescription();
            }
            @Override
            public String getCategory() {
                return product.getCategory();
            }
            @Override
            public String getSeller() {
                return product.getSeller().getUsername();
            }
            @Override
            public String getBuyer() {
                UserEntity buyer = product.getBuyer();
                return (buyer != null) ? buyer.getUsername() : null;
            }
            @Override
            public Boolean isSold(){return product.isSold();}
        };
    }

    /**
     * Returns not sold products
     * @return list of products where String Buyer field equals ""
     */
    @Override
    public List<ProductProjection> findForSaleProduct() {
        List<ProductEntity> allProducts = productRepository.findAll();
        return allProducts.stream()
                .filter(product -> !product.isSold())
                .map(this::mapToProjection)
                .collect(Collectors.toList());
    }

    /**
     * Returns not sold products from single category
     * @param category String name of category
     * @return list of products where String Buyer field equals "" and category equals provided category
     */
    @Override
    public List<ProductProjection> findProductByCategory(String category) {
        List<ProductEntity> allProducts = productRepository.findAll();

        return allProducts.stream()
                .filter(product -> !product.isSold() && category.equals(product.getCategory()))
                .map(this::mapToProjection)
                .collect(Collectors.toList());
    }

    /**
     * Returns not sold products with similar name
     * @param name String name
     * @return list of products where name is similar to provided string
     */
    @Override
    public List<ProductProjection> findProductByName(String name) {
        List<ProductEntity> products = productRepository.findProductByName(name);
        return products.stream()
                .map(this::mapToProjection)
                .collect(Collectors.toList());
    }

    /**
     * Returns products created by the user
     * @param username string
     * @return list of products where Creator field equals "username"
     */
    @Override
    public List<ProductProjection> findMyProducts(String username) {
        List<ProductEntity> allProducts = productRepository.findAll();
        return allProducts.stream()
                .filter(product -> username.equals(product.getSeller().getUsername()))
                .map(this::mapToProjection)
                .collect(Collectors.toList());
    }


    /**
     * Returns products bought by the user
     * @param username string
     * @return list of products where Buyer field equals "username"
     */
    @Override
    public List<ProductProjection> findBoughtProducts(String username) {
        List<ProductEntity> allProducts = productRepository.findAll();
        return allProducts.stream()
                .filter(product -> {
                    UserEntity buyer = product.getBuyer();
                    return buyer != null && username.equals(buyer.getUsername());
                })
                .map(this::mapToProjection)
                .collect(Collectors.toList());
    }



    /**
     * Returns products sold by the user
     * @param username string
     * @return list of products where creator field equals "username" and buyer field is not null
     */
    @Override
    public List<ProductProjection> findSoldProducts(String username) {
        List<ProductEntity> allProducts = productRepository.findAll();
        return allProducts.stream()
                .filter(product -> product.isSold() && username.equals(product.getSeller().getUsername()))
                .map(this::mapToProjection)
                .collect(Collectors.toList());
    }



    @Override
    public List<ProductEntity> findAllProduct() {
        return productRepository.findAll();
    }

    @Override
    public ProductEntity findById(Long id) {
        return productRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("Product not found with id: " + id));
    }

    @Override
    public boolean existsById(Long id) {
        return productRepository.existsById(id);
    }
}
