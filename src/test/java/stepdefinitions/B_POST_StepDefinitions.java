package stepdefinitions;

import io.cucumber.java.en.*;
import io.restassured.response.Response;
import io.restassured.RestAssured;
import utilities.ConfigReader;
import utilities.ExcelUtils;
import utilities.FileUtils;
import utilities.TestContext;

import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.testng.Assert.assertEquals;

public class B_POST_StepDefinitions {

    private Response response;
    private Map<String, String> testData;
    private String baseUrl = ConfigReader.get("baseUrl");
    public static String createdUserId;
    public static String createdUserName; 

    @Given("the user is authenticated with valid base URL for POST")
    public void setupBaseURI() {
        RestAssured.baseURI = baseUrl;
    }

    @When("the user sends a POST request for {string}")
    public void the_user_sends_a_post_request_for(String testCaseName) {
        testData = ExcelUtils.getDataByTestName(testCaseName);

        String endpoint = testData.get("Endpoint");
       // String requestBody = testData.get("RequestBody");
        
        String requestBodyFile = testData.get("RequestBody");
        String requestBody = FileUtils.readJsonFromFile(requestBodyFile);

        if (endpoint == null || requestBody == null) {
            throw new IllegalArgumentException("Endpoint or RequestBody is missing for test case: " + testCaseName);
        }
       
        String username = ConfigReader.get("username");
        String password = ConfigReader.get("password");
        response = given()
                .auth().preemptive().basic(username, password)  
                .header("Content-Type", "application/json")
                .body(requestBody)
                .post(endpoint);
        
        System.out.println("Response Code: " + response.getStatusCode());
        System.out.println("Response Body:\n" + response.getBody().asString());
    }

    @Then("the response status should match the expected from excel for POST user creation")
    public void the_response_status_should_match_the_expected_from_excel_for_POST_user_creation() {
        int expectedStatus = (int) Double.parseDouble(testData.get("ExpectedStatus"));
        assertEquals(response.getStatusCode(), expectedStatus,
                "Expected: " + expectedStatus + ", but got: " + response.getStatusCode());
        
        if (response.getStatusCode() == 201) {
            createdUserId = response.jsonPath().getString("userId");
            TestContext.setCreatedUserId(createdUserId); 
            System.out.println("Captured userId: " + createdUserId);
            
            createdUserName = response.jsonPath().getString("userFirstName");
            TestContext.setCreatedUserName(createdUserName);
            System.out.println("Captured userName: " + createdUserName);


        }
    }

}
