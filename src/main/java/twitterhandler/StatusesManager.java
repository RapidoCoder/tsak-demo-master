package twitterhandler;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import twitter4j.IDs;
import twitter4j.Paging;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import dto.ControlHandlers.LimitsEndPointsVector;
import dto.ControlHandlers.subCmdUpVector;
import dto.CrManager;
import dto.DDManager;
import dto.TsakException;

public class StatusesManager {

	Twitter twitter;
	DDManager ddManager;
	CrManager cRManager;
	LimitsManager lManager;
	private Status status;
	
	List<String> smReturn;
	private IDs ids;
	private Paging page;
	private List<Status> statuses;
	private Map<String, Object> mentionsTL;
	private Map<String, Object> tweet;
	private Map<String, Object> user;
	private JSONObject jObj;
	
	public StatusesManager(Twitter twtr, DDManager ddm, CrManager crm, LimitsManager lmg) {
		
		ddManager = ddm;
		cRManager = crm;
		lManager = lmg;
		twitter = twtr;
		smReturn = new ArrayList<String>();
	}
	
	public List<String> getStatusById (String sID) throws TsakException {
		
		cRManager.DisplayInfoMessage("[INFO]: Checking Rate Limits Availibity.", true);
		
		int lmts[] = cRManager.rateLimitAnalyzer(twitter, lManager,LimitsEndPointsVector.STATUSES_SHOW_ID);

		int availableCalls = lmts[0];
		int availableTime = lmts[1];
		
		cRManager.DisplayInfoMessage("[INFO]: " + availableCalls
				+ " Calls Available in " + availableTime
				+ " seconds.", true);
		
		cRManager.DisplayInfoMessage("[INFO]: Searching Status by ID.....", true);
		
		try {

			if (availableCalls == 0) {
				throw new TsakException(
						"[ERROE]: Twitter Rate Limit Exceeded. Please try again after "
								+ availableTime + " Seconds.");
			}
			
			Long status_id = Long.parseLong(sID);
			status = twitter.showStatus(status_id);
			
			
			smReturn.add(status.getId() + "\t"
			+ status.getUser().getScreenName() + "\t"
			+ status.getText());
					
//			ddManager.writeLine(status.getId() + "\t"
//					+ status.getUser().getScreenName() + "\t"
//					+ status.getText(), true);
			
			cRManager.DisplayInfoMessage("[INFO]: Status Dumped Successfully to "
					+ TwitterCredentials.getOutputFile(), true);
			
		} catch (TwitterException te) {
			
			cRManager.DisplayErrorMessage("[ERROR]: " + te.getMessage(),true);	
		}
		
		return smReturn;
	}
	
