package utilities;

public class TestContext {
    private static String createdUserId;
    private static String createdUserName;


    public static String getCreatedUserId() {
        return createdUserId;
    }

    public static void setCreatedUserId(String userId) {
        createdUserId = userId;
    }

	public static String getCreatedUserName() {
		 return createdUserName;
	}
	 public static void setCreatedUserName(String username) {
	        createdUserName = username;
	 }
}
