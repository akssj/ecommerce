package alledrogo.service.implementation;

import alledrogo.data.entity.CategoryEntity;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import alledrogo.service.ProductCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

@Component
public class ProductCategoryServiceImpl implements ProductCategoryService {
    private final List<CategoryEntity> categories = new ArrayList<>();

    @Autowired
    public ProductCategoryServiceImpl() {
        updateCategories();
    }

    @Override
    public Collection<CategoryEntity> getCategories() {
        return categories;
    }

    @Override
    public boolean isCategoryValid(String category) {
        for (CategoryEntity mainCategory : categories) {
            if (mainCategory.getSubCategories().contains(category)) {
                return true;
            }
        }
        return false;
    }


    @Override
    public void updateCategories() {
        String filePath = "src/main/resources/Data/ProductCategories.xml";
        List<CategoryEntity> newCategories = readCategoriesFromFile(filePath);
        categories.clear();
        categories.addAll(newCategories);
    }

    private List<CategoryEntity> readCategoriesFromFile(String filePath) {
        List<CategoryEntity> categories = new ArrayList<>();

        try {
            File file = new File(filePath);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(file);
            doc.getDocumentElement().normalize();

            NodeList mainCategoryList = doc.getElementsByTagName("MainCategory");

            for (int i = 0; i < mainCategoryList.getLength(); i++) {
                Node mainCategoryNode = mainCategoryList.item(i);

                if (mainCategoryNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element mainCategoryElement = (Element) mainCategoryNode;

                    String mainCategoryName = mainCategoryElement.getAttribute("name");
                    CategoryEntity mainCategory = new CategoryEntity(mainCategoryName);
                    NodeList subCategoryList = mainCategoryElement.getElementsByTagName("SubCategory");

                    for (int j = 0; j < subCategoryList.getLength(); j++) {
                        Node subCategoryNode = subCategoryList.item(j);

                        if (subCategoryNode.getNodeType() == Node.ELEMENT_NODE) {
                            Element subCategoryElement = (Element) subCategoryNode;
                            String subCategoryName = subCategoryElement.getAttribute("name");
                            mainCategory.addSubCategory(subCategoryName);
                        }
                    }
                    categories.add(mainCategory);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return categories;
    }
}
