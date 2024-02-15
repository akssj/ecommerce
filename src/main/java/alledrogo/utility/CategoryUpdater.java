package alledrogo.utility;

import alledrogo.service.ProductCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class CategoryUpdater {
    private final ProductCategoryService productCategoryService;

    @Autowired
    public CategoryUpdater(ProductCategoryService productCategoryService) {
        this.productCategoryService = productCategoryService;
    }

    @Scheduled(fixedRate = 60000)
    public void updateCategories() {
        productCategoryService.updateCategories();
    }
}
