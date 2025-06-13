package stepDefinition;

import io.cucumber.java.en.*;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import pojo.Testdata;
import utilities.ConfigReader;
import utilities.JsonDataReader;
import utilities.LoggerReader;

import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class GetbyUserName {

    private Response response;
    private RequestSpecification request;
    private Testdata testdata;
    private String baseUrl;
    private String authUsername;
    private String authPassword;
    int userId = PostOperation.userId;
	String firstName = PostOperation.firstName;
    private final String jsonFilePath = "src/test/resources/testData/testdata.json";

    private void loadTestCase(String testCaseId) {
        Map<String, Object> dataMap = JsonDataReader.getTestCaseById(jsonFilePath, testCaseId);
        testdata = new Testdata();
        testdata.testCaseId = (String) dataMap.get("testCaseId");
        testdata.scenarioType = (String) dataMap.get("scenarioType");
        testdata.endpoint = (String) dataMap.get("endpoint");
        testdata.method = (String) dataMap.get("method");
        testdata.userFirstName = (String) dataMap.get("userFirstName");
        testdata.expectedStatusCode = (Integer) dataMap.get("expectedStatusCode");
        testdata.expectedStatusLine = (String) dataMap.get("expectedStatusLine");
        testdata.expectedStatusmessage = (String) dataMap.getOrDefault("expectedStatusmessage", "");
    }

    @Given("user has access to endpoint")
    public void user_has_access_to_endpoint() {
        baseUrl = ConfigReader.getConfig("baseURL");
        authUsername = ConfigReader.getConfig("username");
        authPassword = ConfigReader.getConfig("password");
        request = RestAssured.given().auth().preemptive().basic(authUsername, authPassword);
        
    }

    @Given("user has access without endpoint")
    public void user_has_access_without_endpoint() {
        baseUrl = ConfigReader.getConfig("baseURL");
        authUsername = ConfigReader.getConfig("username");
        authPassword = ConfigReader.getConfig("password");
        request = RestAssured.given().auth().preemptive().basic(authUsername, authPassword);
    }

    @When("user sends a GET request with valid endpoint")
    public void user_sends_get_with_valid_endpoint() {
        loadTestCase("TC11");
        String fname = String.valueOf(PostOperation.firstName);
        String finalEndpoint = testdata.endpoint.contains("{userFirstName}")
                ? testdata.endpoint.replace("{userFirstName}", fname)
                : testdata.endpoint;
        response = request.when().get(baseUrl + "/" + finalEndpoint);
    }

    @When("user sends a GET request with invalid endpoint")
    public void user_sends_get_with_invalid_endpoint() {
        loadTestCase("TC12");
        response = request.when().get(baseUrl + "/" + testdata.endpoint);
    }

    @When("user sends a GET request with invalid userFirstName")
    public void user_sends_get_with_invalid_userFirstName() {
        loadTestCase("TC13");
        String fullEndpoint = testdata.endpoint.replace("{userFirstName}", testdata.userFirstName != null ? testdata.userFirstName : "");
        response = request.when().get(baseUrl + "/" + fullEndpoint);
    }

    @When("user sends a GET request with userFirstName in numeric values")
    public void user_sends_get_with_numeric_userFirstName() {
        loadTestCase("TC14");
        String fullEndpoint = testdata.endpoint.replace("{userFirstName}", testdata.userFirstName);
        response = request.when().get(baseUrl + "/" + fullEndpoint);
    }

    @When("user sends a GET request with userFirstName in alphanumeric format")
    public void user_sends_get_with_alphanumeric_userFirstName() {
        loadTestCase("TC15");
        String fullEndpoint = testdata.endpoint.replace("{userFirstName}", testdata.userFirstName);
        response = request.when().get(baseUrl + "/" + fullEndpoint);
    }

    @When("user sends a GET request with userFirstName in special characters")
    public void user_sends_get_with_special_characters_userFirstName() {
        loadTestCase("TC16");
        String fullEndpoint = testdata.endpoint.replace("{userFirstName}", testdata.userFirstName);
        response = request.when().get(baseUrl + "/" + fullEndpoint);
    }

    @When("user sends a POST request with valid endpoint")
    public void user_sends_post_with_valid_endpoint() {
        loadTestCase("TC17");
        String fullEndpoint = testdata.endpoint.replace("{userFirstName}", testdata.userFirstName);
        response = request.when().post(baseUrl + "/" + fullEndpoint);
    }

    @When("user sends a GET request without endpoint")
    public void user_sends_get_without_endpoint() {
        loadTestCase("TC18");
        response = request.when().get(baseUrl);
    }

    @When("user sends a GET request with valid endpoint without auth")
    public void user_sends_get_with_valid_endpoint_without_auth() {
        loadTestCase("TC19");  // Make sure this test case expects 401
        String fullEndpoint = testdata.endpoint.replace("{userFirstName}", testdata.userFirstName);

        // Request without authentication
        request = RestAssured.given();  // No auth setup
        response = request.when().get(baseUrl + "/" + fullEndpoint);
    }

    @Then("Admin receives {int} OK Status Code and should display only user with that particular first name in response body")
    public void admin_receives_200_ok_status_code(int expectedCode) {
    	 response.then()
         .statusCode(expectedCode)
         .statusLine(containsStringIgnoringCase("ok"))
         .contentType("application/json")
         .body("userFirstName", hasItem(PostOperation.firstName));
          assertThat(response.getStatusCode(), is(expectedCode));
        assertThat(response.getStatusLine().toUpperCase(), containsString("OK"));
        // Optionally verify the response message contains something expected
        LoggerReader.info("Response Body: " + response.getBody().asPrettyString());
    	
    }

 
    @Then("Admin receives {int} Not Found Status Code in response body")
    public void admin_receives_404_not_found_with_message(int expectedCode) {
    	response.then().statusCode(404).statusLine("HTTP/1.1 404 Not Found").contentType("application/json");
        assertThat(response.getStatusCode(), equalTo(expectedCode));
        assertThat(response.statusLine(), containsString(testdata.expectedStatusLine));
        assertThat(response.getHeader("Content-Type"), containsString("application/json"));
        if (!testdata.expectedStatusmessage.isEmpty()) {
            assertThat(response.getBody().asString(), containsString(testdata.expectedStatusmessage));
        }
    }

    @Then("Admin receives {int} Method Not Allowed Status Code in response body")
    public void admin_receives_405_method_not_allowed(int expectedCode) {
        assertThat(response.getStatusCode(), equalTo(expectedCode));
        assertThat(response.statusLine(), containsString(testdata.expectedStatusLine));
        assertThat(response.getHeader("Content-Type"), containsString("application/json"));
    }
    
      @Then("Admin receives {int} Unauthorized Status Code in response body")
    public void admin_receives_401_unauthorized_status_code(int expectedCode) {
        LoggerReader.info("Actual Status: " + response.getStatusCode());
        LoggerReader.info("Response Body: " + response.getBody().asString());
        assertThat(response.getStatusCode(), equalTo(expectedCode));
        assertThat(response.statusLine(), containsString(testdata.expectedStatusLine));
        assertThat(response.getHeader("Content-Type"), containsString("application/json"));
    }

}