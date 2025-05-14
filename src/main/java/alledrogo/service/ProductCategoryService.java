package alledrogo.service;

import alledrogo.data.entity.CategoryEntity;
import org.springframework.stereotype.Component;

import java.util.Collection;
@Component
public interface ProductCategoryService {
    Collection<CategoryEntity> getCategories();

    boolean isCategoryValid(String category);

    void updateCategories();
}
