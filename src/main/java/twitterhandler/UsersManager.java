package twitterhandler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.ArrayUtils;
import org.json.JSONObject;

import twitter4j.Category;
import twitter4j.ResponseList;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.User;
import dto.ControlHandlers.LimitCheckVector;
import dto.ControlHandlers.LimitsEndPointsVector;
import dto.CrManager;
import dto.DDManager;
import dto.TsakException;

public class UsersManager {

	Twitter twitter;
	DDManager ddManager;
	CrManager cRManager;
	LimitsManager lManager;
	Long timeStart= null, timeEnd= null;
	private ResponseList<Category> categories;
	
	private List<String> umReturn;
	private ResponseList<User> users;
	private Map<String, Object> userMap;
	private JSONObject jObj;
	private BufferedReader bReader;
	private List<String> screenNames;
	private List<Long> ids;
	private ResponseList<User> usersByScreenName;
	private ResponseList<User> usersByIDs;
	
	public UsersManager(Twitter twtr, DDManager ddm, CrManager crm, LimitsManager lmg) {
		
		ddManager = ddm;
		cRManager = crm;
		lManager = lmg;
		twitter = twtr;
		umReturn = new ArrayList<String>();
	}
	
	
	public List<String> getSuggestedCatagories() throws TsakException {

		long timeStart = 0, timeEnd = 0;
		cRManager.DisplayInfoMessage("[INFO]: Checking Rate Limits Availibity.", true);

		int lmts[] = cRManager.rateLimitAnalyzer(twitter, lManager,
				LimitsEndPointsVector.USERS_SUGGESTIONS);

		int availableCalls = lmts[0];
		int availableTime = lmts[1];

		cRManager.DisplayInfoMessage("[INFO]: " + availableCalls
				+ " Calls Available in " + availableTime + " seconds.", true);

		try {

			if (availableCalls == 0) {
				throw new TsakException(
						"[ERROE]: Twitter Rate Limit Exceeded. Please try again after "
								+ availableTime + " Seconds.");
			}

			cRManager.DisplayInfoMessage("[INFO]: Looking for suggested catagories.....", true);
			
			timeStart = System.currentTimeMillis();

			categories = twitter
					.getSuggestedUserCategories();

			for (Category category : categories) {
				
				umReturn.add(category.getName() + " \t " + category.getSlug()+ " \t " + category.getSize());
				//ddManager.writeLine(category.getName() + " \t " + category.getSlug()
				//		+ " \t " + category.getSize(), true);
			}

			timeEnd = System.currentTimeMillis();
			cRManager.DisplayInfoMessage(
					"[INFO]: Dumped Data Successfully to "
							+ TwitterCredentials.getOutputFile() + " in "
							+ String.valueOf((timeEnd - timeStart) / 1000F)
							+ " seconds.", true);

		} catch (TwitterException te) {

			cRManager.DisplayInfoMessage("[ERROR]: " + te.getMessage(),true);
		}
		
		return umReturn;
	}

	public List<String> getUserSuggestions(String slug) throws TsakException {

		long timeStart = 0, timeEnd = 0;
		cRManager.DisplayInfoMessage("[INFO]: Checking Rate Limits Availibity.", true);

		int lmts[] = cRManager.rateLimitAnalyzer(twitter, lManager,
				LimitsEndPointsVector.USERS_SUGGESTIONS_SLUG);

		int availableCalls = lmts[0];
		int availableTime = lmts[1];

		cRManager.DisplayInfoMessage("[INFO]: " + availableCalls
				+ " Calls Available in " + availableTime + " seconds.", true);

		try {

			if (availableCalls == 0) {
				throw new TsakException(
						"[ERROE]: Twitter Rate Limit Exceeded. Please try again after "
								+ availableTime + " Seconds.");
			}

			cRManager.DisplayInfoMessage("[INFO]: Accessing users in given category.....", true);

			timeStart = System.currentTimeMillis();

			users = twitter.getUserSuggestions(slug);
			for (User user : users) {

				userMap = new HashMap<String, Object>();
				userMap.put("screen_name", user.getScreenName());
				userMap.put("name", user.getName());
				userMap.put("id", user.getId());
				userMap.put("profile_image", user.getMiniProfileImageURL());
				userMap.put("friends_count", user.getFriendsCount());
				userMap.put("followers_count", user.getFollowersCount());
				userMap.put("location", user.getLocation());
				userMap.put("language", user.getLang());

				jObj = new JSONObject(userMap);

				umReturn.add(jObj.toString());
				//ddManager.writeLine(jObj.toString(),true);

			}

			timeEnd = System.currentTimeMillis();
			cRManager.DisplayInfoMessage(
					"[INFO]: Suggestions in given category dumped successfully to "
							+ TwitterCredentials.getOutputFile() + " in "
							+ String.valueOf((timeEnd - timeStart) / 1000F)
							+ " seconds.", true);

		} catch (TwitterException te) {

			cRManager.DisplayInfoMessage("[ERROR]: " + te.getMessage(), true);
		}

		return umReturn;
	}
	
