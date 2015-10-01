package twitterhandler;

public final class TwitterCredentials {
	private static String consumer_key = "";
	private static String consumer_secret_key = "";
	private static String user_access_token = "";
	private static String user_access_secret_token = "";
	private static boolean isVerified = false;
	private static String uID = "";
	private static String uScreenName = "";
	private static String outputFile = "";
	private static String keyWords = "";
	private static String sID = "";
	private static String inputFile = "";
	private static String tSname = "";
	private static String tTname = "";
	private static String cSlug = "";
	private static String lid = "";
	private static String pid = "";
	private static String latitude = "";
	private static String longitude = "";
	private static String ip_address = "";
	private static String placeName = "";
	private static String woeid = "";
	
	
	public static String getLatitude() {
		return latitude;
	}

	public static void setLatitude(String latitude) {
		TwitterCredentials.latitude = latitude;
	}

	public static String getLongitude() {
		return longitude;
	}

	public static void setLongitude(String longitude) {
		TwitterCredentials.longitude = longitude;
	}

	public static String getIp_address() {
		return ip_address;
	}

	public static void setIp_address(String ip_address) {
		TwitterCredentials.ip_address = ip_address;
	}

	public static String getuID() {
		return uID;
	}

	public static void setuID(String uID) {
		TwitterCredentials.uID = uID;
	}

	public static String getuScreenName() {
		return uScreenName;
	}

	public static void setuScreenName(String uScreenName) {
		TwitterCredentials.uScreenName = uScreenName;
	}

	public static String getOutputFile() {
		return outputFile;
	}

	public static void setOutputFile(String outputFile) {
		TwitterCredentials.outputFile = outputFile;
	}

	private TwitterCredentials() {
		throw new RuntimeException(
				"Instentiating TwiiterCredentials class not allowed.");
	}

	public static String getConsumerKey() {
		return consumer_key;
	}

	public static void setConsumerKey(String consumer_key) {
		TwitterCredentials.consumer_key = consumer_key;
	}

	public static String getConsumerSecretKey() {
		return consumer_secret_key;
	}

	public static void setConsumerSecretKey(String consumer_secret_key) {
		TwitterCredentials.consumer_secret_key = consumer_secret_key;
	}

	public static String getUserAccessToken() {
		return user_access_token;
	}

	public static void setUserAccessToken(String user_access_token) {
		TwitterCredentials.user_access_token = user_access_token;
	}

	public static String getUserAccessSecretToken() {
		return user_access_secret_token;
	}

	public static void setUserAccessSecretToken(String user_access_secret_token) {
		TwitterCredentials.user_access_secret_token = user_access_secret_token;
	}

	public static String getKeyWords() {
		return keyWords;
	}

	public static void setKeyWords(String keyWords) {
		TwitterCredentials.keyWords = keyWords;
	}

	public static String getsID() {
		return sID;
	}

	public static void setsID(String sID) {
		TwitterCredentials.sID = sID;
	}

	public static String getInputFile() {
		return inputFile;
	}

	public static void setInputFile(String inputFile) {
		TwitterCredentials.inputFile = inputFile;
	}

	public static String gettTname() {
		return tTname;
	}

	public static void settTname(String tTname) {
		TwitterCredentials.tTname = tTname;
	}

	public static String gettSname() {
		return tSname;
	}

	public static void settSname(String tSname) {
		TwitterCredentials.tSname = tSname;
	}

	public static String getcSlug() {
		return cSlug;
	}

	public static void setcSlug(String cSlug) {
		TwitterCredentials.cSlug = cSlug;
	}

	public static String getLid() {
		return lid;
	}

	public static void setLid(String lid) {
		TwitterCredentials.lid = lid;
	}

	public static String getPid() {
		return pid;
	}

	public static void setPid(String pid) {
		TwitterCredentials.pid = pid;
	}

	public static String getPlaceName() {
		return placeName;
	}

	public static void setPlaceName(String placeName) {
		TwitterCredentials.placeName = placeName;
	}

	public static boolean getIsVerified() {
		return isVerified;
	}

	public static void setIsVerified(boolean isVerified) {
		TwitterCredentials.isVerified = isVerified;
	}

	public static String getWoeid() {
		return woeid;
	}

	public static void setWoeid(String woeid) {
		TwitterCredentials.woeid = woeid;
	}

}