	public List<String> getStatusRetweeters (String sID) throws TsakException {
		
		cRManager.DisplayInfoMessage("[INFO]: Checking Rate Limits Availibity.", true);
		
		int lmts[] = cRManager.rateLimitAnalyzer(twitter, lManager,LimitsEndPointsVector.STATUSES_RETWEETERS_IDS);

		int availableCalls = lmts[0];
		int availableTime = lmts[1];
		
		cRManager.DisplayInfoMessage("[INFO]: " + availableCalls
				+ " Calls Available in " + availableTime
				+ " seconds.", true);
		
		cRManager.DisplayInfoMessage("[INFO]: Searching Status Retweeters IDs.....", true);
		
		int counter = 0;
		
		try {

			if (availableCalls == 0) {
				throw new TsakException(
						"[ERROE]: Twitter Rate Limit Exceeded. Please try again after "
								+ availableTime + " Seconds.");
			}
			
			Long status_id = Long.parseLong(sID);
			long pageCursor = -1;
			do {

				ids = twitter.getRetweeterIds(status_id, pageCursor);

				for (Long id : ids.getIDs()) {
					
					smReturn.add(String.valueOf(id));
					//ddManager.writeLine(String.valueOf(id) , true);
					counter ++;
					
				}

			} while ((pageCursor = ids.getNextCursor()) != 0);
			
			if (counter == 0 ) {
				cRManager.DisplayInfoMessage("[INFO]: No Retweeters found for status "+ TwitterCredentials.getsID(), true);
			}else  {
				
				cRManager.DisplayInfoMessage("[INFO]: "+counter+" Retweeters' IDs' Dumped Successfully to "
						+ TwitterCredentials.getOutputFile(), true);
			}
			
		} catch (TwitterException te) {
			
			cRManager.DisplayErrorMessage("[ERROR]: " + te.getMessage(),true);	
		}
		
		return smReturn;
	}
	
	
	public List<String> getMentionsTimeline () throws TsakException {
		
		cRManager.DisplayInfoMessage("[INFO]: Checking Rate Limits Availibity.", true);
		
		int lmts[] = cRManager.rateLimitAnalyzer(twitter, lManager,LimitsEndPointsVector.STATUSES_MENTIONS_TIMELINE);

		int availableCalls = lmts[0];
		int availableTime = lmts[1];
		
		cRManager.DisplayInfoMessage("[INFO]: " + availableCalls
				+ " Calls Available in " + availableTime
				+ " seconds.", true);
		
		cRManager.DisplayInfoMessage("[INFO]: Getting Mentions Timeline.....", true);
				
		try {

			if (availableCalls == 0) {
				throw new TsakException(
						"[ERROE]: Twitter Rate Limit Exceeded. Please try again after "
								+ availableTime + " Seconds.");
			}
			
			page = new Paging(1, 200);
			
			do {
				statuses = twitter.getMentionsTimeline(page);

				for (Status status : statuses) {

					mentionsTL = new HashMap<String, Object>();

					tweet = new HashMap<String, Object>();
					user = new HashMap<String, Object>();

					tweet.put("id", status.getId());
					tweet.put("text", status.getText());
					tweet.put("in_reply_to_screenname",
							status.getInReplyToScreenName());
					tweet.put("in_reply_to_status",
							status.getInReplyToStatusId());
					tweet.put("in_reply_to_user",
							status.getInReplyToUserId());

					user.put("screen_name", status.getUser()
							.getScreenName());
					user.put("name", status.getUser().getName());
					user.put("id", status.getUser().getId());
					user.put("profile_image", status.getUser()
							.getMiniProfileImageURL());
					user.put("friends_count", status.getUser()
							.getFriendsCount());
					user.put("followers_count", status.getUser()
							.getFollowersCount());
					user.put("location", status.getUser().getLocation());
					user.put("language", status.getUser().getLang());

					mentionsTL.put("user", user);
					mentionsTL.put("tweet", tweet);

					jObj = new JSONObject(mentionsTL);

					smReturn.add(jObj.toString());
					//ddManager.writeLine(jObj.toString(), true);
				}

				page.setPage(page.getPage() + 1);

			} while (statuses.size() > 0);
			
			cRManager.DisplayInfoMessage("[INFO]: Mentions Timeline Dumped Successfully to "
					+ TwitterCredentials.getOutputFile(), true);
			
		} catch (TwitterException te) {
			
			System.out.println("Failed to show status: " + te.getMessage());
			
		} catch (ClassCastException e) {
			
			cRManager.DisplayErrorMessage("[ERROR]: " + e.getMessage(), true);
			
		} catch (NumberFormatException e) {

			cRManager.DisplayErrorMessage("[ERROR]: " + e.getMessage(), true);
			
		}
		
		return smReturn;
	}
	
