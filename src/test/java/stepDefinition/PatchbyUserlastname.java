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

public class PatchbyUserlastname {

    private Response response;
    private RequestSpecification request;
    private Testdata testdata;
    private final String jsonFilePath = "src/test/resources/testData/testdata.json";
    public static int userId;
	static String firstName;
    private final String baseUrl = ConfigReader.getConfig("baseURL");
    private final String authUsername = ConfigReader.getConfig("username");
    private final String authPassword = ConfigReader.getConfig("password");
	private ChainValues passvalues=new ChainValues(); ;
    
  
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

    @Given("Admin updates request body for UserLastname")
    public void admin_updates_request_body_for_User_Lastname() {
        loadTestCase("TC58");
    }

    @When("Admin sends HTTP PATCH request")
    public void admin_sends_http_post_request() {
    	String endpointWithId = testdata.endpoint.replace("{userId}", String.valueOf(PostOperation.userId));

        // Only updating last name
        Map<String, Object> patchBody = Map.of("userLastName", testdata.userLastName);

        response = given()
                .auth().preemptive().basic(authUsername, authPassword)
                .contentType("application/json")
                .body(patchBody)
                .when()
                .patch(baseUrl + endpointWithId);
    	}

    

    @Then("user receives Status code 200 OK with response body")
    public void user_receives_status_code_200_ok_with_response_body() {
    	
    	response.then()
        .statusCode(testdata.expectedStatusCode)
        .statusLine(testdata.expectedStatusLine)
        .contentType("application/json")
        .body("userLastName", equalTo(testdata.userLastName));
        /*.body("userContactNumber", equalTo((int)testdata.userContactNumber))  // if needed, cast to int
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
    	        .body("userAddress.zipCode", instanceOf(Number.class));*/

    	    userId = response.path("userId");
    	    firstName = response.path("userFirstName").toString();
    	    passvalues.getuserData().setUserId(userId);
    	    passvalues.getuserData().setUserFirstName(firstName);
    	    LoggerReader.info("User ID: " + userId);
    	    LoggerReader.info("First Name: " + firstName);
    	    LoggerReader.info("Last Name: " + firstName);
    	    assertThat(response.getStatusCode(), is(testdata.expectedStatusCode));
    	    assertThat(response.getStatusLine(), containsString(testdata.expectedStatusLine));
    	    LoggerReader.info("Response Body: " + response.getBody().asPrettyString());
    	}
}


