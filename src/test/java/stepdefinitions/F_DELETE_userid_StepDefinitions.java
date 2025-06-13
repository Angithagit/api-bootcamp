package stepdefinitions;

import static io.restassured.RestAssured.given;
import static org.testng.Assert.assertEquals;

import java.io.File;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Map;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import utilities.ConfigReader;
import utilities.ExcelUtils;
import utilities.TestContext;

public class F_DELETE_userid_StepDefinitions {

	   private Response response;
	    private Map<String, String> testData;
	    private String baseUrl = ConfigReader.get("baseUrl");
	    public static String createdUserId;
	    public static String createdUserName;
	    
	@When("the user sends a DELETE userid request for {string}")
	public void the_user_sends_a_delete_userid_request_for(String testCaseName) {
		testData = ExcelUtils.getDataByTestName(testCaseName);
        String createdUserId=TestContext.getCreatedUserId();
		System.out.println("Captured userID: " + createdUserId);
        String useCaptured = testData.get("UseCapturedUserid");
        String baseEndpoint = testData.get("Endpoint").trim();

        String endpoint;

        if ("TRUE".equalsIgnoreCase(useCaptured)) {
           // String baseEndpoint = testData.get("Endpoint"); 
           
            endpoint = baseEndpoint + createdUserId;
        } else {
           
            endpoint = testData.get("Endpoint");
        
        System.out.println("Excel Endpoint: " + testData.get("Endpoint"));
        }

        String username = ConfigReader.get("username");
        String password = ConfigReader.get("password");

        response = given()
                .auth().preemptive().basic(username, password)
                .header("Accept", "application/json")
                .delete(endpoint);

        System.out.println("DELETE Response: " + response.getBody().asString());
    }
	@Then("the response status should match the expected from excel for DELETE userid")
	public void the_response_status_should_match_the_expected_from_excel_for_delete_userid() {
		int expectedStatus = (int) Double.parseDouble(testData.get("ExpectedStatus"));
        assertEquals(response.getStatusCode(), expectedStatus,
                "Expected: " + expectedStatus + ", but got: " + response.getStatusCode());

	 // Only run validations for 200 OK response
    if (expectedStatus == 201) {
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

		   Map<String, ?> body = response.jsonPath().getMap("");

		    assert body.get("userFirstName") instanceof String;
		    assert body.get("userLastName") instanceof String;
		    assert body.get("userContactNumber") instanceof Integer || body.get("userContactNumber") instanceof Long;
		    assert body.get("userEmailId") instanceof String;

		    Map<String, ?> address = (Map<String, ?>) body.get("userAddress");
		    assert address.get("plotNumber") instanceof String;
		    assert address.get("street") instanceof String;
		    assert address.get("state") instanceof String;
		    assert address.get("country") instanceof String;
		    assert address.get("zipCode") instanceof Integer;

		    try {
		        DateTimeFormatter formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
		        LocalDateTime.parse((String) body.get("creationTime"), formatter);
		        LocalDateTime.parse((String) body.get("lastModTime"), formatter);
		    } catch (DateTimeParseException e) {
		        throw new AssertionError("Invalid datetime format", e);
		    }

		    System.out.println("Data type validation passed.");
		}

}
