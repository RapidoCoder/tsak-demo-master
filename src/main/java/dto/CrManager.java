package dto;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.InputMismatchException;
import java.util.Properties;
import java.util.Scanner;

import jcommander.ArgsManager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import twitter4j.RateLimitStatus;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.User;
import twitterhandler.LimitsManager;
import twitterhandler.TwitterCredentials;
import dto.ControlHandlers.LimitCheckVector;
import dto.ControlHandlers.LimitsEndPointsVector;

//Custom Request Manager
public class CrManager {

	final String leftAlignFormat = "| %-20s | %-20s |%n";
	final Logger tsak_logger = LogManager.getLogger(CrManager.class);

	public CrManager() {

	}

	public boolean getCredentialsFromENV() {

		DisplayInfoMessage(
				"[INFO]: Reading Credentials from TSAK_CONF variable.", true);

		String env_var = System.getenv("TSAK_CONF");

		if (env_var == null || env_var.isEmpty()) {
			DisplayInfoMessage("[INFO]: Skipping Environment variable, TSAK_CONF not set.",true);
			
//			DisplayInfoMessage("[INFO]: Searching local directory for conf.properties file.",true);
//			
//			File propConfFile = new File("tsak.properties");
//
//			if (!propConfFile.exists() || !propConfFile.isFile()) {
//				DisplayErrorMessage("[ERROR]: Couldn't read tsak.properties File.", true);
//				return false;
//			} 
			
			return false;
		} else {

			if (env_var.substring(env_var.length() - 1).equals("/")
					|| env_var.substring(env_var.length() - 1).equals("\\")) {
				env_var = env_var.substring(0, env_var.length() - 1);
			}

			File propConfFile = new File(env_var + File.separator
					+ "tsak.properties");

			if (!propConfFile.exists() || !propConfFile.isFile()) {
				DisplayErrorMessage(
						"[ERROR]: Couldn't read tsak.properties File.", true);
				return false;
			} else {
				Properties prop = new Properties();

				try {
					InputStream propInstream = new FileInputStream(propConfFile);
					prop.load(propInstream);
					propInstream.close();

				} catch (IOException e) {
					DisplayInfoMessage(e.getMessage(), true);
					return false;
				}

				try {
					// save credentials locally
					TwitterCredentials.setConsumerKey(prop.getProperty(
							"consumerKey").trim());
					TwitterCredentials.setConsumerSecretKey(prop.getProperty(
							"consumerSecret").trim());
					TwitterCredentials.setUserAccessToken(prop.getProperty(
							"accessToken").trim());
					TwitterCredentials.setUserAccessSecretToken(prop
							.getProperty("accessSecret").trim());

					if (prop.containsKey("verified")
							&& prop.getProperty("verified") != null
							&& !prop.getProperty("verified").trim().isEmpty()) {

						TwitterCredentials.setIsVerified(Boolean
								.parseBoolean(prop.getProperty("verified")
										.trim()));
					}

				} catch (NullPointerException e) {

					DisplayErrorMessage(
							"[ERROR]: Error Reading all Credentials from TSAK_CONF variable.",
							true);
					return false;
				}
				if (ArgsManager.TwitterCredentialsAvailable()) {
					return true;
				} else {
					DisplayInfoMessage(
							"[INFO]: Skipping tsak.properties File.", true);
					return false;
				}
			}
		}

	}

	
//	private boolean readConfFile (Properties prop) {
//		
//	}
	public void writeVerificationToConfFile(boolean verification) {

		String env_var = System.getenv("TSAK_CONF");
		File propConfFile;
		
		if (env_var != null) {

			if (env_var.substring(env_var.length() - 1).equals("/")
					|| env_var.substring(env_var.length() - 1).equals("\\")) {
				env_var = env_var.substring(0, env_var.length() - 1);
			}

			propConfFile = new File(env_var + File.separator
					+ "tsak.properties");
		}else {
			propConfFile = new File("tsak.properties");
		}
		
		try {

			Properties prop = new Properties();
			OutputStream propOutstream = new FileOutputStream(propConfFile);

			prop.setProperty("consumerKey", TwitterCredentials.getConsumerKey());
			prop.setProperty("consumerSecret",
					TwitterCredentials.getConsumerSecretKey());
			prop.setProperty("accessToken",
					TwitterCredentials.getUserAccessToken());
			prop.setProperty("accessSecret",
					TwitterCredentials.getUserAccessSecretToken());
			prop.setProperty("verified", String.valueOf(verification));
			prop.store(propOutstream, null);
			propOutstream.close();

		} catch (IOException e) {
			DisplayErrorMessage("[ERROR]: " + e.getMessage(), true);
		}

	}

