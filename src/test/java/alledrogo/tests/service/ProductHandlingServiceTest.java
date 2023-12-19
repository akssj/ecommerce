package alledrogo.tests.service;

import alledrogo.data.entity.ProductEntity;
import alledrogo.data.repository.ProductRepository;
import alledrogo.service.implementation.ProductHandlingServiceImpl;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static org.mockito.Mockito.*;
import static org.testng.Assert.*;

/**
 * Tests (Unit) for ProductHandlingServiceImpl.
 * Creates @Mock and @InjectMocks to verify data handling by ProductHandlingServiceImpl and ProductRepository.
 * Goal is to verify that each of these methods are working as intended (verification scenarios).
 */
public class ProductHandlingServiceTest {

    @Mock
    private ProductRepository productRepository;
    @InjectMocks
    private ProductHandlingServiceImpl productHandlingServiceImpl;

    private final ProductEntity productEntity = new ProductEntity("MockItemName", 600, "MockItemDesc", "MockCreator");
    @BeforeClass
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * Verify ProductHandlingServiceImpl.saveProduct(productEntity) with mockProduct.
     * Check if required methods were called only once.
     * expected result: Saves new mock item.
     */
    @Test(priority = 1)
    protected void saveProductTest() {
         when(productRepository.save(productEntity)).thenReturn(productEntity);

        ProductEntity result = productHandlingServiceImpl.saveProduct(productEntity);

        assertNotNull(result);
        assertEquals("MockItemName", result.getName());
        assertEquals(600, result.getPrice());
        assertEquals("MockItemDesc", result.getDescription());
        assertEquals("MockCreator", result.getCreator());

        verify(productRepository, times(1)).save(productEntity);
    }

    /**
     * Verify ProductHandlingServiceImpl.updateProduct(productEntity) with mockProduct.
     * Check if required methods were called only once.
     * expected result: Updates mock item.
     */
    @Test(priority = 2)
    protected void updateProductTest() {
        productEntity.setName("updated_name");
        productEntity.setDescription("updated_desc");
        productEntity.setPrice(100);

        when(productRepository.save(productEntity)).thenReturn(productEntity);

        ProductEntity result = productHandlingServiceImpl.updateProduct(productEntity);

        assertEquals("updated_name", result.getName());
        assertEquals("updated_desc", result.getDescription());
        assertEquals(100, result.getPrice());

        verify(productRepository, times(2)).save(productEntity);
    }

    /**
     * Verify ProductHandlingServiceImpl.buyProduct(productEntity) with mockProduct.
     * Check if required methods were called only once.
     * expected result: Buys mock item for mock user.
     */
    @Test(priority = 3)
    protected void buyProductTest() {
        productEntity.setBuyer("MockBuyer");

        when(productRepository.save(productEntity)).thenReturn(productEntity);

        ProductEntity result = productHandlingServiceImpl.buyProduct(productEntity);

        assertNotNull(result);
        assertEquals("updated_name", result.getName());
        assertEquals(100, result.getPrice());
        assertEquals("updated_desc", result.getDescription());
        assertEquals("MockCreator", result.getCreator());
        assertEquals("MockBuyer", result.getBuyer());

        verify(productRepository, times(3)).save(productEntity);
    }

    /**
     * Verify ProductHandlingServiceImpl.deleteProduct(id) with mockProduct.
     * Check if required methods were called only once.
     * expected result: Deletes mock item.
     */
    @Test(priority = 4)
    protected void deleteProductTest() {
        doNothing().when(productRepository).deleteById(anyLong());

        productHandlingServiceImpl.deleteProduct(0L);

        verify(productRepository, times(1)).deleteById(anyLong());
    }

}
