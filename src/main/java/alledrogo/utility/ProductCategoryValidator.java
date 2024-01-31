package alledrogo.utility;

import alledrogo.data.entity.CategoryEntity;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ProductCategoryValidator {
    public static Collection<CategoryEntity> categories;

    public ProductCategoryValidator() {
        categories = readCategories();
    }

    public static Collection<CategoryEntity> getCategories() {
        return categories;
    }

    public static boolean isCategoryValid(String category) {
        try {
            String xmlFilePath = "src/main/resources/Data/ProductCategories.xml";
            File xmlFile = new File(xmlFilePath);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(xmlFile);

            doc.getDocumentElement().normalize();

            NodeList mainCategories = doc.getElementsByTagName("MainCategory");

            for (int i = 0; i < mainCategories.getLength(); i++) {
                Element mainCategory = (Element) mainCategories.item(i);
                String mainCategoryName = mainCategory.getAttribute("name");

                NodeList subCategories = mainCategory.getElementsByTagName("SubCategory");

                for (int j = 0; j < subCategories.getLength(); j++) {
                    Element subCategory = (Element) subCategories.item(j);
                    String subCategoryName = subCategory.getAttribute("name");

                    String fullCategoryName = mainCategoryName + "/" + subCategoryName;

                    if (fullCategoryName.equals(category)) {
                        return true;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public static Collection<CategoryEntity> readCategories() {
        String filePath = "src/main/resources/Data/ProductCategories.xml";

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
