package tests;

import org.testng.annotations.Test;

import base.BaseTest;
import dataproviders.BaseDataProvider;
import io.restassured.response.Response;
import utils.UserContext;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

import java.util.Map;

public class GetUserByIdTest extends BaseTest {
	
	@Test(dataProvider = "getUserByIdData", dataProviderClass = BaseDataProvider.class)
	public void getUserByIdTest(Map<String, Object> data) {
		 System.out.println("Running GetUserByIdTest with data: " + data);
		 String scenarioType = (String) data.get("scenarioType");
		    int expectedStatus = (int) data.get("expectedStatusCode");
		    
		    String endpoint;
		    
		    if ("POSITIVE".equalsIgnoreCase(scenarioType)) {
		        // Dynamically captured ID from POST
		    	int userId = UserContext.getCreatedUserId();
		    	System.out.println("captured user id:"+userId);
		        endpoint = "/user/" + userId;
		        System.out.println("endpoint:"+endpoint);
		    } else {
		        // Use ID from JSON for NEGATIVE cases
		        Object idObj = data.get("userId");
		        if (idObj == null) {
	                // Case like missing ID
	                endpoint = "/user";
	            } else {
	                endpoint = "/user/" + idObj.toString(); // e.g., non-existent ID, string instead of int
	            }
		    }
		    	    
	    
	    Response response = authrequest
	        .given()
	        .when()
	        .get(endpoint);
	    
	    // Log and assert status code
        int actualStatusCode = response.getStatusCode();
        System.out.println("Expected status code: " + expectedStatus);
        System.out.println("Actual status code: " + actualStatusCode);
	    
        try
        {
	    response.then()
	        .statusCode(expectedStatus)
	        .log().all();
	    if (expectedStatus == 200 && "POSITIVE".equalsIgnoreCase(scenarioType)) {
	        // Validate response schema only for positive
	        response.then().body(matchesJsonSchemaInClasspath("schemas/schema.json"));
	    }
        }catch(AssertionError e)
        {
        	 System.err.println("❌ Test failed: Expected " + expectedStatus + " but got " + actualStatusCode);
             throw e; // Fail the test
        }
	        	
	}

}
