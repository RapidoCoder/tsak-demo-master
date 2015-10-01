package twitterswissarmyknife;

import java.io.IOException;

import twitter4j.TwitterException;

public class Main {
	
	public static void main(String[] args) {
		
		try {
			
			// create App instance
			AppLauncher appLauncher = new AppLauncher();
			// run App instance.
			appLauncher.initLaunchSequence(args);
			
		} catch (TwitterException e) {
			System.err.println(e.getMessage());
		} catch (IllegalStateException e) {
			System.err.println(e.getMessage());
		} catch (IOException e) {
			System.err.println(e.getMessage());
		}catch (Exception e) {
			System.err.println(e.getMessage());
		}
	}

}
