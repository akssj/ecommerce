package alledrogo.utility;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;

public class ProductCategoryValidator {

    public static boolean isCategoryValid(String category) {
        try {
            String xmlFilePath = "ProductCategories.xml";
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
}
