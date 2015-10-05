package twitterhandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.User;
import dto.ControlHandlers.LimitsEndPointsVector;
import dto.CrManager;
import dto.DDManager;
import dto.TsakException;

public class SearchManager {

	Twitter twitter;
	DDManager ddManager;
	CrManager cRManager;
	LimitsManager lManager;
	Long timeStart= null, timeEnd= null;
	private ResponseList<User> users;
	private Map<String, Object> user;
	private JSONObject jObj;
	
	private List <String> smReturn;
	private QueryResult results;
	private Query query;
	private List<Status> tweets;
	private Map<String,String> userInfo;
	private Map<String, Object> tweetMap;
	
	public SearchManager(Twitter twtr, DDManager ddm, CrManager crm, LimitsManager lmg) {
		
		ddManager = ddm;
		cRManager = crm;
		lManager = lmg;
		twitter = twtr;
		smReturn = new ArrayList<String>();
	}
	
	
	public List<String> searchUsers(String keywords) throws TsakException {

		cRManager.DisplayInfoMessage(
				"[INFO]: Checking Rate Limits Availibity.", true);

		int lmts[] = cRManager.rateLimitAnalyzer(twitter, lManager,
				LimitsEndPointsVector.USERS_SEARCH);

		int availableCalls = lmts[0];
		int availableTime = lmts[1];

		cRManager.DisplayInfoMessage("[INFO]: " + availableCalls
				+ " Calls Available in " + availableTime
				+ " Seconds. but only 50 can be used at a time.", true);

		try {

			if (availableCalls == 0) {
				throw new TsakException(
						"[ERROE]: Twitter Rate Limit Exceeded. Please try again after "
								+ availableTime + " Seconds.");
			}

			cRManager.DisplayInfoMessage("[INFO]: Searching Users....", true);
			
			timeStart = System.currentTimeMillis();
			int page = -1;
			do {
				users = twitter.searchUsers(keywords,page);
				for (User tuser : users) {
					
					user = new HashMap<String, Object>();

					user.put("screen_name", tuser.getScreenName());
					user.put("name", tuser.getName());
					user.put("id", tuser.getId());
					user.put("profile_image", tuser.getBiggerProfileImageURL());
					user.put("friends_count", tuser.getFriendsCount());
					user.put("followers_count", tuser.getFollowersCount());
					user.put("location", tuser.getLocation());
					user.put("language", tuser.getLang());

					jObj = new JSONObject(user);
					smReturn.add(jObj.toString());
					//ddManager.writeLine(jObj.toString(), true);
				}

				page++;
			} while (users.size() != 0 && page < 50);

			timeEnd = System.currentTimeMillis();
			cRManager.DisplayInfoMessage(
					"[INFO]: Dumped Data Successfully to "
							+ TwitterCredentials.getOutputFile() + " in "
							+ String.valueOf((timeEnd - timeStart) / 1000F)
							+ " seconds.", true);
			
		} catch (TwitterException te) {
			cRManager.DisplayInfoMessage("[ERROR]: " + te.getMessage(), true);
		} finally {
		}
		
		return smReturn;
	}
	
	
	public List<String> searchTweets(String keyWords) throws TsakException {

		int numDumpTweets = 0;
		
		cRManager.DisplayInfoMessage(
				"[INFO]: Checking Rate Limits Availibity.", true);
		
		int lmts[] = cRManager.rateLimitAnalyzer(twitter, lManager,LimitsEndPointsVector.SEARCH_TWEETS);

		int availableCalls = lmts[0];
		int availableTime = lmts[1];
		
		cRManager.DisplayInfoMessage("[INFO]: " + availableCalls
				+ " Calls Available in " + availableTime
				+ " seconds.", true);

		try {

			if (availableCalls == 0) {
				throw new TsakException("[ERROE]: Twitter Rate Limit Exceeded. Please try again after " +availableTime+ " Seconds.");
			}
			
			query = new Query(keyWords);

			cRManager.DisplayInfoMessage("[INFO]: Searching Tweets...", true);
			//start timer to count time
			timeStart = System.currentTimeMillis();
			
			do {
				results = twitter.search(query);

				tweets = results.getTweets();
				
				for (Status tweet : tweets) {
					
					numDumpTweets ++;
					
					userInfo = new HashMap<String,String>();
					userInfo.put("Name", tweet.getUser().getName());
					userInfo.put("Screen_name", tweet.getUser().getScreenName());
					userInfo.put("ID", String.valueOf(tweet.getUser().getId()));
					userInfo.put("Location", tweet.getUser().getLocation());
					userInfo.put("imageURL", tweet.getUser().getOriginalProfileImageURL());
					userInfo.put("URL", tweet.getUser().getURL());
					userInfo.put("FriendsCount", String.valueOf(tweet.getUser().getFriendsCount()));
					userInfo.put("FriendsCount", String.valueOf(tweet.getUser().getFollowersCount()));
					
					tweetMap = new HashMap<String, Object>();
					tweetMap.put("user", userInfo);
					tweetMap.put("tweet", tweet.getText());

					
					jObj = new JSONObject(tweetMap);
					
					smReturn.add(jObj.toString());
					//write json to output file
					//ddManager.writeLine(json.toString(), true);	
				}
			} while ((query = results.nextQuery()) != null);
			
			//stop timer
			timeEnd = System.currentTimeMillis();
			//show message ==> numtweetsDump + TimeElapased
			cRManager.DisplayInfoMessage(
					"[INFO]: " +numDumpTweets+ " Tweets are dumped successfully in " + String.valueOf((timeEnd - timeStart)/1000F) + " seconds.", true);
		} catch (TwitterException te) {
			
			cRManager.DisplayInfoMessage("[INFO]: "+numDumpTweets+ " Tweets' dumped successfully in " + String.valueOf((timeEnd - timeStart)/1000F) + " seconds.", true);
			cRManager.DisplayErrorMessage("[ERROR]: " + te.getMessage(), true);
			
		}
		
		return smReturn;
	}
}
