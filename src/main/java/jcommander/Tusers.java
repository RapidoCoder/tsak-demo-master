package jcommander;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;

@Parameters
public class Tusers extends FDbase{

	@Parameter(names = "-i", description = "Input File Containing IDs/ScreenNames")
	private String inputFile = "";
	
	public String getInputFile() {
		return inputFile;
	}

	public void setInputFile(String inputFile) {
		this.inputFile = inputFile;
	}
	
}
