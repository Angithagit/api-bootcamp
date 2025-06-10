package stepdefinitions;


import io.cucumber.java.en.*;
import io.restassured.response.Response;
import io.restassured.RestAssured;
import utilities.ConfigReader;
import static io.restassured.RestAssured.given;

import static org.testng.Assert.assertEquals;
import utilities.ExcelUtils;
import java.util.Map;

public class A_GET_allUsers_StepDefinitions {

	 private Response response;
	 private Map<String, String> testData;
	 private String baseUrl = ConfigReader.get("baseUrl");
	

    @Given("the system has one or more valid users")
    public void setupBaseURI() {
    	RestAssured.baseURI = baseUrl;
}

    @When("the user sends a GET request for  {string}")
    public void the_user_sends_a_get_request_for(String testCaseName) {

    	 testData = ExcelUtils.getDataByTestName(testCaseName);
    	    String endpoint = testData.get("Endpoint");
    	    String method = testData.get("Method");

     if (testCaseName.equalsIgnoreCase("UnauthorizedAccess")) {
         response = given()
                 .auth().preemptive().basic("invalid", "invalid")
                 .header("Accept", "application/json")
                 .get(endpoint);
         return;
     }

     switch (method.toUpperCase()) {
         case "GET":
             response = given()
                     .auth().preemptive().basic(ConfigReader.get("username"), ConfigReader.get("password"))
                     .header("Accept", "application/json")
                     .get(endpoint);
             break;

         case "POST":
             response = given()
                     .auth().preemptive().basic(ConfigReader.get("username"), ConfigReader.get("password"))
                     .header("Accept", "application/json")
                     .post(endpoint);
             break;

         default:
             throw new IllegalArgumentException("Unsupported method: " + method);
     }

}

@Then("the response status should match the expected from excel")
public void the_response_status_should_match_the_expected_from_excel() {
	 int expectedStatus = (int) Double.parseDouble(testData.get("ExpectedStatus"));
     assertEquals(response.getStatusCode(), expectedStatus,
             "Expected: " + expectedStatus + ", but got: " + response.getStatusCode());
 }


}

