package twitterhandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import twitter4j.PagableResponseList;
import twitter4j.Paging;
import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.User;
import twitter4j.UserList;
import dto.ControlHandlers.LimitsEndPointsVector;
import dto.ControlHandlers.subCmdUpVector;
import dto.CrManager;
import dto.DDManager;
import dto.TsakException;

public class ListsManager {
	Twitter twitter;
	DDManager ddManager;
	CrManager cRManager;
	LimitsManager lManager;
	Long timeStart = null, timeEnd = null;
	private Paging page;
	private ResponseList<Status> statuses ;
	private Map<String, Object> listStatus;
	
	private List<String> lReturn;
	private JSONObject jObj;
	private ResponseList<UserList> userlist;
	private PagableResponseList<User> users;
	private PagableResponseList<UserList> lists;

	public ListsManager(Twitter twtr, DDManager ddm, CrManager crm,
			LimitsManager lmg) {

		ddManager = ddm;
		cRManager = crm;
		lManager = lmg;
		twitter = twtr;
		lReturn = new ArrayList<String>();
	}

	public List<String> getListStatuses() throws TsakException {

		int counter = 0;

		cRManager.DisplayInfoMessage(
				"[INFO]: Checking Rate Limits Availibity.", true);

		int lmts[] = cRManager.rateLimitAnalyzer(twitter, lManager,
				LimitsEndPointsVector.LISTS_STATUSES);

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

			cRManager.DisplayInfoMessage("[INFO]: Getting list statuses....",
					true);

			timeStart = System.currentTimeMillis();

			page = new Paging(1, 50);
			
			do {
				
				statuses = twitter.getUserListStatuses(Long.parseLong(TwitterCredentials.getLid()), page);

				for (Status status : statuses) {
					
					listStatus = new HashMap<String, Object>();

					listStatus.put("createdAt", status.getCreatedAt());
					listStatus.put("id", status.getId());
					listStatus.put("text", status.getText());
					listStatus.put("retweetedStatus",
							status.getRetweetedStatus());
					listStatus.put("userMentionEntities",
							status.getUserMentionEntities());
					listStatus.put("hashtagEntities",
							status.getHashtagEntities());
					listStatus.put("mediaEntities", status.getMediaEntities());
					listStatus.put("user", status.getUser());
					listStatus.put("geoLocation", status.getGeoLocation());

					//ddManager.writeLine((new JSONObject(listStatus)).toString(), true);
					counter++;
					
					jObj = new JSONObject(listStatus);
					
						lReturn.add(jObj.toString());	
				}

				page.setPage(page.getPage() + 1);
			} while (statuses.size() > 0 && page.getPage() <= 179);

			timeEnd = System.currentTimeMillis();

			if (lReturn.isEmpty()) {
				return lReturn;
			}
			
			cRManager.DisplayInfoMessage(
					"[INFO]: " + counter
							+ " List status/statuses dumped successfully to "
							+ TwitterCredentials.getOutputFile() + " in "
							+ String.valueOf((timeEnd - timeStart) / 1000F)
							+ " seconds.", true);

		} catch (TwitterException te) {

			cRManager.DisplayInfoMessage(
					"[INFO]: " + counter
							+ " List status/statuses dumped successfully to "
							+ TwitterCredentials.getOutputFile() + " in "
							+ String.valueOf((timeEnd - timeStart) / 1000F)
							+ " seconds.", true);

			cRManager.DisplayInfoMessage("[ERROR]: " + te.getMessage(), true);
		} catch (NumberFormatException e) {

			cRManager.DisplayInfoMessage(
					"[ERROR]: Wrong list id." + e.getMessage(), true);
		} 
		
