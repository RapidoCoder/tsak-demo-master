package jcommander;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;

@Parameters
public class Ttrends extends FDbase {

	
	@Parameter(names = "-woeid", description = "where on earth ID.")
	private String woeid = "";

	public String getWoeid() {
		return woeid;
	}

	public void setWoeid(String woeid) {
		this.woeid = woeid;
	}
}
