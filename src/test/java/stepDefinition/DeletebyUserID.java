package stepDefinition;

import io.cucumber.java.en.*;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import pojo.Testdata;
import utilities.ConfigReader;
import utilities.JsonDataReader;

import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class DeletebyUserID {

    private Response response;
    private RequestSpecification request;
    private Testdata testdata;
    private final String jsonFilePath = "src/test/resources/testData/testdata.json";
    private final String baseUrl = ConfigReader.getConfig("baseURL");
    private final String authUsername = ConfigReader.getConfig("username");
    private final String authPassword = ConfigReader.getConfig("password");
    int userId = PostOperation.userId;
	String firstName = PostOperation.firstName;

    private void loadTestCase(String testCaseId) {
        Map<String, Object> dataMap = JsonDataReader.getTestCaseById(jsonFilePath, testCaseId);
        testdata = new Testdata();
        testdata.testCaseId = (String) dataMap.get("testCaseId");
        testdata.scenarioType = (String) dataMap.get("scenarioType");
        testdata.endpoint = (String) dataMap.get("endpoint");
        testdata.method = (String) dataMap.get("method");
        testdata.userId = (String) dataMap.get("userId");
        testdata.expectedStatusCode = (Integer) dataMap.get("expectedStatusCode");
        testdata.expectedStatusLine = (String) dataMap.get("expectedStatusLine");
        testdata.expectedStatusmessage = (String) dataMap.getOrDefault("expectedStatusmessage", "");
    }

    @Given("Admin sets DELETE request with userId and valid endpoint")
    public void admin_sets_delete_request_with_user_id_and_valid_endpoint() {
        loadTestCase("TC68");
        request = given()
                .auth().basic(authUsername, authPassword)
                .contentType("application/json");
    }
    
    @Given("Admin sets DELETE request with userId")
    public void admin_sets_delete_request_with_user_id() {
        loadTestCase("TC60");
        request = given()
                .auth().basic(authUsername, authPassword)
                .contentType("application/json");
    }
   
    @When("admin sends DELETE request")
    public void admin_sends_delete_request() {
        String userIdStr;
        // Use userId from JSON unless test case is positive and needs real userId
        if ("Positive".equalsIgnoreCase(testdata.scenarioType)) {
            userIdStr = String.valueOf(PostOperation.userId);
        } else {
            userIdStr = testdata.userId;
        }

        String finalEndpoint = testdata.endpoint.contains("{userId}")
                ? testdata.endpoint.replace("{userId}", userIdStr)
                : testdata.endpoint;

        System.out.println("Sending DELETE to endpoint: " + baseUrl + finalEndpoint);
        response = request.when().delete(baseUrl + finalEndpoint);
    }

    @Given("Admin sets DELETE request with invalid userId")
    public void admin_sets_delete_request_with_invalid_user_id() {
        loadTestCase("TC59");
        request = given()
                .auth().basic(authUsername, authPassword)
                .contentType("application/json");
    }

    @Then("admin receives status code {int}  Not found")
    public void admin_receives_status_code_not_found(Integer expectedCode) {
        assertThat(response.getStatusCode(), equalTo(expectedCode));
        assertThat(response.statusLine().toLowerCase(), containsString("not found"));
    }

    @Then("Admin receives status code {int}  Bad Request in response body")
    public void admin_receives_status_code_bad_request_in_response_body(Integer expectedCode) {
        assertThat(response.getStatusCode(), equalTo(expectedCode));
        assertThat(response.statusLine().toLowerCase(), containsString("bad request"));
    }

    @Given("Admin sets DELETE request without auth")
    public void admin_sets_delete_request_without_auth() {
        loadTestCase("TC61");
        request = given()
                .contentType("application/json");  // No auth
    }

    @Then("admin receives status code {int} Unauthorized in response body")
    public void admin_receives_status_code_unauthorized_status_code_in_response_body(Integer expectedCode) {
        assertThat(response.getStatusCode(), equalTo(expectedCode));
        assertThat(response.statusLine().toLowerCase(), containsString("unauthorized"));
    }

    @Given("Admin sets DELETE request without endpoint")
    public void admin_sets_delete_request_without_endpoint() {
        loadTestCase("TC62");
        request = given()
                .auth().basic(authUsername, authPassword)
                .contentType("application/json");
    }

    @Given("Admin sets DELETE request with invalid end point")
    public void admin_sets_delete_request_with_invalid_end_point() {
        loadTestCase("TC63");
        request = given()
                .auth().basic(authUsername, authPassword)
                .contentType("application/json");
    }

    @Given("Admin sets POST request with userId and endpoint")
    public void admin_sets_post_request_with_user_id_and_endpoint() {
        loadTestCase("TC64");
        request = given()
                .auth().basic(authUsername, authPassword)
                .contentType("application/json");
    }

    @When("admin sends HTTPS POST request")
    public void admin_sends_HTTPS_post_request() {
        String finalEndpoint = testdata.endpoint.replace("{userId}", testdata.userId);
        response = request.when().post(baseUrl + finalEndpoint);
    }

    @Then("admin receives status code {int} Method Not Allowed Status Code in response body")
    public void admin_receives_status_code_method_not_allowed_status_code_in_response_body(Integer expectedCode) {
        assertThat(response.getStatusCode(), equalTo(expectedCode));
        assertThat(response.statusLine().toLowerCase(), containsString("method not allowed"));
    }

    @Given("Admin sets DELETE request with userId in string format")
    public void admin_sets_delete_request_with_user_id_in_string_format() {
        loadTestCase("TC65");
        request = given()
                .auth().basic(authUsername, authPassword)
                .contentType("application/json");
    }

    @Given("Admin sets DELETE request with userId in Alphanumeric values")
    public void admin_sets_delete_request_with_user_id_in_alphanumeric_values() {
        loadTestCase("TC66");
        request = given()
                .auth().basic(authUsername, authPassword)
                .contentType("application/json");
    }

    @Given("Admin sets DELETE request with userId in special characters")
    public void admin_sets_delete_request_with_user_id_in_special_characters() {
        loadTestCase("TC67");
        request = given()
                .auth().basic(authUsername, authPassword)
                .contentType("application/json");
    }
    
    @Then("admin receives status code {int} OK with message in response body")
    public void admin_receives_status_code_ok_with_message_in_response_body(Integer expectedStatusCode) {
    	 response.then()
         .statusCode(expectedStatusCode)
         .statusLine(containsStringIgnoringCase("ok"))
         .contentType("application/json")
         .body("message", equalTo(testdata.expectedStatusmessage));
        assertThat(response.getStatusCode(), is(expectedStatusCode));
        assertThat(response.getStatusLine().toUpperCase(), containsString("OK"));
        // Optionally verify the response message contains something expected
        System.out.println("Response Body: " + response.getBody().asPrettyString());
    }
}
