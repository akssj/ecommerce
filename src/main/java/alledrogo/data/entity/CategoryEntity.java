package alledrogo.data.entity;

import java.util.ArrayList;
import java.util.List;

public class CategoryEntity {
    private final String name;
    private final List<String> subCategories;

    public CategoryEntity(String name) {
        this.name = name;
        this.subCategories = new ArrayList<>();
    }

    public String getName() {return name;}

    public void addSubCategory(String subCategory) {subCategories.add(subCategory);}

    @Override
    public String toString() {
        return "Category{" +
                "name='" + name + '\'' +
                ", subCategories=" + subCategories +
                '}';
    }
}