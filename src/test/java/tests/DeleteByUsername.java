package tests;

import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.Test;

import base.BaseTest;
import dataproviders.BaseDataProvider;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import utils.UserContext;

public class DeleteByUsername extends BaseTest {

	@Test(dataProvider = "deleteByUserName", dataProviderClass = BaseDataProvider.class)
	public void deleteUserByUsername(Map<String, Object> user) {
		String scenarioType = (String) user.get("scenarioType");
		int expectedStatusCode = (int) user.get("expectedStatusCode");
		String expectedMessage = (String) user.get("expectedMessage");
		String endpoint;

		if ("POSITIVE".equalsIgnoreCase(scenarioType)) {
			// Dynamically captured ID from POST
			String userFirstName = UserContext.getUserFirstName();
			System.out.println("captured user firstname:" + userFirstName);
			endpoint = "/deleteuser/username/" + userFirstName;
			System.out.println("endpoint for positive:" + endpoint);
			System.out.println("Base URI: " + RestAssured.baseURI);
			System.out.println("Calling: " + RestAssured.baseURI + endpoint);

		} else {
			// Use username from JSON for NEGATIVE cases
			Object idObj = user.get("userFirstName");
			if (idObj == null) {
				// Case like missing userFirstName
				endpoint = "/deleteuser/username/";
			} else {
				endpoint = "/deleteuser/username/" + idObj.toString();
			}
			System.out.println("Negative scenario → Endpoint: " + endpoint);
		}
		Response response = authrequest.given().when().delete(endpoint);

		// Status Code Validation
		int actualStatusCode = response.getStatusCode();
		System.out.println("Expected Status Code: " + expectedStatusCode);
		System.out.println("Actual Status Code: " + actualStatusCode);
		Assert.assertEquals(actualStatusCode, expectedStatusCode, "Status code mismatch");

		// Header Validation
		String contentType = response.getHeader("Content-Type");
		Assert.assertTrue(contentType.contains("application/json"), "Content-Type mismatch");
		System.out.println("Content-Type: " + contentType);

		// Response body validation
		if (expectedStatusCode == 200 || expectedStatusCode == 404 || expectedStatusCode == 400) {
			String actualMessage = response.jsonPath().getString("message");
			System.out.println("Response Message: " + actualMessage);
			Assert.assertEquals(actualMessage, expectedMessage, "Message mismatch");
		}

	}
}
