package jcommander;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;

@Parameters
public class Tslug extends FDbase{

	@Parameter(names = "-slug", description = "Catagory slug.")
	private String cSlug = "";

	public String getcSlug() {
		return cSlug;
	}

	public void setcSlug(String cSlug) {
		this.cSlug = cSlug;
	}
}
