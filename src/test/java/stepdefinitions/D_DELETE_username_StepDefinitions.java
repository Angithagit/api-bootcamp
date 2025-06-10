package stepdefinitions;

import static io.restassured.RestAssured.given;
import static org.testng.Assert.assertEquals;

import java.util.Map;

import io.restassured.response.Response;
import utilities.ConfigReader;
import utilities.ExcelUtils;
import utilities.TestContext;
import io.cucumber.java.en.*;


public class D_DELETE_username_StepDefinitions {

	  private Response response;
	    private Map<String, String> testData;
	    
	    
	@When("the user sends a DELETE request for {string}")
	public void the_user_sends_a_delete_request_for(String testCaseName) {
		 testData = ExcelUtils.getDataByTestName(testCaseName);
	        String createdUserName=TestContext.getCreatedUserName();
			System.out.println("Captured userName: " + createdUserName);
	        String useCaptured = testData.get("UseCapturedUsername");

	        String endpoint;

	        if ("TRUE".equalsIgnoreCase(useCaptured)) {
	            String baseEndpoint = testData.get("Endpoint"); 
	           
//	            if (baseEndpoint == null || createdUserId == null) {
//	                throw new IllegalArgumentException("Captured User ID or base endpoint is missing for test case: " + testCaseName);
//	            }

	            endpoint = baseEndpoint + createdUserName;
	        } else {
	           
	            endpoint = testData.get("Endpoint");
	        }


	        String username = ConfigReader.get("username");
	        String password = ConfigReader.get("password");

	        response = given()
	                .auth().preemptive().basic(username, password)
	                .header("Accept", "application/json")
	                .delete(endpoint);

	        System.out.println("GET Response: " + response.getBody().asString());
	    }

	@Then("the response status should match the expected from excel for DELETE username")
	public void the_response_status_should_match_the_expected_from_excel_for_delete_username() {
		 int expectedStatus = (int) Double.parseDouble(testData.get("ExpectedStatus"));
	        assertEquals(response.getStatusCode(), expectedStatus,
	                "Expected: " + expectedStatus + ", but got: " + response.getStatusCode());
	}


}
