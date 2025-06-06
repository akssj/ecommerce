package alledrogo.service.implementation;

import alledrogo.data.entity.CategoryEntity;
import alledrogo.data.entity.ProductEntity;
import alledrogo.data.entity.UserEntity;
import alledrogo.data.repository.ProductRepository;
import alledrogo.service.ProductCategoryService;
import alledrogo.service.ProductProjection;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.testng.Assert.*;

public class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductCategoryService productCategoryService;

    @InjectMocks
    private ProductServiceImpl productService;

    private final UserEntity seller = new UserEntity();
    private final UserEntity buyer = new UserEntity();
    private List<ProductEntity> mockProducts;

    @BeforeClass
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        seller.setUsername("TestUser");
        buyer.setUsername("buyer");
        mockProducts = List.of(
                new ProductEntity("Test_Item_0", 100.0f, "DescA", "PowerTools", seller),
                new ProductEntity("Test_Item_1", 200.0f, "DescB", "PowerTools", seller),
                new ProductEntity("Test_Item_2", 300.0f, "DescC", "Rings", seller),
                new ProductEntity("Sold_item", 400.0f, "DescD", "Biographies", seller)
        );

        CategoryEntity powerTools = new CategoryEntity("Tools");
        powerTools.addSubCategory("PowerTools");

        when(productCategoryService.getCategories()).thenReturn(List.of(powerTools));

        mockProducts.get(3).setBuyer(buyer);
        mockProducts.get(3).setSold(true);
    }

    @Test(priority = 1)
    public void testFindForSaleProduct() {
        when(productRepository.findAll()).thenReturn(mockProducts);

        List<ProductProjection> result = productService.findForSaleProduct();

        assertEquals(3, result.size());
        for (ProductProjection projection : result) {
            assertFalse(projection.isSold());
        }
    }

    @Test(priority = 2)
    public void testFindProductByCategory() {
        String category = "PowerTools";
        when(productRepository.findAll()).thenReturn(mockProducts);

        List<ProductProjection> result = productService.findProductByCategory(category);

        assertEquals(2, result.size());
        for (ProductProjection projection : result) {
            assertEquals(category, projection.getCategory());
        }
    }

    @Test(priority = 3)
    public void testFindProductByName() {
        String name = "Test_Item_1";
        when(productRepository.findProductByName(name)).thenReturn(List.of(mockProducts.get(1)));

        List<ProductProjection> result = productService.findProductByName(name);

        assertEquals(1, result.size());
        assertEquals(name, result.get(0).getName());
    }

    @Test(priority = 4)
    public void testFindMyProducts() {
        String username = "TestUser";
        when(productRepository.findAll()).thenReturn(mockProducts);

        List<ProductProjection> result = productService.findMyProducts(username);

        assertEquals(3, result.size());
        for (ProductProjection projection : result) {
            assertEquals(username, projection.getSeller());
        }
    }

    @Test(priority = 5)
    public void testFindBoughtProducts() {
        String username = "buyer";
        when(productRepository.findAll()).thenReturn(mockProducts);

        List<ProductProjection> result = productService.findBoughtProducts(username);

        assertEquals(1, result.size());
        assertEquals(username, result.get(0).getBuyer());
    }

    @Test(priority = 6)
    public void testFindSoldProducts() {
        String username = "TestUser";
        when(productRepository.findAll()).thenReturn(mockProducts);

        List<ProductProjection> result = productService.findSoldProducts(username);

        assertEquals(1, result.size());
        assertEquals(username, result.get(0).getSeller());
    }

    @Test(priority = 7)
    public void testFindAllProduct() {
        when(productRepository.findAll()).thenReturn(mockProducts);

        List<ProductEntity> result = productService.findAllProduct();

        assertEquals(mockProducts.size(), result.size());
        assertEquals(mockProducts, result);
    }

    @Test(priority = 8)
    public void testFindById() {
        Long idToFind = 1L;
        ProductEntity productToFind = mockProducts.get(0);
        when(productRepository.findById(idToFind)).thenReturn(Optional.of(productToFind));

        ProductEntity result = productService.findById(idToFind);

        assertNotNull(result);
        assertEquals(productToFind, result);
    }

    @Test(priority = 9)
    public void testExistsById() {
        Long idToCheck = 1L;
        when(productRepository.existsById(idToCheck)).thenReturn(true);

        boolean result = productService.existsById(idToCheck);

        assertTrue(result);
    }

}
