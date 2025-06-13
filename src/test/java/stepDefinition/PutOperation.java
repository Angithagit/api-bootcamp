package stepDefinition;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.en.*;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import pojo.Testdata;
import pojo.UserAddress;
import utilities.ChainValues;
import utilities.ConfigReader;
import utilities.JsonDataReader;
import utilities.LoggerReader;
import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import java.util.Map;

public class PutOperation {

    private Response response;
    private RequestSpecification request;
    private Testdata testdata;
    private String baseUrl;
    private String authUsername;
    private String authPassword;
    private final String jsonFilePath = "src/test/resources/testData/testaata.json";
    public static int userId;
	static String firstName;
	private ChainValues passvalues = new ChainValues();

    
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

        // Handle nested userAddress map
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

    

    private void prepareRequestWithoutAuth() {
        baseUrl = ConfigReader.getConfig("baseURL");
        request = given()
                .baseUri(baseUrl)
                .header("Content-Type", "application/json");
    }

    private void prepareRequestWithBasicAuth(String username, String password) {
        baseUrl = ConfigReader.getConfig("baseURL");
        request = given()
                .baseUri(baseUrl)
                .auth().preemptive().basic(username, password)
                .header("Content-Type", "application/json");
    }

    @Given("User updates PUT request with no auth")
    public void user_updates_put_request_with_no_auth() {
        loadTestCase("TC20");
        prepareRequestWithoutAuth();
    }

    @Given("User updates PUT request with invalid base auth")
    public void user_updates_put_request_with_invalid_base_auth() {
        loadTestCase("TC21");
        prepareRequestWithBasicAuth("invalidUser", "invalidPass");
    }

    @Given("User updates PUT request with valid end point")
    public void user_updates_put_request_with_valid_end_point() {
        loadTestCase("TC22");
        authUsername = ConfigReader.getConfig("username");
        authPassword = ConfigReader.getConfig("password");
        prepareRequestWithBasicAuth(authUsername, authPassword);
    }

    @When("User sends HTTP PUT request")
    public void user_sends_http_put_request() throws Exception {
        // Serialize testdata object to JSON string
        ObjectMapper mapper = new ObjectMapper();
        String jsonBody = mapper.writeValueAsString(testdata);

        // Replace userId in endpoint
        String endpointWithUserId = testdata.endpoint.replace("{userId}", "23866");
        response = request
                .body(jsonBody)
                .when()
                .put(baseUrl + "/" + endpointWithUserId);
    }

    @Then("User receives {int} Unauthorized Status Code in response body")
    public void user_receives_unauthorized_status_code_in_response_body(Integer expectedStatusCode) {
        assertThat(response.getStatusCode(), equalTo(expectedStatusCode));
        assertThat(response.statusLine(), containsString(testdata.expectedStatusLine));
    }

    @Then("User receives {int} OK Status Code in response body")
    public void user_receives_ok_status_code_in_response_body(Integer expectedStatusCode) {
    	response.then()
        .statusCode(testdata.expectedStatusCode)
        .statusLine(testdata.expectedStatusLine)
        .contentType("application/json")
        .body("userFirstName", equalTo(testdata.userFirstName))
        .body("userLastName", equalTo(testdata.userLastName))
        .body("userContactNumber", equalTo(testdata.userContactNumber))  // if needed, cast to int
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

    	    LoggerReader.info("Validated authorized response: " + response.getStatusLine());

    	    userId = response.path("userId");
    	    passvalues.getuserData().setUserId(userId);
    	    LoggerReader.info("User ID: " + userId);
    	    LoggerReader.info("First Name: " + firstName);
    	    assertThat(response.getStatusCode(), is(testdata.expectedStatusCode));
    	    assertThat(response.getStatusLine(), containsString(testdata.expectedStatusLine));
    	    LoggerReader.info("Response Body: " + response.getBody().asPrettyString());
        assertThat(response.getStatusCode(), equalTo(expectedStatusCode));
        assertThat(response.statusLine(), containsString(testdata.expectedStatusLine));
        assertThat(response.getBody().asString(), containsString(testdata.userFirstName));
        assertThat(response.getHeader("Content-Type"), containsString("application/json"));
        LoggerReader.info("Response Body: " + response.getBody().asPrettyString());
        LoggerReader.info("Expected userContactNumber from JSON: " + testdata.userContactNumber);
    }
    
    @Given("User updates PUT request with invalid end point")
    public void user_updates_put_request_with_invalid_end_point() {
    	loadTestCase("TC23");
        authUsername = ConfigReader.getConfig("username");
        authPassword = ConfigReader.getConfig("password");
        prepareRequestWithBasicAuth(authUsername, authPassword);
    }

    @Then("User receives {int}  Not found Status Code in response body")
    public void user_receives_not_found_status_code_in_response_body(Integer int1) {
    	 assertThat(response.getStatusCode(), equalTo(int1));
         assertThat(response.statusLine(), containsString(testdata.expectedStatusLine));
         assertThat(response.getHeader("Content-Type"), containsString("application/json")); 
    }

