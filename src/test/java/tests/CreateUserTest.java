package tests;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.Test;

import base.BaseTest;
import dataproviders.BaseDataProvider;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import utils.UserContext;
import static org.hamcrest.Matchers.equalTo;

public class CreateUserTest extends BaseTest {

	@Test(dataProvider = "createUserData", dataProviderClass = BaseDataProvider.class)
	public void createUserTest(Map<String, Object> user) {
		String scenarioType = (String) user.get("scenarioType");
		int expectedStatus = (int) user.get("expectedStatusCode");
		String userfirstname = (String) user.get("userFirstName");

		// Prepare payload
		Map<String, Object> userAddress = (Map<String, Object>) user.get("userAddress");
		user.put("userAddress", userAddress);

		// Remove test-only data before sending the request

		user.remove("scenarioType");
		user.remove("expectedStatusCode");

		Response response = authrequest.given().log().all() // Log full request
				.contentType(ContentType.JSON).body(user).when().post("/createusers");

		System.out.println("Response: " + response.asString());

		int actualStatusCode = response.getStatusCode();
		System.out.println("Expected: " + expectedStatus + ", Actual: " + actualStatusCode);

		// status code validation
		Assert.assertEquals(actualStatusCode, expectedStatus, "status code mismatch");

		// Header Validation
		Assert.assertEquals(response.getHeader("Content-Type"), "application/json", "X->Content-Type mismatch");
		Assert.assertNotNull(response.getHeader("Server"), "X-> Server header missing");

		try {
			response.then().log().all().statusCode(expectedStatus);

			if (expectedStatus == 201 && "POSITIVE".equalsIgnoreCase(scenarioType)) {
				// schema/datatype Validation
				response.then().body(matchesJsonSchemaInClasspath("schemas/schema.json"));
				// Get userId from response and save it
				int userId = response.jsonPath().getInt("userId");
				System.out.println("Created userId: " + userId);
				UserContext.setUserId(userId); // this should store it for use in GET test

				String userFirstName1 = response.jsonPath().getString("userFirstName");
				Assert.assertEquals(userFirstName1, userfirstname, "Username mismatch after GET by username");

				System.out.println("Created userFirstName: " + userFirstName1);
				UserContext.setUserFirstName(userFirstName1);
				// this should store it for use in GET test

				// Data Value Validation — comparing expected with actual response
				response.then().body("userFirstName", equalTo(user.get("userFirstName")))
						.body("userLastName", equalTo(user.get("userLastName")))
						.body("userEmailId", equalTo(user.get("userEmailId")))
						.body("userContactNumber", equalTo(user.get("userContactNumber")))
						.body("userAddress.plotNumber", equalTo(userAddress.get("plotNumber")))
						.body("userAddress.street", equalTo(userAddress.get("street")))
						.body("userAddress.state", equalTo(userAddress.get("state")))
						.body("userAddress.country", equalTo(userAddress.get("country")))
						.body("userAddress.zipCode", equalTo(userAddress.get("zipCode")));
			}

		} catch (AssertionError e) {
			System.err.println("X-> Test failed: Expected " + expectedStatus + " but got " + actualStatusCode);
			throw e;
		}
	}
}
