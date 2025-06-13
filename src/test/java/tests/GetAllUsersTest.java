package tests;

import java.util.List;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.Test;

import base.BaseTest;
import dataproviders.BaseDataProvider;
import io.restassured.response.Response;

public class GetAllUsersTest extends BaseTest {

	@Test(dataProvider = "getAllUserData", dataProviderClass = BaseDataProvider.class)
	public void getAllUsers(Map<String, Object> userdata) {
		try {
			// Extract input data from JSON
			String scenarioType = (String) userdata.get("scenarioType");
			String httpMethod = (String) userdata.get("httpMethod");
			String endpoint = (String) userdata.get("endpoint");
			int expectedStatusCode = (int) userdata.get("expectedStatusCode");
			String expectedError = (String) userdata.getOrDefault("error", null);

			Response response;

			// Send request based on HTTP method
			switch (httpMethod.toUpperCase()) {
			case "GET":
				response = authrequest.given().log().all().when().get(endpoint);
				break;
			case "POST":
				response = authrequest.given().log().all().when().post(endpoint);
				break;
			default:
				throw new IllegalArgumentException("Unsupported HTTP method: " + httpMethod);
			}

			// Status Code Validation
			int actualStatusCode = response.getStatusCode();
			Assert.assertEquals(actualStatusCode, expectedStatusCode, "Status code mismatch");
			System.out.println("Status code is as expected: " + actualStatusCode);

			// Header Validation
			String contentType = response.getHeader("Content-Type");
			Assert.assertTrue(contentType.contains("application/json"), "Content-Type mismatch");
			System.out.println("Content-Type: " + contentType);

			// Body Validation
			if ("POSITIVE".equalsIgnoreCase(scenarioType) && expectedStatusCode == 200) {
				List<Map<String, Object>> users = response.jsonPath().getList("$");
				System.out.println("Total users fetched: " + users.size());
			} else {
				// NEGATIVE scenario
				String actualError = response.jsonPath().getString("error");
				String message = response.jsonPath().getString("message");

				System.out.println("X-->Error: " + actualError);
			}

		} catch (Exception e) {
			System.err.println("X-->TEST FAILED: " + e.getMessage());
			e.printStackTrace();
			throw e; // Re-throw for TestNG to mark as failed
		}
	}
}