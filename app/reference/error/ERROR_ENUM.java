package reference.error;

public enum ERROR_ENUM {
	ERR_NOERROR					(0,""),

	ERR_METHOD_NOT_FOUND			(10,"medthod is not found"),
	ERR_METHOD_PARAMS			(11,"parameter error"),
	ERR_NETWORK_NOT_SUPPORT		(12,"network doesn't support"),
	ERR_ALGORITHM_NOT_SUPPORT	(13,"algorithm doesn't support"),
	ERR_SIGNATURE_WRONG			(14,"sigature is wrong"),
	ERR_ADDRESS_WRONG			(15,"address is wrong"),
	ERR_USER_NOT_FOUND			(16,"user not found"),
	ERR_USER_EXISTED				(17,"user has existed"),
	ERR_TWITTER_API_ERROR			(18,"twitter api error"),
	ERR_TWITTER_VERIFIED			(19,"twitter account has verified"),
	ERR_USER_PROFILE				(101,"get user profile error"),
	ERR_AUTH_FAIL					(999,"authentication fail"),
	ERR_UNKNOW_ERROR				(9999,"unknown error");

	private final int id;
	private String msg;

	ERROR_ENUM(int id, String msg) {
		this.id = id;
		this.msg = msg;
	}

	public int getId() {
		return id;
	}

	public String getMsg() {
		return msg;
	}
}
