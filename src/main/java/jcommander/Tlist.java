package jcommander;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;

@Parameters
public class Tlist extends FDbase{

	@Parameter(names = "-lid", description = "List ID.")
	private String lId = "";
	
	public String getlId() {
		return lId;
	}

	public void setlId(String lId) {
		this.lId = lId;
	}
}
