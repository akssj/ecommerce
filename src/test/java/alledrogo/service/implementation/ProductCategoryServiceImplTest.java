package alledrogo.service.implementation;

import alledrogo.data.entity.CategoryEntity;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Collection;

import static org.testng.Assert.*;

public class ProductCategoryServiceImplTest {

    private ProductCategoryServiceImpl productCategoryService;

    @BeforeMethod
    public void setUp() {
        productCategoryService = new ProductCategoryServiceImpl();
    }

    @Test
    public void testGetCategories() {
        Collection<CategoryEntity> result = productCategoryService.getCategories();

        assertNotNull(result);
        assertFalse(result.isEmpty(), "The list of categories should not be empty");
    }
}
