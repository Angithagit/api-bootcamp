package tests;

import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.Test;

import base.BaseTest;
import dataproviders.BaseDataProvider;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import utils.UserContext;

public class DeleteUserById extends BaseTest {

	@Test(dataProvider = "deleteById", dataProviderClass = BaseDataProvider.class)
	public void deleteUserById(Map<String, Object> userData) {

		String scenarioType = (String) userData.get("scenarioType");
		int expectedStatusCode = (int) userData.get("expectedStatusCode");
		String expectedStatus = (String) userData.get("expectedStatus");
		String expectedMessage = (String) userData.get("expectedMessage");
		String endpoint;

		if ("POSITIVE".equalsIgnoreCase(scenarioType)) {
			// Dynamically captured ID from POST
			int userId = UserContext.getUserId();
			System.out.println("captured user id:" + userId);
			endpoint = "/deleteuser/" + userId;
			System.out.println("endpoint:" + endpoint);
			System.out.println("Base URI: " + RestAssured.baseURI);
			System.out.println("Calling: " + RestAssured.baseURI + endpoint);

		} else {
			// Use ID from JSON for NEGATIVE cases
			Object idObj = userData.get("userId");
			if (idObj == null) {
				// Case like missing ID
				endpoint = "/deleteuser";
			} else {
				endpoint = "/deleteuser/" + idObj.toString();
			}
			System.out.println("Negative scenario → Endpoint: " + endpoint);
		}
		Response response = authrequest.given().when().delete(endpoint);

		// Status Code Validation
		int actualStatusCode = response.getStatusCode();
		System.out.println("Expected Status Code: " + expectedStatusCode);
		System.out.println("Actual Status Code: " + actualStatusCode);
		// Log response body for debugging
		System.out.println("Response Body: " + response.getBody().asString());
		Assert.assertEquals(actualStatusCode, expectedStatusCode, "Status code mismatch");

		// Special logging for known statuses
		switch (actualStatusCode) {
		case 200:
			System.out.println("User deleted successfully.");
			break;
		case 400:
			System.out.println("Bad Request – possibly missing or invalid userId.");
			break;
		case 404:
			System.out.println("User not found – ID may be non-existent.");
			break;
		default:
			System.out.println("Received status code: " + actualStatusCode);
			break;
		}

		// Header Validation
		String contentType = response.getHeader("Content-Type");
		Assert.assertTrue(contentType.contains("application/json"), "Content-Type mismatch");
		System.out.println("Content-Type: " + contentType);

		// Data validation: 'status' and 'message'
		String status = response.jsonPath().getString("status");
		System.out.println("Status: " + status);
		Assert.assertEquals(status, expectedStatus, "Status field mismatch");

		// Validate 'message' from response body
		String message = response.jsonPath().getString("message");

		System.out.println("Status field from response: " + status);
		System.out.println("Message field from response: " + message);

		Assert.assertEquals(status.toLowerCase(), expectedStatus.toLowerCase(), "X->'status' field mismatch");
		Assert.assertEquals(message, expectedMessage, "X-> 'message' field mismatch");

	}
}
