package dataproviders;

import org.testng.annotations.DataProvider;

import utils.JsonUtils;

public class BaseDataProvider {

	 @DataProvider(name = "createUserData")
	    public static Object[][] postUserData() throws Exception {
	        return JsonUtils.convertToDataProvider("testdata/user_data.json");
	    }

	    @DataProvider(name = "getUserByIdData")
	    public static Object[][] getUserByIdData() throws Exception {
	        return JsonUtils.convertToDataProvider("testdata/user_ids.json");
	    }
}