	public List<String> getMemberSuggestions(String slug) throws TsakException  {

		long timeStart = 0, timeEnd = 0;
		cRManager.DisplayInfoMessage("[INFO]: Checking Rate Limits Availibity.", true);

		int lmts[] = cRManager.rateLimitAnalyzer(twitter, lManager,
				LimitsEndPointsVector.USERS_SUGGESTIONS_SLUG_MEMBERS);

		int availableCalls = lmts[0];
		int availableTime = lmts[1];

		cRManager.DisplayInfoMessage("[INFO]: " + availableCalls
				+ " Calls Available in " + availableTime + " seconds.", true);

		try {

			if (availableCalls == 0) {
				throw new TsakException(
						"[ERROE]: Twitter Rate Limit Exceeded. Please try again after "
								+ availableTime + " Seconds.");
			}

			cRManager.DisplayInfoMessage("[INFO]: Looking up Memebers suggestions....", true);
			
			timeStart = System.currentTimeMillis();

			users = twitter.getMemberSuggestions(slug);

			for (User user : users) {
				userMap = new HashMap<String, Object>();

				if (user.getStatus() != null) {
					userMap.put("status", user.getStatus().getText());
				} else {
					userMap.put("status", "Private User");
				}

				userMap.put("screen_name", user.getScreenName());
				userMap.put("name", user.getName());
				userMap.put("id", user.getId());
				userMap.put("profile_image", user.getMiniProfileImageURL());
				userMap.put("friends_count", user.getFriendsCount());
				userMap.put("followers_count", user.getFollowersCount());
				userMap.put("location", user.getLocation());
				userMap.put("language", user.getLang());

				jObj = new JSONObject(userMap);
				umReturn.add(jObj.toString());
				
				//ddManager.writeLine(jObj.toString(), true);
			}

			timeEnd = System.currentTimeMillis();
			cRManager.DisplayInfoMessage(
					"[INFO]: Members suggestions dumped successfully to "
							+ TwitterCredentials.getOutputFile() + " in "
							+ String.valueOf((timeEnd - timeStart) / 1000F)
							+ " seconds.", true);

		} catch (TwitterException te) {

			cRManager.DisplayInfoMessage("[ERROR]: " + te.getMessage(), true);
		}
		
		return umReturn;
	}

	
	public List<String> lookupUsers(String file) throws TsakException, IOException {

		cRManager.DisplayInfoMessage("[INFO]: Checking Rate Limits Availibity.", true);

		int availableCalls = lManager.getRemainingLimitSpecific(twitter,
				LimitsEndPointsVector.USERS_LOOKUP,
				LimitCheckVector.LIMIT_REMAINING);
		int availableTime = lManager.getRemainingLimitSpecific(twitter,
				LimitsEndPointsVector.USERS_LOOKUP,
				LimitCheckVector.TIME_SECONDS_UNTIL_RESET);

		cRManager.DisplayInfoMessage("[INFO]: " + availableCalls
				+ " Calls Available in " + availableTime + " seconds.", true);

		cRManager.DisplayInfoMessage("[INFO]: Looking up provided users....", true);
		
		try {

			if (availableCalls == 0) {
				throw new TsakException(
						"[ERROE]: Twitter Rate Limit Exceeded. Please try again after "
								+ availableTime + " Seconds.");
			}

			bReader = new BufferedReader(new FileReader(new File(file)));

			String line;
			screenNames = new ArrayList<String>();
			ids = new ArrayList<Long>();

			while ((line = bReader.readLine()) != null) {

				if (!line.isEmpty()) {
					try {

						Long id = Long.parseLong(line);
						ids.add(id);

					} catch (ClassCastException e) {
						screenNames.add(line);
					} catch (NumberFormatException e) {
						screenNames.add(line);
					}
				}

			}

			if (screenNames.size() != 0) {

				int mod = screenNames.size() / 100;
				int counter = 0;
				String[] page = new String[100];
				for (int i = 0; i < mod; i++) {

					for (int j = 0; j < 100; j++) {
						page[j] = screenNames.get(counter++);
					}

					String[] stringArray = Arrays.copyOf(screenNames.toArray(),
							screenNames.size(), String[].class);

					usersByScreenName = twitter
							.lookupUsers(stringArray);

					// dump now
					for (User user : usersByScreenName) {

						if (user.getStatus() != null) {
							
							umReturn.add("@" + user.getScreenName() + " - "+ user.getStatus().getText());
//							ddManager.writeLine("@" + user.getScreenName() + " - "
//									+ user.getStatus().getText(), true);

						} else {
							umReturn.add("@" + user.getScreenName()+ " status is Private.");
//							ddManager.writeLine("@" + user.getScreenName()
//									+ " status is Private.", true);
						}
					}

				}
				int c = counter;
				page = new String[(screenNames.size() - c)];
				for (int k = 0; k < (screenNames.size() - c); k++) {

					page[k] = screenNames.get(counter++);
				}

				String[] stringArray = Arrays.copyOf(screenNames.toArray(),
						screenNames.size(), String[].class);

				usersByScreenName = twitter
						.lookupUsers(stringArray);

				// dump to file
				for (User user : usersByScreenName) {

					if (user.getStatus() != null) {
						umReturn.add("@" + user.getScreenName() + " - "+ user.getStatus().getText());

//						ddManager.writeLine("@" + user.getScreenName() + " - "
//								+ user.getStatus().getText(), true);
					} else {
						
						umReturn.add("@" + user.getScreenName()+ " status is Private.");
						
//						ddManager.writeLine("@" + user.getScreenName()
//								+ " status is Private.", true);
					}
				}

			}

			if (ids.size() != 0) {

				Long[] idsLong = Arrays.copyOf(ids.toArray(), ids.size(),
						Long[].class);

				// Long[] idsLong = ((Long[]) ids.toArray());

				int mod = idsLong.length / 100;
				int counter = 0;
				Long[] page = new Long[100];
				for (int i = 0; i < mod; i++) {

					for (int j = 0; j < 100; j++) {
						page[j] = idsLong[counter++];
					}

					usersByIDs = twitter
							.lookupUsers(ArrayUtils.toPrimitive(page));

					// dump now
					for (User user : usersByIDs) {

						if (user.getStatus() != null) {
							umReturn.add("@" + user.getScreenName() + " - "+ user.getStatus().getText());

//							ddManager.writeLine("@" + user.getScreenName() + " - "
//									+ user.getStatus().getText(), true);
						} else {
							umReturn.add("@" + user.getScreenName()+ " status is Private.");
							
//							ddManager.writeLine("@" + user.getScreenName()
//									+ " status is Private.", true);
						}
					}

				}
				int c = counter;
				page = new Long[(idsLong.length - c)];
				for (int k = 0; k < (idsLong.length - c); k++) {

					page[k] = idsLong[counter++];
				}

				usersByIDs = twitter.lookupUsers(ArrayUtils
						.toPrimitive(page));

				// dump to file
				for (User user : usersByIDs) {

					if (user.getStatus() != null) {
						umReturn.add("@" + user.getScreenName() + " - "+ user.getStatus().getText());

//						ddManager.writeLine("@" + user.getScreenName() + " - "
//								+ user.getStatus().getText(), true);
					} else {
						umReturn.add("@" + user.getScreenName()+ " status is Private.");
//						ddManager.writeLine("@" + user.getScreenName()
//								+ " status is Private.", true);
					}
				}

			}
			
			cRManager.DisplayInfoMessage("[INFO]: Looked up users dumped successfully to "
					+ TwitterCredentials.getOutputFile(), true);

		} catch (FileNotFoundException e) {

			cRManager.DisplayInfoMessage("[ERROR]: " + e.getMessage(), true);
		} catch (ClassCastException e) {

			cRManager.DisplayInfoMessage("[ERROR]: " + e.getMessage(), true);
		} catch (Exception e) {

			cRManager.DisplayInfoMessage("[ERROR]: " + e.getMessage(), true);
		} finally {
			bReader.close();
		}
		
		return umReturn;
	}
	
	
}
