package dto;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;

import twitterhandler.TwitterCredentials;

//Data Dump Manager (DDManager)
public class DDManager {

	CrManager cRManager;
	PrintStream writer;

	public DDManager(CrManager crm) {
		
		cRManager = crm;
		writer = getWriter();
	}

	private PrintStream getWriter() {

		PrintStream writer = null;

		try {
			writer = new PrintStream(new FileOutputStream(new File(
					TwitterCredentials.getOutputFile()), true));
			
		} catch (FileNotFoundException e) {
			
		}
		
		return writer;
	}
	
	public void closeWriter () {
		
		writer.close();
	}
	

	public void writeLine (String line, boolean newline) {
		
		if (newline) {
			writer.println(line);
				
		}else {
			writer.print(line);
		}
		writer.flush();
	}

}