	public List<String> userTimeLine(String Tuser,subCmdUpVector sbv)
			throws FileNotFoundException, TsakException {

		int counter = 0;
		Long timeStart = null, timeEnd = null;
		String userScreenName = Tuser;

		int availableCalls = 0;
		int availableTime = 0;

		cRManager.DisplayInfoMessage("[INFO]: Checking Rate Limits Availibity.", true);
		

		if (sbv == subCmdUpVector.HOME_TIMELINE) {

			int lmts[] = cRManager.rateLimitAnalyzer(twitter, lManager,LimitsEndPointsVector.STATUSES_HOME_TIMELINE);

			availableCalls = lmts[0];
			availableTime = lmts[1];
			
		}else if (sbv == subCmdUpVector.USER_TIMELINE) {

			int lmts[] = cRManager.rateLimitAnalyzer(twitter, lManager,LimitsEndPointsVector.STATUSES_USER_TIMELINE);

			availableCalls = lmts[0];
			availableTime = lmts[1];
			
		}else if (sbv == subCmdUpVector.OWN_RETWEETS) {
			
			int lmts[] = cRManager.rateLimitAnalyzer(twitter, lManager,LimitsEndPointsVector.STATUSES_RETWEETS_OF_ME);

			availableCalls = lmts[0];
			availableTime = lmts[1];
		}
		
		cRManager.DisplayInfoMessage("[INFO]: " + availableCalls
				+ " Calls Available in " + availableTime
				+ " seconds.", true);
		
		cRManager.DisplayInfoMessage("[INFO]: Reading User Timeline...", true);
		
		try {
			
			if (availableCalls == 0) {
				throw new TsakException("[ERROE]: Twitter Rate Limit Exceeded. Please try again after " +availableTime+ " Seconds.");
			}
			// start timer to count time
			timeStart = System.currentTimeMillis();

			cRManager.DisplayInfoMessage(
					"[INFO]: Dumping Statuses to file...", true);

			int pageCounter = 1;

			if (sbv == subCmdUpVector.HOME_TIMELINE) {
				int maxTweetsRetrive = 200;
				
				page = new Paging(pageCounter, maxTweetsRetrive);
				do {
					
					statuses = twitter.getHomeTimeline(page);
					for (Status status : statuses) {

						smReturn.add(status.getId() +"\t"+ status.getText());
						//ddManager.writeLine(status.getId() +"\t"+ status.getText(), true);
						counter++;
					}
					pageCounter++;
					page.setPage(pageCounter);
				} while (statuses.size() > 0);
				
			}else if (sbv == subCmdUpVector.USER_TIMELINE) {
				int maxTweetsRetrive = 200;
				page = new Paging(pageCounter, maxTweetsRetrive);
				do {
					
					try {
						
						statuses = twitter.getUserTimeline(Long.parseLong(TwitterCredentials.getuID()), page);
						userScreenName = TwitterCredentials.getuID();
					} catch (ClassCastException e) {

						statuses = twitter.getUserTimeline(TwitterCredentials.getuScreenName(), page);
						userScreenName = TwitterCredentials.getuScreenName();
					} catch (NumberFormatException e) {
						
						statuses = twitter.getUserTimeline(TwitterCredentials.getuScreenName(), page);
						userScreenName = TwitterCredentials.getuScreenName();
					}

					for (Status status : statuses) {
						smReturn.add(status.getText());
						//ddManager.writeLine(status.getText(), true);
						counter++;
					}

					pageCounter++;
					page.setPage(pageCounter);
				} while (statuses.size() > 0);

			}else if (sbv == subCmdUpVector.OWN_RETWEETS) {
				
				int maxTweetsRetrive = 100;
				page = new Paging(pageCounter, maxTweetsRetrive);
				do {
					statuses = twitter.getRetweetsOfMe(page);
					userScreenName = Tuser;

					for (Status status : statuses) {
						
						smReturn.add(status.getText());
						//ddManager.writeLine(status.getText(), true);
						counter++;
						
					}

					pageCounter++;
					page.setPage(pageCounter);
				} while (statuses.size() > 0);
			}
			// stop timer
			timeEnd = System.currentTimeMillis();
			
			cRManager.DisplayInfoMessage(
					"[INFO]: " + counter
							+ " Status/Statuses are dumped successfully in "
							+ String.valueOf((timeEnd - timeStart) / 1000F)
							+ " Seconds from @" + userScreenName
							+ " Timeline.", true);

		} catch (TwitterException te) {

			cRManager.DisplayErrorMessage("[ERROR]: " + te.getMessage(),true);
			
		}finally {
		}
		return smReturn;
	}
	
	
}
