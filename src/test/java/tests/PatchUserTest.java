package tests;

import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.Test;

import base.BaseTest;
import dataproviders.BaseDataProvider;
import io.restassured.response.Response;
import utils.UserContext;

public class PatchUserTest extends BaseTest {

	@Test(dataProvider = "updateUserbyId", dataProviderClass = BaseDataProvider.class)
	public void updateByIDTest(Map<String, Object> user) {

		String scenarioType = (String) user.get("scenarioType");
		int expectedStatus = (int) user.get("expectedStatusCode");
		String userfirstname = (String) user.get("userFirstName");

		// Get the dynamic userId stored earlier in UserContext
		Integer userId = UserContext.getUserId(); // assuming getter returns Integer

		if (userId == null) {
			throw new RuntimeException("UserId not found in UserContext. Create user first!");
		}

		// Build endpoint with dynamic userId from UserContext
		String endpoint = "/updateuserfields/" + userId;
		System.out.println("Endpoint is" + endpoint);

		// Remove fields that should NOT be sent in PATCH payload
		user.remove("userId");
		user.remove("creationTime");
		user.remove("lastModTime");

		// Send PATCH request with JSON body constructed from Map
		Response response = authrequest.given().header("Content-Type", "application/json").body(user).when()
				.patch(endpoint).then().extract().response();

		System.out.println("PATCH Response: " + response.getBody().asString());

		if (expectedStatus == 200 && "POSITIVE".equalsIgnoreCase(scenarioType)) {

			Assert.assertEquals(response.statusCode(), expectedStatus, "Status code mismatch");

		} else if (expectedStatus == 400) {
			// Bad Request scenario
			Assert.assertEquals(response.statusCode(), expectedStatus, "Expected 400 Bad Request");

		} else if (expectedStatus == 404) {
			// Not Found scenario
			Assert.assertEquals(response.statusCode(), expectedStatus, "Expected 404 Not Found");

		} else {
			// For other scenarios
			Assert.assertEquals(response.statusCode(), expectedStatus, "Status code mismatch");
		}
	}

}
