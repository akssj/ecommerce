package main.controllers;

import io.restassured.http.ContentType;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.JSONException;
import org.json.JSONObject;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class InitialTestDataSetup {
    private static final String CONFIG_FILE_DIRECTORY = "src/test/java/main/controllers/TestConfig.xml";
    private static final String TEST_ACCOUNT_SETUP_DATA_NODE = "TestAccountSetupData";
    private static final String TEST_ACCOUNT_USERNAME_ELEMENT = "Test_Account_Username";
    private static final String TEST_ACCOUNT_PASSWORD_ELEMENT = "Test_Account_Password";
    private static final String LOGIN_ENDPOINT = "http://localhost:8080/api/auth/login";
    private String testAccountUsername;
    private String testAccountPassword;

    @BeforeClass
    public void readTestAccountData() {
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

    //TODO fix it
    @Test
    public void performInitialTestDataSetup(){
        try {
            URL obj = new URL(LOGIN_ENDPOINT);

            String json = "{\"username\":\"" + testAccountUsername + "\"," + "\"password\":\"" + testAccountPassword + "\"}";

            HttpURLConnection con = (HttpURLConnection) obj.openConnection();

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

            while ((inputLine = bufferedReader.readLine()) != null) {
                response.append(inputLine);
            }
            bufferedReader.close();


        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    @AfterClass
    public void writeTestAccountData() {
        try {

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
