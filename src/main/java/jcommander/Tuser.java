package jcommander;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;

@Parameters
public class Tuser extends FDbase{

	@Parameter(names = "-uid", description = "Twitter User ID.")
	private String uID = "";
	
	@Parameter(names = "-uname", description = "Target user Screen Name")
	private String uScreenName = "";
	
	public String getuScreenName() {
		return uScreenName;
	}

	public void setuScreenName(String uScreenName) {
		this.uScreenName = uScreenName;
	}

	public String getuID() {
		return uID;
	}

	public void setuID(String uID) {
		this.uID = uID;
	}

}
