package stepDefinition;

import io.cucumber.java.en.*;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import pojo.Testdata;
import utilities.ConfigReader;
import utilities.JsonDataReader;

import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class GetAllUsers {

    private Response response;
    private RequestSpecification request;
    private Testdata testdata;
    private final String jsonFilePath = "src/test/resources/testData/testdata.json"; // updated filename
    private final String baseUrl = ConfigReader.getConfig("baseURL");
    private final String authUsername = ConfigReader.getConfig("username");
    private final String authPassword = ConfigReader.getConfig("password");

    private void loadTestCase(String testCaseId) {
        Map<String, Object> data = JsonDataReader.getTestCaseById(jsonFilePath, testCaseId);
        testdata = new Testdata();
        testdata.testCaseId = (String) data.get("testCaseId");
        testdata.scenarioType = (String) data.get("scenarioType");
        testdata.method = (String) data.get("method");
        testdata.endpoint = (String) data.get("endpoint");
        testdata.expectedStatusCode = (Integer) data.get("expectedStatusCode");
        testdata.expectedStatusLine = (String) data.get("expectedStatusLine");
        
    }

    // TC01 - Valid GET Request
    @Given("Admin sets the GET request with valid endpoint")
    public void admin_sets_the_get_request_with_valid_endpoint() {
        loadTestCase("TC01");
        request = RestAssured.given().baseUri(baseUrl);
        request.auth().preemptive().basic(authUsername, authPassword);
    }

    @When("Admin sends GET Request with endpoint")
    public void admin_sends_get_request_with_endpoint() {
        response = request.when().get(testdata.endpoint);
    }

    @Then("Admin receives {int} OK Status Code and should display all the users in response body")
    public void admin_receives_ok_status_code_and_should_display_all_the_users_in_response_body(Integer statusCode) {
        assertThat(response.statusCode(), is(statusCode));
        assertThat(response.getBody().asString(), not(emptyOrNullString()));
    }

    // TC02 - Without endpoint
    @Given("Admin sets the GET request without endpoint")
    public void admin_sets_the_get_request_without_endpoint() {
        loadTestCase("TC02");
        request = RestAssured.given().baseUri(baseUrl);
        request.auth().preemptive().basic(authUsername, authPassword);
    }

    @When("Admin sends GET Request without endpoint")
    public void admin_sends_get_request_without_endpoint() {
        response = request.when().get();
    }

    @Then("Admin receives {int} Not Found Status Code")
    public void admin_receives_not_found_status_code(Integer statusCode) {
        assertThat(response.statusCode(), is(statusCode));
        assertThat(response.statusLine(), containsString("Not Found"));
    }

    // TC03 - Invalid endpoint
    @Given("Admin sets the GET request with invalid endpoint")
    public void admin_sets_the_get_request_with_invalid_endpoint() {
        loadTestCase("TC03");
        request = RestAssured.given().baseUri(baseUrl);
        request.auth().preemptive().basic(authUsername, authPassword);
    }

    @When("Admin sends GET Request with invalid endpoint")
    public void admin_sends_get_request_with_invalid_endpoint() {
        response = request.when().get(testdata.endpoint);
    }

    @Then("Admin receives {int} Not Found in response body")
    public void admin_receives_not_found_in_response_body(Integer statusCode) {
        assertThat(response.statusCode(), is(statusCode));
        assertThat(response.statusLine(), containsString("Not Found"));
    }

    // TC04 - Invalid request type (POST instead of GET)
    @Given("Admin sets the POST request with valid endpoint")
    public void admin_sets_the_post_request_with_valid_endpoint() {
        loadTestCase("TC04");
        request = RestAssured.given().baseUri(baseUrl);
        request.auth().preemptive().basic(authUsername, authPassword);
    }

    @When("Admin sends POST Request with endpoint")
    public void admin_sends_post_request_with_endpoint() {
        response = request.when().post(testdata.endpoint);
    }

    @Then("Admin receives {int} Method Not Allowed")
    public void admin_receives_method_not_allowed(Integer statusCode) {
        assertThat(response.statusCode(), is(statusCode));
        assertThat(response.statusLine(), containsString("Method Not Allowed"));
    }

    // TC05 - No Authorization
    @Given("Admin sets the GET request with valid endpoint and no authorization")
    public void admin_sets_the_get_request_with_valid_endpoint_and_no_authorization() {
        loadTestCase("TC05");
        request = RestAssured.given().baseUri(baseUrl); // No auth
    }

    @When("Admin sends HTTPS Request with endpoint")
    public void admin_sends_https_request_with_endpoint() {
        response = request.when().get(testdata.endpoint);
    }

    @Then("Admin receives {int} Unauthorized Status Code")
    public void admin_receives_unauthorized_status_code(Integer statusCode) {
        assertThat(response.statusCode(), is(statusCode));
        assertThat(response.statusLine(), containsString("Unauthorized"));
    }
}
