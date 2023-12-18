package alledrogo.tests.service;

import alledrogo.data.entity.ProductEntity;
import alledrogo.data.repository.ProductRepository;
import alledrogo.service.implementation.ProductServiceImpl;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;
import static org.testng.Assert.*;

/**
 * Tests (Unit) for ProductServiceImpl.
 * Creates @Mock and @InjectMocks to verify data handling by ProductServiceImpl and productRepository.
 * Goal is to verify that each of these methods are working as intended (verification scenarios).
 */
public class ProductServiceTest {
    @Mock
    private ProductRepository productRepository;
    @InjectMocks
    private ProductServiceImpl productServiceImpl;

    private List<ProductEntity> mockProducts;
    private void setUpMockProducts(){
        mockProducts = Arrays.asList(
                new ProductEntity("TempTestProduct01", 100, "TempTestDescription01", "TempTestUser01"),
                new ProductEntity("TempTestProduct02", 200, "TempTestDescription02", "TempTestUser02"),
                new ProductEntity("TempTestProduct03", 300, "TempTestDescription03", "TempTestUser03"),
                new ProductEntity("TempTestProduct04", 400, "TempTestDescription04", "TempTestUser04"),
                new ProductEntity("TempTestProduct", 500, "TempTestDescription05", "TempTestUser05"),
                new ProductEntity("SoldTempTestProduct", 600, "TempTestDescription", "TempTestUser"),
                new ProductEntity("SoldTempTestProduct", 700, "TempTestDescription", "TempTestUser"),
                new ProductEntity("TempTestProduct", 800, "TempTestDescription", "TempTestUser")
        );
    }

    @BeforeClass
    void setUp() {
        MockitoAnnotations.openMocks(this);
        setUpMockProducts();
        mockProducts.get(5).setBuyer("TestBuyer1");
        mockProducts.get(6).setBuyer("TestBuyer2");
    }

    /**
     * Verify productServiceImpl.findAllProduct() with mockProducts.
     * Check if required methods were called only once.
     * expected result: Receive list of mockProducts.
     */
    @Test(priority = 1)
    protected void findAllProductTest() {
        when(productRepository.findAll()).thenReturn(mockProducts);

        List<ProductEntity> result = productServiceImpl.findAllProduct();

        assertEquals(mockProducts.size(), result.size());

        for (int i = 0; i < mockProducts.size(); i++) {
            ProductEntity expectedProducts = mockProducts.get(i);
            ProductEntity actualProducts = result.get(i);

            assertEquals(expectedProducts.getId(), actualProducts.getId());
            assertEquals(expectedProducts.getName(), actualProducts.getName());
            assertEquals(expectedProducts.getPrice(), actualProducts.getPrice());
            assertEquals(expectedProducts.getDescription(), actualProducts.getDescription());
            assertEquals(expectedProducts.getCreator(), actualProducts.getCreator());
            assertEquals(expectedProducts.getBuyer(), actualProducts.getBuyer());
        }

        verify(productRepository, times(1)).findAll();
    }

    /**
     * Verify productServiceImpl.findForSaleProduct() with mockProducts.
     * Check if required methods were called only once.
     * expected result: Receive list of mockProducts available to buy.
     */
    @Test(priority = 2)
    protected void findForSaleProductTest() {
        when(productRepository.findAll()).thenReturn(mockProducts);

        List<ProductEntity> result = productServiceImpl.findForSaleProduct();

        assertEquals(6, result.size());
        for (ProductEntity actualProducts : result) {
            assertEquals("", actualProducts.getBuyer());
        }

        verify(productRepository, times(2)).findAll();
    }

    /**
     * Verify productServiceImpl.findBoughtProducts(username) with mockProducts.
     * Check if required methods were called only once.
     * expected result: Receive list of mockProducts bought by the user.
     */
    @Test(priority = 3)
    protected void findBoughtProductsTest() {

        when(productRepository.findAll()).thenReturn(mockProducts);

        List<ProductEntity> result = productServiceImpl.findBoughtProducts("TestBuyer1");

        assertEquals(result.size(), 1);

        assertEquals("TestBuyer1", result.get(0).getBuyer());

        verify(productRepository, times(3)).findAll();
    }

    /**
     * Verify productServiceImpl.findSoldProducts(username) with mockUser.
     * Check if required methods were called only once.
     * expected result: Receive list of mockProducts sold by the user.
     */
    @Test(priority = 4)
    protected void findSoldProductsTest() {

        when(productRepository.findAll()).thenReturn(mockProducts);

        List<ProductEntity> result = productServiceImpl.findSoldProducts("TempTestUser");

        assertEquals(result.size(), 2);

        assertEquals("TestBuyer1", result.get(0).getBuyer());

        verify(productRepository, times(4)).findAll();
    }

    /**
     * Verify productServiceImpl.findById(id) with mockUser.
     * Check if required methods were called only once.
     * expected result: Receive single mockProduct with associated id.
     */
    @Test(priority = 5)
    protected void findByIdTest() {
        when(productRepository.findById(4L)).thenReturn(Optional.of(mockProducts.get(4)));

        ProductEntity expectedProduct = mockProducts.get(4);
        ProductEntity result = productServiceImpl.findById(4L);

        assertNotNull(result);
        assertEquals(result, expectedProduct);

        verify(productRepository, times(1)).findById(4L);
    }

    /**
     * Verify productServiceImpl.findByName(username) with mockUser.
     * Check if required methods were called only once.
     * expected result: Receive list of mockProducts with provided name.
     */
    @Test(priority = 6)
    protected void findByNameTest() {
        String productName = "TempTestProduct";

        when(productRepository.findByName(productName)).thenReturn(Arrays.asList(mockProducts.get(4), mockProducts.get(7)));

        List<ProductEntity> result = productServiceImpl.findByName(productName);

        assertEquals(result.size(), 2);

        verify(productRepository, times(1)).findByName(productName);
    }

    /**
     * Verify productServiceImpl.existsById(id) with mockUser.
     * Check if required methods were called only once.
     * expected result: Receive boolean true if user exists.
     */
    @Test(priority = 7)
    protected void existsByIdTest() {
        Long productId = 3L;

        when(productRepository.existsById(productId)).thenReturn(true);

        boolean result = productServiceImpl.existsById(productId);

        assertTrue(result);

        verify(productRepository, times(1)).existsById(productId);
    }
}
