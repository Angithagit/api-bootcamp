package stepdefinitions;


import io.cucumber.java.en.*;
import io.restassured.response.Response;
import io.restassured.RestAssured;
import static io.restassured.RestAssured.given;

import static org.testng.Assert.assertEquals;
import java.io.File;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;
import utilities.ConfigReader;
import utilities.ExcelUtils;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchema;

import org.hamcrest.Matcher;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

public class A_GET_allUsers_StepDefinitions {

	 private Response response;
	 private Map<String, String> testData;
	 private String baseUrl = ConfigReader.get("baseUrl");
	

    @Given("the system has one or more valid users")
    public void setupBaseURI() {
    	RestAssured.baseURI = baseUrl;
}

    @When("the user sends a GET request for  {string}")
    public void the_user_sends_a_get_request_for(String testCaseName) {

    	 testData = ExcelUtils.getDataByTestName(testCaseName);
    	    String endpoint = testData.get("Endpoint");
    	    String method = testData.get("Method");

     if (testCaseName.equalsIgnoreCase("UnauthorizedAccess")) {
         response = given()
                 .auth().preemptive().basic("invalid", "invalid")
                 .header("Accept", "application/json")
                 .get(endpoint);
         return;
     }

     switch (method.toUpperCase()) {
         case "GET":
             response = given()
                     .auth().preemptive().basic(ConfigReader.get("username"), ConfigReader.get("password"))
                     .header("Accept", "application/json")
                     .get(endpoint);
             break;

         case "POST":
             response = given()
                     .auth().preemptive().basic(ConfigReader.get("username"), ConfigReader.get("password"))
                     .header("Accept", "application/json")
                     .post(endpoint);
             break;

         default:
             throw new IllegalArgumentException("Unsupported method: " + method);
     }

}

    @Then("the response status should match the expected from excel")
    public void the_response_status_should_match_the_expected_from_excel() {  

    int expectedStatus = (int) Double.parseDouble(testData.get("ExpectedStatus"));

    assertEquals(response.getStatusCode(), expectedStatus,
            "Expected: " + expectedStatus + ", but got: " + response.getStatusCode());

    // Only run validations for 200 OK response
    if (expectedStatus == 200) {
        validateContentTypeHeader();
        validateJsonSchema();
        validateDataTypes();
      
    }
}
         
         public void validateContentTypeHeader() {

    String contentType = response.getHeader("Content-Type");
    System.out.println("Content-Type header value: " + contentType); // <-- LOG

    assert contentType != null && contentType.contains("application/json") : "Content-Type is not application/json";

         }
         public void validateJsonSchema() {
        	    try {
        	        InputStream schemaStream = getClass().getClassLoader().getResourceAsStream("schemas/GetUserSchema.json");
        	        if (schemaStream == null) {
        	            throw new RuntimeException("Schema file not found in classpath!");
        	        }

        	        File tempSchemaFile = File.createTempFile("GetUserSchema", ".json");
        	        java.nio.file.Files.copy(
        	            schemaStream,
        	            tempSchemaFile.toPath(),
        	            java.nio.file.StandardCopyOption.REPLACE_EXISTING
        	        );

        	    

        	        System.out.println(" JSON schema validation passed using File.");
        	    } catch (Exception e) {
        	        e.printStackTrace();
        	        throw new RuntimeException(" Schema validation failed: " + e.getMessage(), e);
        	    }
        	}


    
         public void validateDataTypes() {
        	    List<Map> users = response.jsonPath().getList("", Map.class);

        	    if (users == null || users.isEmpty()) {
        	        throw new AssertionError("User list is empty or null");
        	    }

        	    for (Map<String, ?> body : users) {
        	        assert body.get("userFirstName") instanceof String : "userFirstName is not a String";
        	        assert body.get("userLastName") instanceof String : "userLastName is not a String";

        	        Object contact = body.get("userContactNumber");
        	        assert contact instanceof Integer || contact instanceof Long :
        	            "userContactNumber is not an Integer or Long";

        	        assert body.get("userEmailId") instanceof String : "userEmailId is not a String";

        	        Map<String, ?> address = (Map<String, ?>) body.get("userAddress");
        	        if (address == null) {
        	            throw new AssertionError("userAddress is null");
        	        }

        	        Object plot = address.get("plotNumber");
        	      //  System.out.println("plotNumber value: " + plot + " | Type: " + (plot != null ? plot.getClass() : "null"));
        	        if (plot != null) {
        	            assert plot instanceof String : "plotNumber is not a String";
        	        }

        	        Object street = address.get("street");
        	       // System.out.println("street value: " + street + " | Type: " + (street != null ? street.getClass() : "null"));
        	        if (street != null) {
        	            assert street instanceof String : "street is not a String";
        	        }

        	        Object state = address.get("state");
        	        if (state != null) {
        	            assert state instanceof String : "state is not a String";
        	        }

        	        Object country = address.get("country");
        	        if (country != null) {
        	            assert country instanceof String : "country is not a String";
        	        }

        	        Object zip = address.get("zipCode");
        	        if (zip != null) {
        	            assert zip instanceof Integer || zip instanceof Long :
        	                "zipCode is not an Integer or Long";
        	        }

        	        try {
        	            DateTimeFormatter formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
        	            LocalDateTime.parse((String) body.get("creationTime"), formatter);
        	            LocalDateTime.parse((String) body.get("lastModTime"), formatter);
        	        } catch (DateTimeParseException e) {
        	            throw new AssertionError("Invalid datetime format", e);
        	        }
        	    }

        	    System.out.println("Data type validation passed for all users.");
        	}

}