	public boolean isTwitterAccessable() {

		boolean available = false;
		try {
			final URL url = new URL("https://twitter.com");
			final URLConnection conn = url.openConnection();
			conn.connect();
			available = true;
		} catch (Exception e) {
			available = false;
		}
		return available;
	}

	public int readUserChoiceResponseNumber(Scanner s) {
		DisplayInfoMessage("\n1. Retry \t 2. Exit", true);

		try {
			int resp = Integer.parseInt(s.next());
			return resp;
		} catch (InputMismatchException e) {
			int resp = readUserChoiceResponseNumber(s);
			return resp;
		} catch (ClassCastException e) {
			int resp = readUserChoiceResponseNumber(s);
			return resp;
		} catch (NumberFormatException e) {
			int resp = readUserChoiceResponseNumber(s);
			return resp;
		}
	}

	public void printUserProfile(User user, boolean showBasic)
			throws TwitterException {

		RateLimitStatus status = user.getRateLimitStatus();

		Integer Limit = status.getLimit();
		Integer RemainingLimits = status.getRemaining();
		String uName = user.getName();
		Long uID = user.getId();
		String uScreenName = user.getScreenName();
		Integer followerCount = user.getFollowersCount();
		Integer friendsCount = user.getFriendsCount();

		DisplayInfoMessage("Requester Information:", false);
		printHeaders();
		System.out.format(leftAlignFormat, "Name", uName);
		System.out.format(leftAlignFormat, "ID", uID.toString());
		System.out.format(leftAlignFormat, "Screen Name", uScreenName);
		System.out.format(leftAlignFormat, "Follower Count",
				followerCount.toString());
		System.out.format(leftAlignFormat, "Friend Count",
				friendsCount.toString());
		System.out.format(leftAlignFormat, "Total Limits", Limit.toString());
		System.out.format(leftAlignFormat, "Remaining Limit",
				RemainingLimits.toString());
		printFooter();
	}

	public int[] rateLimitAnalyzer(Twitter twitter, LimitsManager lManager,
			LimitsEndPointsVector epv) {

		int[] tmp_vect = new int[2];

		tmp_vect[0] = lManager.getRemainingLimitSpecific(twitter, epv,
				LimitCheckVector.LIMIT_REMAINING);
		tmp_vect[1] = lManager.getRemainingLimitSpecific(twitter, epv,
				LimitCheckVector.TIME_SECONDS_UNTIL_RESET);

		return tmp_vect;

	}

	private void printHeaders() {
		System.out
				.format("\n+----------------------+----------------------+%n");
		System.out.printf("| Property             | Value                |%n");
		System.out.format("+----------------------+----------------------+%n");

	}

	private void printFooter() {
		System.out.format("+----------------------+----------------------+%n");
	}

	public void DisplayInfoMessage(String msg, boolean newLine) {
		if (newLine) {
			tsak_logger.info(msg + "\n");
		} else {
			tsak_logger.info(msg);
		}
	}

	public void DisplayErrorMessage(String msg, boolean newLine) {
		if (newLine) {
			tsak_logger.error(msg + "\n");
		} else {
			tsak_logger.error(msg);
		}
	}

	@Override
	protected void finalize() throws Throwable {
		super.finalize();
	}
}
