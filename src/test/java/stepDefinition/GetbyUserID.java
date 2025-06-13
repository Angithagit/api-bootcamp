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

public class GetbyUserID {
	 private Response response;
	    private RequestSpecification request;
	    private Testdata testdata;
	    private final String jsonFilePath = "src/test/resources/testData/testdata.json";
	    private final String baseUrl = ConfigReader.getConfig("baseURL");
	    private final String authUsername = ConfigReader.getConfig("username");
	    private final String authPassword = ConfigReader.getConfig("password");
	    int userId = PostOperation.userId;
		String firstName = PostOperation.firstName;


	      

	        // Load data for test case by ID
	        private void loadTestCase(String testCaseId) {
	            Map<String, Object> data = JsonDataReader.getTestCaseById(jsonFilePath, testCaseId);
	            testdata = new Testdata();
	            testdata.testCaseId = (String) data.get("testCaseId");
	            testdata.scenarioType = (String) data.get("scenarioType");
	            testdata.method = (String) data.get("method");
	            testdata.userId = (String) data.get("userId");
	            testdata.endpoint = (String) data.get("endpoint");
	            testdata.expectedStatusCode = (Integer) data.get("expectedStatusCode");
	            testdata.expectedStatusLine = (String) data.get("expectedStatusLine");
	        }


	        @Given("Admin sets a GET request with invalid userId")
	        public void given_invalid_user_id() {
	            loadTestCase("TC49");
	        	String endpointWithUserId = testdata.endpoint.replace("{userId}", testdata.userId);
	            // Build request with auth and set endpoint
	            request = RestAssured.given()
	                       .auth()
	                       .basic(authUsername, authPassword)
	                       .baseUri(baseUrl)
	                       .basePath(endpointWithUserId);
	        }

	        @Given("Admin sets GET request without auth")
	        public void given_without_auth() {
	            loadTestCase("TC50");
	            request = RestAssured.given();
	        }

	        @Given("Admin sets GET request without endpoint")
	        public void given_without_endpoint() {
	            loadTestCase("TC51");
	            request = RestAssured.given().auth().basic(authUsername, authPassword);
	        }

	        @Given("Admin sets GET request with invalid end point")
	        public void given_invalid_endpoint() {
	            loadTestCase("TC52");
	            request = RestAssured.given().auth().basic(authUsername, authPassword);
	        }

	        @Given("admin sets POST request with userId and endpoint")
	        public void given_post_request_wrong_method() {
	            loadTestCase("TC53");
	            request = RestAssured.given().auth().basic(authUsername, authPassword);
	        }

	        @Given("Admin sets GET request with userId in string format")
	        public void given_user_id_string() {
	            loadTestCase("TC54");
	            request = RestAssured.given().auth().basic(authUsername, authPassword);
	        }

	        @Given("Admin sets GET request with userId in Alphanumeric values")
	        public void given_user_id_alphanumeric() {
	            loadTestCase("TC55");
	            request =RestAssured.given().auth().basic(authUsername, authPassword);
	        }

	        @Given("Admin sets GET request with userId in special characters")
	        public void given_user_id_special_chars() {
	            loadTestCase("TC56");
	            request = RestAssured.given().auth().basic(authUsername, authPassword);
	        }

	        @Given("Admin sets GET request with userId and valid endpoint")
	        public void given_valid_user_id_endpoint() {
	            loadTestCase("TC57");
	            request = RestAssured.given().auth().basic(authUsername, authPassword);
	        }


	        @When("Admin sends GET request")
	        public void when_send_get_request() {          
	                // Prefer testdata.userId if present (for negative scenarios) otherwise use created userId (positive case)
	                String userIdStr;
	                if (testdata.userId != null && !testdata.userId.toString().isEmpty()) {
	                    userIdStr = testdata.userId.toString();
	                } else {
	                    userIdStr = String.valueOf(PostOperation.userId); // From previously created user
	                }

	                // Replace placeholder in endpoint if it exists
	                String finalEndpoint = testdata.endpoint.contains("{userId}")
	                        ? testdata.endpoint.replace("{userId}", userIdStr)
	                        : testdata.endpoint;
	                response = request.when().get(baseUrl + finalEndpoint);
	            }

	        @When("admin sends POST request")
	        public void when_send_post_request() {
	        	
	        	    String finalEndpoint = testdata.endpoint;
	        	    if (testdata.endpoint.contains("{userId}") && testdata.userId != null) {
	        	        finalEndpoint = testdata.endpoint.replace("{userId}", testdata.userId);
	        	    }
	        	    response = request.when().post(baseUrl + finalEndpoint);
	        	}

	        @Then("admin receives status code {int}  Not found in response body")
	        public void then_not_found_status(int statusCode) {
	            assertThat(response.getStatusCode(), equalTo(statusCode));
	            assertThat(response.statusLine().toLowerCase(), containsString("not found"));
	            LoggerReader.error("Unexpected response: " + response.getBody().asPrettyString());

	        }

	        @Then("admin receives 401 Unauthorized Status Code message in response body")
	        public void then_unauthorized_status() {
	            assertThat(response.getStatusCode(), equalTo(401));
	            assertThat(response.statusLine().toLowerCase(), containsString("unauthorized"));
	        }

	        @Then("admin receives status code {int}  Bad Request in response body")
	        public void then_bad_request_status(int statusCode) {
	            assertThat(response.getStatusCode(), equalTo(statusCode));
	            assertThat(response.statusLine().toLowerCase(), containsString("bad request"));
	            LoggerReader.error("Unexpected response: " + response.getBody().asPrettyString());
	        }

	        @Then("Admin receives 405 Method Not Allowed Status Code message in response body")
	        public void then_method_not_allowed_status() {
	            assertThat(response.getStatusCode(), equalTo(405));
	            assertThat(response.statusLine().toLowerCase(), containsString("method not allowed"));
	        }

	        @Then("Admin receives status code {int} OK in response body")
	        public void then_ok_status(int statusCode) {
	        	
	            response.then()
	                .statusCode(statusCode)
	                .statusLine(containsStringIgnoringCase("ok"))
	                .contentType("application/json")
	                .body("userId", equalTo(PostOperation.userId))
	                .body("userId", instanceOf(Number.class));
	            assertThat(response.getStatusCode(), equalTo(statusCode));
	            assertThat(response.statusLine().toLowerCase(), containsString("ok"));
	        }

	    }
