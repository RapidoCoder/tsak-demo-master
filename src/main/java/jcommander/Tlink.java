package jcommander;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;

@Parameters
public class Tlink extends FDbase{

	@Parameter(names = "-suser", description = "Source user ScreenName/ID.")
	private String tSname = "";

	@Parameter(names = "-tuser", description = "Target user ScreenName/ID.")
	private String tTname = "";
	
	public String gettSname() {
		return tSname;
	}

	public void settSname(String tSname) {
		this.tSname = tSname;
	}

	public String gettTname() {
		return tTname;
	}

	public void settTname(String tTname) {
		this.tTname = tTname;
	}
	
}
