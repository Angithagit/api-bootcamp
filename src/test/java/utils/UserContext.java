package utils;

public class UserContext {
	
	public static int userId;
	
	public static String  userFirstName;
	
	public static String getUserFirstName() {
		return userFirstName;
	}

	public static void setUserFirstName(String name) {
		userFirstName = name;
	}

	public static int getUserId() {
		return userId;
	}

	public static void setUserId(int userId) {
		UserContext.userId = userId;
	}

	

	
	

}
