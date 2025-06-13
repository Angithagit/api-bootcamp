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
	    @DataProvider(name = "getUserByFirstNameData")
	    public static Object[][] getUserByFirstNameData() throws Exception {
	        return JsonUtils.convertToDataProvider("testdata/getuserBy_firstname.json");
	    }
	    @DataProvider(name="getAllUserData")
	    public static Object[][] getAllUsers() throws Exception {
	        return JsonUtils.convertToDataProvider("testdata/get_all_users.json");
	    }
	    @DataProvider(name="deleteById")
	    public static Object[][] deleteById() throws Exception {
	        return JsonUtils.convertToDataProvider("testdata/delete_by_id.json");
	    }
	    @DataProvider(name="updateUserbyId")
	    public static Object[][] updateById() throws Exception {
	        return JsonUtils.convertToDataProvider("testdata/put.json");
	    }
	    @DataProvider(name="deleteByUserName")
	    public static Object[][] deleteByUsername() throws Exception {
	        return JsonUtils.convertToDataProvider("testdata/deleteByUsername.json");
	    }
	    
}