    @Given("User updates request body  with contact number less than {int} digits")
    public void user_updates_request_body_with_contact_number_less_than_digits(Integer int1) {
    	
        	loadTestCase("TC24");
            authUsername = ConfigReader.getConfig("username");
            authPassword = ConfigReader.getConfig("password");
            prepareRequestWithBasicAuth(authUsername, authPassword);
    }

    @Then("Admin receives Status code {int} BAD REQUEST with  message {string} in response body")
    public void admin_receives_status_code_bad_request_with_message_in_response_body(Integer int1, String string) {
    	assertThat(response.getStatusCode(), equalTo(int1));
        assertThat(response.statusLine().toUpperCase(), containsString(testdata.expectedStatusLine.toUpperCase()));
        assertThat(response.getHeader("Content-Type"), containsString("application/json"));
        assertThat(response.getBody().asString(), containsString(string));
        System.out.println("Response Body: " + response.getBody().asString());

    }
    @Given("User updates request body  with contact number more than {int} digits")
    public void user_updates_request_body_with_contact_number_more_than_digits(Integer int1) {
    	
        	loadTestCase("TC25");
            authUsername = ConfigReader.getConfig("username");
            authPassword = ConfigReader.getConfig("password");
            prepareRequestWithBasicAuth(authUsername, authPassword);
    }
    
    @Given("User updates request body with emailid has special characters")
    public void user_updates_request_body_with_emailid_has_special_characters_$_at_the_end() {
    	loadTestCase("TC26");
        authUsername = ConfigReader.getConfig("username");
        authPassword = ConfigReader.getConfig("password");
        prepareRequestWithBasicAuth(authUsername, authPassword);
    }


    @Given("User updates request body  with FirstName has numeric values")
    public void user_updates_request_body_with_first_name_has_numeric_values() {
    	loadTestCase("TC27");
        authUsername = ConfigReader.getConfig("username");
        authPassword = ConfigReader.getConfig("password");
        prepareRequestWithBasicAuth(authUsername, authPassword);
    }

    @Given("User updates request body  with FirstName has alphanumeric values")
    public void user_updates_request_body_with_first_name_has_alphanumeric_values() {
    	loadTestCase("TC28");
        authUsername = ConfigReader.getConfig("username");
        authPassword = ConfigReader.getConfig("password");
        prepareRequestWithBasicAuth(authUsername, authPassword);
    }

    @Given("User updates request body  with FirstName contains special characters")
    public void user_updates_request_body_with_first_name_contains_special_characters() {
    	loadTestCase("TC29");
        authUsername = ConfigReader.getConfig("username");
        authPassword = ConfigReader.getConfig("password");
        prepareRequestWithBasicAuth(authUsername, authPassword);
    }

    @Given("User updates request body  with LastName has alphanumeric values")
    public void user_updates_request_body_with_last_name_has_alphanumeric_values() {
    	loadTestCase("TC30");
        authUsername = ConfigReader.getConfig("username");
        authPassword = ConfigReader.getConfig("password");
        prepareRequestWithBasicAuth(authUsername, authPassword);
    }

    @Given("User updates request body  with LastName has special characters")
    public void user_updates_request_body_with_last_name_has_special_characters() {
    	loadTestCase("TC31");
        authUsername = ConfigReader.getConfig("username");
        authPassword = ConfigReader.getConfig("password");
        prepareRequestWithBasicAuth(authUsername, authPassword);
    }

    @Given("User updates request body  with LastName has numeric values")
    public void user_updates_request_body_with_last_name_has_numeric_values() {
    	loadTestCase("TC32");
        authUsername = ConfigReader.getConfig("username");
        authPassword = ConfigReader.getConfig("password");
        prepareRequestWithBasicAuth(authUsername, authPassword);
    }
    
    @Then("User receives Status code {int} BAD REQUEST with message {string} in response body")
    public void user_receives_status_code_bad_request_with_message_in_response_body(Integer int1, String string) {
    	  assertThat(response.getStatusCode(), is(testdata.expectedStatusCode));
          assertThat(response.getStatusLine(), containsString(testdata.expectedStatusLine));
          assertThat(response.getBody().asString(), containsString(testdata.expectedStatusmessage));
        System.out.println("Response Body: " + response.getBody().asString());
    }

    @Given("User updates request body with plotNumber contains string")
    public void user_updates_request_body_with_plot_number_contains_string() {
    	loadTestCase("TC33");
        authUsername = ConfigReader.getConfig("username");
        authPassword = ConfigReader.getConfig("password");
        prepareRequestWithBasicAuth(authUsername, authPassword);
    }

