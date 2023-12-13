package alledrogo.tests.dataset;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;

/**
 * TestDataDealer class.
 * Read TestAccount data from "TestAccountDataset.xml" and delivers it to other test classes on demand.
 */
public class TestDataDealer {
    private static final String DATASET_FILE_DIRECTORY = "src/test/java/alledrogo/dataset/TestAccountDataset.xml";
    private static final String TEST_ACCOUNT_CREATED_DATA_NODE = "TestAccountCreatedData";
    private static final String TEST_ACCOUNT_TOKEN_ELEMENT = "token";
    private static final String TEST_ACCOUNT_TYPE_ELEMENT = "type";
    private static final String TEST_ACCOUNT_ID_ELEMENT = "id";
    private static final String TEST_ACCOUNT_USERNAME_ELEMENT = "username";
    private static final String TEST_ACCOUNT_ROLES_ELEMENT = "roles"; //TODO remake to match collection
    private static final String TEST_ACCOUNT_BALANCE_ELEMENT = "balance";

    private String token;
    private String type;
    private Long id;
    private String username;
    private String roles;
    private Integer balance;

    public String getToken() {return token;}
    public String getType() {return type;}
    public Long getId() {return id;}
    public String getUsername() {return username;}
    public String getRoles() {return roles;}
    public Integer getBalance() {return balance;}

    //TODO i might want to change vars to arrays so more than one test account can be used
    public TestDataDealer() {
        try {
            File file = new File(DATASET_FILE_DIRECTORY);

            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(file);
            document.getDocumentElement().normalize();

            NodeList nodeList = document.getElementsByTagName(TEST_ACCOUNT_CREATED_DATA_NODE);

            for (int i = 0; i < nodeList.getLength(); i++) {
                Element element = (Element) nodeList.item(i);
                this.token = element.getElementsByTagName(TEST_ACCOUNT_TOKEN_ELEMENT).item(0).getTextContent();
                this.type = element.getElementsByTagName(TEST_ACCOUNT_TYPE_ELEMENT).item(0).getTextContent();
                this.id = Long.valueOf(element.getElementsByTagName(TEST_ACCOUNT_ID_ELEMENT).item(0).getTextContent());
                this.username = element.getElementsByTagName(TEST_ACCOUNT_USERNAME_ELEMENT).item(0).getTextContent();
                this.roles = element.getElementsByTagName(TEST_ACCOUNT_ROLES_ELEMENT).item(0).getTextContent();
                this.balance = Integer.valueOf(element.getElementsByTagName(TEST_ACCOUNT_BALANCE_ELEMENT).item(0).getTextContent());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
