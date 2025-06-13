package stepDefinition;

import io.cucumber.java.en.*;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import pojo.Testdata;
import pojo.UserAddress;
import utilities.ConfigReader;
import utilities.JsonDataReader;
import utilities.LoggerReader;
import utilities.ChainValues;


import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import java.util.Map;

public class PostOperation {

    private Response response;
    private RequestSpecification request;
    private Testdata testdata;
    private final String jsonFilePath = "src/test/resources/testData/testdata.json";
    public static int userId;
	static String firstName;
    private final String baseUrl = ConfigReader.getConfig("baseURL");
    private final String authUsername = ConfigReader.getConfig("username");
    private final String authPassword = ConfigReader.getConfig("password");
	private ChainValues passvalues;
    
    public PostOperation() {
        this.passvalues = new ChainValues(); 
    }


    private void loadTestCase(String testCaseId) {
        Map<String, Object> dataMap = JsonDataReader.getTestCaseById(jsonFilePath, testCaseId);
        testdata = new Testdata();
        testdata.testCaseId = (String) dataMap.get("testCaseId");
        testdata.scenarioType = (String) dataMap.get("scenarioType");
        testdata.endpoint = (String) dataMap.get("endpoint");
        testdata.method = (String) dataMap.get("method");
        testdata.userFirstName = (String) dataMap.get("userFirstName");
        testdata.userLastName = (String) dataMap.get("userLastName");

        Object contactNumObj = dataMap.get("userContactNumber");
        if (contactNumObj instanceof Integer) {
            testdata.userContactNumber = ((Integer) contactNumObj).longValue();
        } else if (contactNumObj instanceof Long) {
            testdata.userContactNumber = (Long) contactNumObj;
        }

        testdata.userEmailId = (String) dataMap.get("userEmailId");
        testdata.expectedStatusCode = (Integer) dataMap.get("expectedStatusCode");
        testdata.expectedStatusLine = (String) dataMap.get("expectedStatusLine");
        testdata.expectedStatusmessage = (String) dataMap.getOrDefault("expectedStatusmessage", "");

        Map<String, Object> addressMap = (Map<String, Object>) dataMap.get("userAddress");
        if (addressMap != null) {
            UserAddress address = new UserAddress();
            address.plotNumber = (String) addressMap.get("plotNumber");
            address.street = (String) addressMap.get("street");
            address.state = (String) addressMap.get("state");
            address.country = (String) addressMap.get("country");

            Object zipCodeObj = addressMap.get("zipCode");
            if (zipCodeObj instanceof Integer) {
                address.zipCode = (Integer) zipCodeObj;
            } else if (zipCodeObj instanceof Long) {
                address.zipCode = ((Long) zipCodeObj).intValue();
            }

            testdata.userAddress = address;
        }
    }

    @Given("Admin creates request body with valid inputs")
    public void admin_creates_request_body_with_valid_inputs() {
        loadTestCase("TC06");
    }

    @When("Admin sends HTTP POST request")
    public void admin_sends_http_post_request() {
        response = given()
                .auth().preemptive().basic(authUsername, authPassword)
                .header("Content-Type", "application/json")
                .body(testdata)
                .when()
                .post(baseUrl + testdata.endpoint);
    }

    @Then("Admin receives Status code 200 OK with response body")
    public void admin_receives_status_code_200_ok_with_response_body() {
    	
    	response.then()
        .statusCode(testdata.expectedStatusCode)
        .statusLine(testdata.expectedStatusLine)
        .contentType("application/json")
        .body("userFirstName", equalTo(testdata.userFirstName))
        .body("userLastName", equalTo(testdata.userLastName))
        .body("userContactNumber", equalTo((int)testdata.userContactNumber))  // if needed, cast to int
        .body("userEmailId", equalTo(testdata.userEmailId))
        .body("userAddress.plotNumber", equalTo(testdata.userAddress.plotNumber))
        .body("userAddress.street", equalTo(testdata.userAddress.street))
        .body("userAddress.state", equalTo(testdata.userAddress.state))
        .body("userAddress.country", equalTo(testdata.userAddress.country))
        .body("userAddress.zipCode", equalTo(testdata.userAddress.zipCode))
    	        .body("userFirstName", instanceOf(String.class))
    	        .body("userLastName", instanceOf(String.class))
    	        .body("userContactNumber", instanceOf(Number.class))
    	        .body("userEmailId", instanceOf(String.class))
    	        .body("userAddress.plotNumber", instanceOf(String.class))
    	        .body("userAddress.street", instanceOf(String.class))
    	        .body("userAddress.state", instanceOf(String.class))
    	        .body("userAddress.country", instanceOf(String.class))
    	        .body("userAddress.zipCode", instanceOf(Number.class));

    	    userId = response.path("userId");
    	    firstName = response.path("userFirstName").toString();
    	    passvalues.getuserData().setUserId(userId);
    	    passvalues.getuserData().setUserFirstName(firstName);
    	    LoggerReader.info("User ID: " + userId);
    	    LoggerReader.info("First Name: " + firstName);
    	    assertThat(response.getStatusCode(), is(testdata.expectedStatusCode));
    	    assertThat(response.getStatusLine(), containsString(testdata.expectedStatusLine));
    	    LoggerReader.info("Response Body: " + response.getBody().asPrettyString());
    	}

    @Given("Admin creates request body with existing emailid")
    public void admin_creates_request_body_with_existing_emailid() {
        loadTestCase("TC07");
    }

    @Then("Admin receives Status code 409 CONFLICT with message in response body")
    public void admin_receives_status_code_409_conflict_with_message_in_response_body() {
        assertThat(response.getStatusCode(), is(testdata.expectedStatusCode));
        assertThat(response.getStatusLine(), containsString(testdata.expectedStatusLine));
        assertThat(response.getBody().asString(), containsString(testdata.expectedStatusmessage));
        System.out.println("Response Body: " + response.getBody().asPrettyString());
    }

  
    @Then("Admin receives 404  Not found Status Code in response body")
    public void admin_receives_404_not_found_status_code_in_response_body() {
        assertThat(response.getStatusCode(), is(testdata.expectedStatusCode));
        assertThat(response.getStatusLine(), containsString(testdata.expectedStatusLine));
        assertThat(response.getBody().asString(), containsString(testdata.expectedStatusmessage));
        System.out.println("Response Body: " + response.getBody().asPrettyString());
    }
    
    @Given("Admin creates request with invalid end point")
    public void admin_creates_request_with_invalid_end_point() {
        loadTestCase("TC08");
        request = given()
                .baseUri(baseUrl)
                .auth().preemptive().basic(authUsername, authPassword)
                .header("Content-Type", "application/json");

        // Override basePath to an invalid endpoint
        RestAssured.basePath = "/create"; // intentionally incorrect
    }
    
    @Given("User creates request body with emailid has special characters")
    public void user_creates_request_body_with_emailid_has_special_characters() {
    	loadTestCase("TC09");
    }

    @Then("User receives Status code {int} BAD REQUEST with message in response body")
    public void user_receives_status_code_bad_request_with_message_in_response_body(Integer int1) {
    	  assertThat(response.getStatusCode(), equalTo(int1));
    	    assertThat(response.statusLine().toUpperCase(), containsString("BAD REQUEST"));
    	    assertThat(response.getBody().asString(), containsString(testdata.expectedStatusmessage));
    	    System.out.println("Response Body: " + response.getBody().asPrettyString());
    }

    @Given("User creates request body  with FirstName has numeric values")
    public void user_creates_request_body_with_first_name_has_numeric_values() {
    	loadTestCase("TC10");
    }

}
