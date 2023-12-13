package alledrogo.tests.dataset;

import io.restassured.http.ContentType;
import org.json.JSONException;
import org.json.JSONObject;
import org.testng.annotations.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

import static io.restassured.RestAssured.given;

/**
 * Initial Test class read TestAccount data from "TestAccountConfig.xml" and save login response data into "TestAccountDataset.xml".
 */
public class InitialTestDataSetup {
    private static final String CONFIG_FILE_DIRECTORY = "src/test/java/alledrogo/dataset/TestAccountConfig.xml";
    private static final String DATASET_FILE_DIRECTORY = "src/test/java/alledrogo/dataset/TestAccountDataset.xml";
    private static final String TEST_ACCOUNT_SETUP_DATA_NODE = "TestAccountSetupData";
    private static final String TEST_ACCOUNT_USERNAME_ELEMENT = "Test_Account_Username";
    private static final String TEST_ACCOUNT_PASSWORD_ELEMENT = "Test_Account_Password";
    private static final String TEST_ACCOUNT_CREATED_DATA_NODE = "TestAccountCreatedData";
    private static final String LOGIN_ENDPOINT = "http://localhost:8080/auth/login";
    private static final String REGISTER_ENDPOINT = "http://localhost:8080/auth/signup";
    private String testAccountUsername;
    private String testAccountPassword;
    private StringBuilder testAccountLoginResponse;

    @Test(priority = 1)
    protected void readTestAccountData() {
        try {
            File file = new File(CONFIG_FILE_DIRECTORY);

            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(file);
            document.getDocumentElement().normalize();

            NodeList nodeList = document.getElementsByTagName(TEST_ACCOUNT_SETUP_DATA_NODE);

            for (int i = 0; i < nodeList.getLength(); i++) {
                Element element = (Element) nodeList.item(i);

                testAccountUsername = element.getElementsByTagName(TEST_ACCOUNT_USERNAME_ELEMENT).item(0).getTextContent();
                testAccountPassword = element.getElementsByTagName(TEST_ACCOUNT_PASSWORD_ELEMENT).item(0).getTextContent();

                if (!testAccountUsername.isEmpty() && !testAccountPassword.isEmpty()) {
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test(priority = 2)
    protected void createTestAccount() throws JSONException {
        JSONObject testJsonRequest = new JSONObject();
        testJsonRequest.put("username", testAccountUsername);
        testJsonRequest.put("password", testAccountPassword);

        given()
            .contentType(ContentType.JSON)
            .body(testJsonRequest.toString())
        .when()
            .post(REGISTER_ENDPOINT);
    }

    @Test(priority = 3)
    protected void performInitialTestDataSetup(){
        try {
            URL url = new URL(LOGIN_ENDPOINT);

            String json = "{\"username\":\"" + testAccountUsername + "\"," + "\"password\":\"" + testAccountPassword + "\"}";

            HttpURLConnection con = (HttpURLConnection) url.openConnection();

            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json");
            con.setDoOutput(true);

            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            wr.writeBytes(json);
            wr.flush();
            wr.close();

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();

            while (( inputLine = bufferedReader.readLine()) != null) {
                response.append(inputLine);
            }
            bufferedReader.close();

            testAccountLoginResponse = response;

        }catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Test(priority = 4)
    protected void saveTestAccountData() {
        try {
            File file = new File(DATASET_FILE_DIRECTORY);
            Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(file);
            document.getDocumentElement().normalize();
            Path filePath = file.toPath();

            //TODO fix that .isempty
            Element element = (Element) document.getElementsByTagName(TEST_ACCOUNT_CREATED_DATA_NODE).item(0);
            if (element == null) {
                element = document.createElement(TEST_ACCOUNT_CREATED_DATA_NODE);
                document.getDocumentElement().appendChild(element);
            } else {
                while (element.hasChildNodes()) {
                    element.removeChild(element.getFirstChild());
                }
            }

            String jsonData = testAccountLoginResponse.toString();
            String[] keyValuePairs = jsonData.replaceAll("[{}\"]", "").split(",");
            for (String keyValuePair : keyValuePairs) {
                String[] keyValue = keyValuePair.split(":");
                String key = keyValue[0].trim();
                String value = keyValue[1].trim();
                key = escapeXml(key);
                value = escapeXml(value);
                Element keyElement = element.getOwnerDocument().createElement(key);
                keyElement.appendChild(element.getOwnerDocument().createTextNode(value));
                element.appendChild(keyElement);
            }

            javax.xml.transform.TransformerFactory tf = javax.xml.transform.TransformerFactory.newInstance();
            javax.xml.transform.Transformer transformer = tf.newTransformer();
            java.io.StringWriter writer = new java.io.StringWriter();
            transformer.transform(new javax.xml.transform.dom.DOMSource(document), new javax.xml.transform.stream.StreamResult(writer));
            String modifiedXmlString = writer.toString();

            Files.write(filePath, modifiedXmlString.getBytes(), StandardOpenOption.TRUNCATE_EXISTING);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private static String escapeXml(String input) {
        return input.replaceAll("&", "&amp;")
                .replaceAll("<", "&lt;")
                .replaceAll(">", "&gt;")
                .replaceAll("\"", "&quot;")
                .replaceAll("'", "&apos;");
    }
}
