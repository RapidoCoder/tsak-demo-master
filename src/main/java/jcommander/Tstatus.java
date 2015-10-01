package jcommander;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;

@Parameters
public class Tstatus extends FDbase {
	
	@Parameter(names = "-sid", description = "Status ID.")
	private String sID = "";

	public String getsID() {
		return sID;
	}

	public void setuID(String sID) {
		this.sID = sID;
	}

}
