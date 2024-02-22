package alledrogo.service.implementation;

import alledrogo.data.entity.ProductEntity;
import alledrogo.data.repository.ProductRepository;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ProductHandlingServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductHandlingServiceImpl productHandlingService;

    @BeforeClass
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test(priority = 1)
    public void testSaveProduct() {
        ProductEntity productEntity = new ProductEntity();

        when(productRepository.save(any(ProductEntity.class))).thenReturn(productEntity);
        boolean result = productHandlingService.saveProduct(productEntity);

        verify(productRepository, times(1)).save(any(ProductEntity.class));
        assert result;
    }

    @Test(priority = 2)
    public void testUpdateProduct() {
        ProductEntity productEntity = new ProductEntity();

        when(productRepository.save(any(ProductEntity.class))).thenReturn(productEntity);
        ProductEntity result = productHandlingService.updateProduct(productEntity);

        verify(productRepository, times(2)).save(any(ProductEntity.class));
        assert result != null;
    }

    @Test(priority = 3)
    public void testBuyProduct() {
        ProductEntity productEntity = new ProductEntity();

        when(productRepository.save(any(ProductEntity.class))).thenReturn(productEntity);
        ProductEntity result = productHandlingService.buyProduct(productEntity);

        verify(productRepository, times(3)).save(any(ProductEntity.class));
        assert result != null;
    }

    @Test(priority = 4)
    public void testDeleteProduct() {
        Long productId = 1L;

        when(productRepository.existsById(productId)).thenReturn(true);
        boolean result = productHandlingService.deleteProduct(productId);

        verify(productRepository, times(1)).existsById(productId);
        verify(productRepository, times(1)).deleteById(productId);
        assert result;
    }
}
