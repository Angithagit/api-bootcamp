package stepDefinition;

import io.cucumber.java.en.*;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import pojo.Testdata;
import utilities.ConfigReader;
import utilities.JsonDataReader;


import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class PutOperation {
    private Response response;
    private RequestSpecification request;
    private Testdata testdata;
    private String baseUrl;
    private String authUsername;
    private String authPassword;
    private final String jsonFilePath = "src/test/resources/testData/testDataPost.json";

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

    @Given("User updates PUT request with no auth")
    public void user_updates_put_request_with_no_auth() {
        loadTestCase("TC_001");
        baseUrl = ConfigReader.getConfig("baseURL");
        request = given()
                .baseUri(baseUrl)
                .header("Content-Type", "application/json");
               
    }

    @When("User sends HTTP PUT request")
    public void user_sends_http_put_request() {
        response = request.when().put(baseUrl + "/" + testdata.endpoint.replace("{userId}", "26140"));
    }

    @Then("User receives {int} Unauthorized Status Code in response body")
    public void user_receives_unauthorized_status_code_in_response_body(Integer expectedStatusCode) {
        assertThat(response.getStatusCode(), equalTo(expectedStatusCode));
        assertThat(response.statusLine(), containsString(testdata.expectedStatusLine));
    }

    @Given("User updates PUT request with invalid base auth")
    public void user_updates_put_request_with_invalid_base_auth() {
        loadTestCase("TC_002");
        baseUrl = ConfigReader.getConfig("baseURL");
        request = given()
                .baseUri(baseUrl)
                .auth().preemptive().basic("invalidUser", "invalidPass")
                .header("Content-Type", "application/json");
                
    }

    @Given("User updates PUT request with valid end point")
    public void user_updates_put_request_with_valid_end_point() {
        loadTestCase("TC_003");
        baseUrl = ConfigReader.getConfig("baseURL");
        authUsername = ConfigReader.getConfig("username");
        authPassword = ConfigReader.getConfig("password");
        request = RestAssured.given()
                .baseUri(baseUrl)
                .auth().preemptive().basic(authUsername, authPassword)
                .header("Content-Type", "application/json");
    }
                

    @Then("User receives {int} OK Status Code in response body")
    public void user_receives_ok_status_code_in_response_body(Integer int1) {
        assertThat(response.getStatusCode(), equalTo(200));
        assertThat(response.getStatusCode(), equalTo(int1));
        assertThat(response.statusLine(), containsString(testdata.expectedStatusLine));
        assertThat(response.getBody().asString(), containsString(testdata.userFirstName));
        assertThat(response.getHeader("Content-Type"), containsString("application/json"));
    }
}