    @Given("User updates request body with plotNumber has special characters")
    public void user_updates_request_body_with_plot_number_has_special_characters() {
    	loadTestCase("TC34");
        authUsername = ConfigReader.getConfig("username");
        authPassword = ConfigReader.getConfig("password");
        prepareRequestWithBasicAuth(authUsername, authPassword);
    }

    @Given("User updates request body with plotNumber has numeric values")
    public void user_updates_request_body_with_plot_number_has_numeric_values() {
    	loadTestCase("TC35");
        authUsername = ConfigReader.getConfig("username");
        authPassword = ConfigReader.getConfig("password");
        prepareRequestWithBasicAuth(authUsername, authPassword);
    }

    @Given("User updates request body with street has special characters")
    public void user_updates_request_body_with_street_has_special_characters() {
    	loadTestCase("TC36");
        authUsername = ConfigReader.getConfig("username");
        authPassword = ConfigReader.getConfig("password");
        prepareRequestWithBasicAuth(authUsername, authPassword);
    }

    @Given("User updates request body with street has numeric values")
    public void user_updates_request_body_with_street_has_numeric_values() {
    	loadTestCase("TC37");
        authUsername = ConfigReader.getConfig("username");
        authPassword = ConfigReader.getConfig("password");
        prepareRequestWithBasicAuth(authUsername, authPassword);
    }

    @Given("User updates request body with street has alphanumeric values")
    public void user_updates_request_body_with_street_has_alphanumeric_values() {
    	loadTestCase("TC38");
        authUsername = ConfigReader.getConfig("username");
        authPassword = ConfigReader.getConfig("password");
        prepareRequestWithBasicAuth(authUsername, authPassword);
    }

    @Given("User updates request body with state has special characters")
    public void user_updates_request_body_with_state_has_special_characters() {
    	loadTestCase("TC39");
        authUsername = ConfigReader.getConfig("username");
        authPassword = ConfigReader.getConfig("password");
        prepareRequestWithBasicAuth(authUsername, authPassword);
    }

    @Given("User updates request body with state has numeric values")
    public void user_updates_request_body_with_state_has_numeric_values() {
    	loadTestCase("TC40");
        authUsername = ConfigReader.getConfig("username");
        authPassword = ConfigReader.getConfig("password");
        prepareRequestWithBasicAuth(authUsername, authPassword);
    }

    @Given("User updates request body with state has alphanumeric values")
    public void user_updates_request_body_with_state_has_alphanumeric_values() {
    	loadTestCase("TC41");
        authUsername = ConfigReader.getConfig("username");
        authPassword = ConfigReader.getConfig("password");
        prepareRequestWithBasicAuth(authUsername, authPassword);
    }

    @Given("User updates request body with country has numeric values")
    public void user_updates_request_body_with_country_has_numeric_values() {
    	loadTestCase("TC42");
        authUsername = ConfigReader.getConfig("username");
        authPassword = ConfigReader.getConfig("password");
        prepareRequestWithBasicAuth(authUsername, authPassword);
    }

    @Given("User updates request body with country has special characters")
    public void user_updates_request_body_with_country_has_special_characters() {
    	loadTestCase("TC43");
        authUsername = ConfigReader.getConfig("username");
        authPassword = ConfigReader.getConfig("password");
        prepareRequestWithBasicAuth(authUsername, authPassword);
    }

    @Given("User updates request body with country  has alphanumeric values")
    public void user_updates_request_body_with_country_has_alphanumeric_values() {
    	loadTestCase("TC44");
        authUsername = ConfigReader.getConfig("username");
        authPassword = ConfigReader.getConfig("password");
        prepareRequestWithBasicAuth(authUsername, authPassword);
    }

    @Given("User updates request body with zipcode has special characters")
    public void user_updates_request_body_with_zipcode_has_special_characters() {
    	loadTestCase("TC45");
        authUsername = ConfigReader.getConfig("username");
        authPassword = ConfigReader.getConfig("password");
        prepareRequestWithBasicAuth(authUsername, authPassword);
    }

    @Given("User updates request body with zipcode has string values")
    public void user_updates_request_body_with_zipcode_has_string_values() {
    	loadTestCase("TC46");
        authUsername = ConfigReader.getConfig("username");
        authPassword = ConfigReader.getConfig("password");
        prepareRequestWithBasicAuth(authUsername, authPassword);
    }

    @Given("User updates request body with zipcode has alphanumeric values")
    public void user_updates_request_body_with_zipcode_has_alphanumeric_values() {
    	loadTestCase("TC47");
        authUsername = ConfigReader.getConfig("username");
        authPassword = ConfigReader.getConfig("password");
        prepareRequestWithBasicAuth(authUsername, authPassword);
    }

    @Given("User updates request body with existing contactNumber")
    public void user_updates_request_body_with_existing_contact_number() {
    	loadTestCase("TC48");
        authUsername = ConfigReader.getConfig("username");
        authPassword = ConfigReader.getConfig("password");
        prepareRequestWithBasicAuth(authUsername, authPassword);
    }

}
