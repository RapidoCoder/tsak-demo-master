package jcommander;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;

@Parameters(separators = "=", commandDescription = "Twitter Swiss Army Knife Base Command")
public class Tsak {

    @Parameter(names = "-consumerKey", description = "Application consumer KEY.")
    private String consumerKey = "";

    @Parameter(names = "-consumerSecret", description = "Application consumer secret KEY.")
    private String consumerSecretKey = "";
    
    @Parameter(names = "-accessToken", description = "User Access Token.")
    private String accessKey = "";

    @Parameter(names = "-accessSecret", description = "User Access secret Token.")
    private String accessSecretKey = "";
       
    
    public String getAccessKey() {
		return accessKey;
	}

	public void setAccessKey(String accessKey) {
		this.accessKey = accessKey;
	}

	public String getAccessSecretKey() {
		return accessSecretKey;
	}

	public void setAccessSecretKey(String accessSecretKey) {
		this.accessSecretKey = accessSecretKey;
	}
  
	public String getConsumerKey() {
		return consumerKey;
	}

	public void setConsumerKey(String consumerKey) {
		this.consumerKey = consumerKey;
	}

	public String getConsumerSecretKey() {
		return consumerSecretKey;
	}

	public void setConsumerSecretKey(String consumerSecretKey) {
		this.consumerSecretKey = consumerSecretKey;
	}
	
}
