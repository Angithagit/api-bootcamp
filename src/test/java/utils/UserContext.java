package utils;

public class UserContext {
	
	public static int createdUserId;

	public static int getCreatedUserId() {
		return createdUserId;
	}

	public static void setCreatedUserId(int createdUserId) {
		UserContext.createdUserId = createdUserId;
	}

}