		return lReturn;
	}

	public List<String> getUserLists() throws TsakException {

		cRManager.DisplayInfoMessage(
				"[INFO]: Checking Rate Limits Availibity.", true);

		int lmts[] = cRManager.rateLimitAnalyzer(twitter, lManager,
				LimitsEndPointsVector.LISTS_LIST);

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

			cRManager
					.DisplayInfoMessage("[INFO]: Getting user lists....", true);

			timeStart = System.currentTimeMillis();

			try {

				userlist = twitter.getUserLists(Long
						.parseLong(TwitterCredentials.getuID()));

			} catch (ClassCastException e) {

				userlist = twitter.getUserLists(TwitterCredentials
						.getuScreenName());
			} catch (NumberFormatException e) {

				userlist = twitter.getUserLists(TwitterCredentials
						.getuScreenName());
			}

			for (UserList list : userlist) {

				Map<String, Object> listMap = new HashMap<String, Object>();

				listMap.put("name", list.getName());
				listMap.put("id", list.getId());
				listMap.put("description", list.getDescription());
				listMap.put("slug", list.getSlug());

				jObj = new JSONObject(listMap);
				//ddManager.writeLine(jObj.toString(), true);

				lReturn.add(jObj.toString());
			}

			timeEnd = System.currentTimeMillis();
			cRManager.DisplayInfoMessage(
					"[INFO]: User Lists dumped successfully to "
							+ TwitterCredentials.getOutputFile() + " in "
							+ String.valueOf((timeEnd - timeStart) / 1000F)
							+ " seconds.", true);

		} catch (TwitterException te) {

			cRManager.DisplayInfoMessage(("[ERROR]: " + te.getMessage()), true);
		}

		return lReturn;
	}

	public List<String> getUserListSubscribers(subCmdUpVector sbv) throws TsakException {

		cRManager.DisplayInfoMessage("[INFO]: Checking Rate Limits Availibity.", true);
		
		String memsub = "";
		
		int availableCalls = 0;
		int availableTime = 0;

		if (sbv == subCmdUpVector.LIST_SUBSCRIBERS) {
			int lmts[] = cRManager.rateLimitAnalyzer(twitter, lManager,
					LimitsEndPointsVector.LISTS_SUBSCRIBERS);

			availableCalls = lmts[0];
			availableTime = lmts[1];
			
			memsub = "subscribers";
			
		}else if (sbv == subCmdUpVector.LIST_MEMBERS) {
			
			int lmts[] = cRManager.rateLimitAnalyzer(twitter, lManager,
					LimitsEndPointsVector.LISTS_MEMBERS);

			availableCalls = lmts[0];
			availableTime = lmts[1];
			
			memsub = "members";
		}


		cRManager.DisplayInfoMessage("[INFO]: " + availableCalls
				+ " Calls Available in " + availableTime + " seconds.", true);

		try {

			if (availableCalls == 0) {
				throw new TsakException(
						"[ERROE]: Twitter Rate Limit Exceeded. Please try again after "
								+ availableTime + " Seconds.");
			}

			cRManager.DisplayInfoMessage("[INFO]: Getting "+ TwitterCredentials.getLid() + "'s "+memsub+"....", true);

			timeStart = System.currentTimeMillis();

			long cursor = -1;
			
			do {

				if (sbv == subCmdUpVector.LIST_SUBSCRIBERS) {
				users = twitter.getUserListSubscribers(Long.parseLong(TwitterCredentials.getLid()), cursor);
				
				}else if (sbv == subCmdUpVector.LIST_MEMBERS) {
					
					users = twitter.getUserListMembers(Long.parseLong(TwitterCredentials.getLid()), cursor);
				}
				cursor = users.getNextCursor();
				
				for (User tuser : users) {
					
					Map<String, Object> user = new HashMap<String, Object>();

					user.put("screen_name", tuser.getScreenName());
					user.put("name", tuser.getName());
					user.put("id", tuser.getId());
					user.put("profile_image", tuser.getBiggerProfileImageURL());
					user.put("friends_count", tuser.getFriendsCount());
					user.put("followers_count", tuser.getFollowersCount());
					user.put("location", tuser.getLocation());
					user.put("language", tuser.getLang());

					jObj = new JSONObject(user);
					//ddManager.writeLine(json_user.toString(), true);
					lReturn.add(jObj.toString());
					
				}

			} while (users.size() > 0 && cursor < 14);

			timeEnd = System.currentTimeMillis();
			cRManager.DisplayInfoMessage(
					"[INFO]: " + TwitterCredentials.getLid()
							+ " "+memsub+" dumped successfully to "
							+ TwitterCredentials.getOutputFile() + " in "
							+ String.valueOf((timeEnd - timeStart) / 1000F)
							+ " seconds.", true);

		} catch (TwitterException te) {

			cRManager.DisplayInfoMessage(("[ERROR]: " + te.getMessage()), true);
		}

		return lReturn;
	}
	
	public List<String> UserListSubsciptions ()throws TsakException {
			
			cRManager.DisplayInfoMessage("[INFO]: Checking Rate Limits Availibity.", true);

			int lmts[] = cRManager.rateLimitAnalyzer(twitter, lManager,
					LimitsEndPointsVector.LISTS_SUBSCRIPTIONS);
			
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

				cRManager.DisplayInfoMessage("[INFO]: Getting lists subscriptions...", true);
				
				timeStart = System.currentTimeMillis();
				
				long cursor = -1;
				
				do {
					
					lists = twitter.getUserListSubscriptions(TwitterCredentials.getuScreenName(), cursor);
					
					for (UserList list : lists) {
						
						Map<String, Object> lmap = new HashMap<String, Object>();
						lmap.put("id", list.getId());
						lmap.put("slug", list.getSlug());
						lmap.put("name", list.getName());
						lmap.put("members_count", list.getMemberCount());
						lmap.put("subscribers_count", list.getSubscriberCount());
						lmap.put("description", list.getDescription());
						lmap.put("uri", list.getURI());
						
						jObj = new JSONObject(lmap);
						lReturn.add(jObj.toString());

						//ddManager.writeLine((new JSONObject(lmap)).toString() , true);
					}
					
					cursor = lists.getNextCursor();
					
				} while (lists.size() > 0 && cursor < 14);
				
				timeEnd = System.currentTimeMillis();
				cRManager.DisplayInfoMessage(
						"[INFO]: "+TwitterCredentials.getuScreenName()+"'s lists subscriptions dumped successfully to "
								+ TwitterCredentials.getOutputFile() + " in "
								+ String.valueOf((timeEnd - timeStart) / 1000F)
								+ " seconds.", true);
				
			} catch (TwitterException te) {
				cRManager.DisplayErrorMessage("[ERROR]: " + te.getMessage(), true);
			}
			
		return lReturn;
	}

}
