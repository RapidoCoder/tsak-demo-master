package jcommander;

import com.beust.jcommander.Parameter;

public class FDbase {
	
	@Parameter(names = "-o", description = "Output File.")
	private String outputFile = "";

	public String getOutputFile() {
		return outputFile;
	}

	public void setOutputFile(String outputFile) {
		this.outputFile = outputFile;
	}
	
	
}
