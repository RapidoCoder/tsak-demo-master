package dto;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import twitter4j.ResponseList;
import twitter4j.User;
import twitterhandler.TwitterCredentials;
import au.com.bytecode.opencsv.CSVWriter;

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
	
	public void userIteratorWriter(ResponseList<User> users) throws IOException{
		
		CSVWriter csvWriter = new CSVWriter(new FileWriter(new File(
				TwitterCredentials.getOutputFile())));

		csvWriter.writeNext(new String[] { "ID", "Screen Name", "Name",
				"Follower Count", "Friends Count" });
		
		for (User user : users) {

			Long id = user.getId();
			Integer flwrzcount = user.getFollowersCount();
			Integer frndscount = user.getFriendsCount();

			String[] row = { id.toString(),
					user.getScreenName(), user.getName(),
					flwrzcount.toString(), frndscount.toString() };

			csvWriter.writeNext(row);
		}
		
		csvWriter.close();
		
	}
	
	public void listMapsJsonWriter (List<Map<String, Object>> listmaps) {
		
		for (Map<String, Object> map : listmaps) {
			
			writeLine((new JSONObject(map)).toString() , true);
		}	
	}
	
	public void listObjectWriter (List<? extends Object> lists) {
		
		for (Object obj : lists) {
			
			writeLine(obj.toString() , true);
		}	
	}
	
	public void mapJsonWriter (Map<String, Object> map) {
			
			writeLine((new JSONObject(map)).toString() , true);	
	}

}
