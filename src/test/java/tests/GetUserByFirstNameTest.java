package tests;

import java.util.List;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.Test;

import base.BaseTest;
import dataproviders.BaseDataProvider;
import io.restassured.response.Response;
import utils.UserContext;
import static org.hamcrest.Matchers.equalTo;

public class GetUserByFirstNameTest extends BaseTest {

	@Test(dataProvider = "getUserByFirstNameData", dataProviderClass = BaseDataProvider.class)
	public void getUserByFirstNameTest(Map<String, Object> userTestData) {
		String scenarioType = (String) userTestData.get("scenarioType");
		String userFirstName;

		// Use dynamic value from POST test for POSITIVE scenario
		if ("POSITIVE".equalsIgnoreCase(scenarioType)) {
			userFirstName = UserContext.getUserFirstName();
			System.out.println("captured user firstname:" + userFirstName);
			Assert.assertNotNull(userFirstName, "No userFirstName available from POST test");
		} else {
			// For NEGATIVE scenarios, use the static test data
			userFirstName = (String) userTestData.get("userFirstName");
		}

		int expectedStatusCode = (int) userTestData.get("expectedStatusCode");

		// call the endpoint
		Response response = authrequest.given().log().all().when().get("/users/username/" + userFirstName);

		// status code validation
		response.then().log().all().statusCode(expectedStatusCode);

		// Header validation
		Assert.assertEquals(response.getHeader("Content-Type"), "application/json", "X:Content-Type mismatch");
		Assert.assertNotNull(response.getHeader("Server"), "X:Server header missing");

		if (expectedStatusCode == 200 && "POSITIVE".equalsIgnoreCase(scenarioType)) {
			// If response is a list, use getList
			List<String> firstNames = response.jsonPath().getList("userFirstName");
			Assert.assertNotNull(firstNames, "No userFirstName found in response");
			Assert.assertFalse(firstNames.isEmpty(), "userFirstName list is empty");

			String actualFirstName = firstNames.get(0); // First element
			System.out.println("Extracted firstName from response: " + actualFirstName);
			Assert.assertEquals(actualFirstName, userFirstName, "Username mismatch after GET by username");

		} else {
			// NEGATIVE: Validate error message in response
			String errorMessage = response.jsonPath().getString("message");
			Assert.assertNotNull(errorMessage, "Error message is missing");
			Assert.assertTrue(
					errorMessage.toLowerCase().contains("not found")
							|| errorMessage.toLowerCase().contains("bad request"),
					"Unexpected error message for negative scenario");
			System.out.println("Negative scenario passed for: " + userFirstName);
		}

	}
}
