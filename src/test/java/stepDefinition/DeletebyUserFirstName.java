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

public class DeletebyUserFirstName {

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
        testdata.userFirstName = (String) dataMap.get("userFirstName");
        testdata.expectedStatusCode = (Integer) dataMap.get("expectedStatusCode");
        testdata.expectedStatusLine = (String) dataMap.get("expectedStatusLine");
        testdata.expectedStatusmessage = (String) dataMap.getOrDefault("expectedMessage", "");
    }


    private void setRequestWithAuth() {
        String endpoint = testdata.endpoint.replace("{userFirstName}", testdata.userFirstName);
        request = given()
                .baseUri(baseUrl)
                .basePath(endpoint)
                .auth()
                .preemptive()
                .basic(authUsername, authPassword);
    }

    @Given("user sets DELETE request with invalid userFirstName")
    public void admin_sets_delete_request_with_invalid_user_first_name() {
        loadTestCase("TC69");
        setRequestWithAuth();
    }

    @Given("user sets DELETE request with userFirstName which is already deleted")
    public void admin_sets_delete_request_with_user_first_name_which_is_already_deleted() {
        loadTestCase("TC70"); // Use TC10 for positive test
        setRequestWithAuth();
    }

    @Given("user sets DELETE request with userFirstName and valid endpoint")
    public void admin_sets_delete_request_with_user_first_name_and_valid_endpoint() {
        loadTestCase("TC78"); // Use TC10 for positive test
        setRequestWithAuth();
    }

    @Given("user sets DELETE request without authorizarion")
    public void admin_sets_delete_request_without_authorizarion() {
        loadTestCase("TC71");
        String endpoint = testdata.endpoint.replace("{userFirstName}", testdata.userFirstName);
        request = given()
                .baseUri(baseUrl)
                .basePath(endpoint);
    }

    @Given("user sets DELETE request By userFirstName and without endpoint")
    public void admin_sets_delete_request_by_user_first_name_and_without_endpoint() {
        loadTestCase("TC72");
        request = given()
                .baseUri(baseUrl)
                .basePath("")
                .auth()
                .preemptive()
                .basic(authUsername, authPassword);
    }

    @Given("user sets DELETE request By userFirstName with invalid end point")
    public void admin_sets_delete_request_by_user_first_name_with_invalid_end_point() {
        loadTestCase("TC73");
        request = given()
                .baseUri(baseUrl)
                .basePath(testdata.endpoint)
                .auth()
                .preemptive()
                .basic(authUsername, authPassword);
    }

    @Given("user sets POST request with userFirstName and endpoint")
    public void admin_sets_post_request_with_user_first_name_and_endpoint() {
        loadTestCase("TC74");
        String endpoint = testdata.endpoint.replace("{userFirstName}", testdata.userFirstName);
        request = given()
                .baseUri(baseUrl)
                .basePath(endpoint)
                .auth()
                .preemptive()
                .basic(authUsername, authPassword);
    }

    @Given("user sets DELETE request with userFirstName in Alphanumeric values")
    public void admin_sets_delete_request_with_user_first_name_in_alphanumeric_values() {
        loadTestCase("TC76");
        setRequestWithAuth();
    }

    @Given("user sets DELETE request with userFirstName in special characters")
    public void admin_sets_delete_request_with_user_first_name_in_special_characters() {
        loadTestCase("TC77");
        setRequestWithAuth();
    }

    @Given("user sets DELETE request with userFirstName in numeric values")
    public void admin_sets_delete_request_with_user_first_name_in_numeric_values() {
        loadTestCase("TC75");
        setRequestWithAuth();
    }


    @When("user sends DELETE request")
    public void admin_sends_delete_request() {
    	String fname = String.valueOf(PostOperation.firstName);
        String finalEndpoint = testdata.endpoint.contains("{userFirstName}")
                ? testdata.endpoint.replace("{userFirstName}", fname)
                : testdata.endpoint;
        response = request.when().delete();
    }

    @When("user sends POST request")
    public void admin_sends_post_request() {
        response = request.when().post();
    }

    @Then("user receives status code {int} {string} in response body")
    public void admin_receives_status_code_with_message(int expectedCode, String expectedLine) {
        assertThat(response.getStatusCode(), is(expectedCode));
        assertThat(response.getStatusLine(), containsString(expectedLine));
    }
    @Then("user receives {int} Unauthorized Status Code in response body")
    public void admin_receives_unauthorized_status_code_in_response_body(Integer expectedCode) {
        assertThat(response.getStatusCode(), equalTo(expectedCode));
        assertThat(response.statusLine().toLowerCase(), containsString("unauthorized"));
    }

    @Then("user receives status code {int}  Not found in response body")
    public void admin_receives_status_code_not_found_in_response_body(Integer expectedCode) {
        assertThat(response.getStatusCode(), equalTo(expectedCode));
        assertThat(response.statusLine().toLowerCase(), containsString("not found"));
    }
    @Then("user receives status code 200 OK with message in response body")
    public void admin_receives_status_code_200_ok_with_message_in_response_body() {
    	response.then()
        .statusCode(testdata.expectedStatusCode)
        .statusLine(containsStringIgnoringCase("ok"))
        .contentType("application/json")
        .body("userFirstName", equalTo(PostOperation.firstName));     
       assertThat(response.getStatusCode(), is(testdata.expectedStatusCode));
       assertThat(response.getStatusLine().toUpperCase(), containsString("OK"));

    }
    @Then("user receives {int} Method Not Allowed Status Code in response body")
    public void admin_receives_method_not_allowed_status_code_in_response_body(Integer expectedCode) {
        assertThat(response.getStatusCode(), equalTo(expectedCode));
        assertThat(response.statusLine().toLowerCase(), containsString("method not allowed"));
    }

}
