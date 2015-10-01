package twitterhandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import twitter4j.AccountSettings;
import twitter4j.IDs;
import twitter4j.Location;
import twitter4j.PagableResponseList;
import twitter4j.Paging;
import twitter4j.ResponseList;
import twitter4j.SavedSearch;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.User;
import twitter4j.UserList;
import dto.ControlHandlers.LimitCheckVector;
import dto.ControlHandlers.LimitsEndPointsVector;
import dto.CrManager;
import dto.DDManager;
import dto.TsakException;

public class AccountManager {

	Twitter twitter;
	DDManager ddManager;
	CrManager cRManager;
	LimitsManager lManager;
	Long timeStart= null, timeEnd= null;
	PagableResponseList<UserList> lists;
	IDs ids;
	PagableResponseList<User> users;
	AccountSettings settings;
	ResponseList<SavedSearch> savedSearches;
	List<Status> statuses;
	List<User> userslist;
		
	List<Map<String, Object>> maplists_return;
	List <Object> objlist_return;
	Map<String, Object> mapObj_return;
	Paging page;
	public AccountManager (Twitter twtr, DDManager ddm, CrManager crm, LimitsManager lmg,Paging paging) {
		
		ddManager = ddm;
		cRManager = crm;
		lManager = lmg;
		twitter = twtr;
		maplists_return = new ArrayList<Map<String, Object>>();
		objlist_return = new ArrayList<Object> ();
		mapObj_return = new HashMap<String, Object>();
		page = paging;
	}
	

	public List<Map<String, Object>> userListMemberships () throws TsakException {
		
		cRManager.DisplayInfoMessage("[INFO]: Checking Rate Limits Availibity.", true);

		int lmts[] = cRManager.rateLimitAnalyzer(twitter, lManager,
				LimitsEndPointsVector.LISTS_MEMBERSHIPS);

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

			cRManager.DisplayInfoMessage("[INFO]: Getting lists memberships...", true);
			
			timeStart = System.currentTimeMillis();
			
			long cursor = -1;
			
			
			do {
				
				
				try {

					lists = twitter.getUserListMemberships(Long.parseLong(TwitterCredentials.getuID()),cursor);
				} catch (ClassCastException e) {
					
					lists = twitter.getUserListMemberships(TwitterCredentials.getuScreenName(), cursor);

				} catch (NumberFormatException e) {
					
					lists = twitter.getUserListMemberships(TwitterCredentials.getuScreenName(), cursor);
				}
				
				for (UserList ulist : lists) {
					
					Map<String, Object> lmap = new HashMap<String, Object>();
					lmap.put("id", ulist.getId());
					lmap.put("slug", ulist.getSlug());
					lmap.put("name", ulist.getName());
					lmap.put("members_count", ulist.getMemberCount());
					lmap.put("subscribers_count", ulist.getSubscriberCount());
					lmap.put("description", ulist.getDescription());
					
					lmap.put("uri", ulist.getURI());
					
					maplists_return.add(lmap);
					
					//ddManager.writeLine((new JSONObject(lmap)).toString() , true);
				}
				
				cursor = lists.getNextCursor();
				
			} while (lists.size() > 0 && cursor < 14);
			
			timeEnd = System.currentTimeMillis();
			cRManager.DisplayInfoMessage(
					"[INFO]: "+TwitterCredentials.getuScreenName()+TwitterCredentials.getuID()+"'s lists memberships dumped successfully to "
							+ TwitterCredentials.getOutputFile() + " in "
							+ String.valueOf((timeEnd - timeStart) / 1000F)
							+ " seconds.", true);
			
		} catch (TwitterException te) {
			cRManager.DisplayErrorMessage("[ERROR]: " + te.getMessage(), true);
		}
		
		return maplists_return;
		
	}
	
	
	public List<Object> getMutesIds() throws TsakException {

		cRManager.DisplayInfoMessage("[INFO]: Checking Rate Limits Availibity.", true);

		int lmts[] = cRManager.rateLimitAnalyzer(twitter, lManager,
				LimitsEndPointsVector.MUTES_USERS_IDS);

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

			cRManager.DisplayInfoMessage("[INFO]: Getting muted IDs...", true);
			
			timeStart = System.currentTimeMillis();
			
			long cursor = -1;
			do {
				 ids = twitter.getMutesIDs(cursor);
				 cursor = ids.getNextCursor();
				 
					for (long id : ids.getIDs()) {
						
						objlist_return.add(String.valueOf(id));
						//ddManager.writeLine(String.valueOf(id), true);
					}
				 
			}while (ids.getIDs().length > 0 && cursor < 15);

			timeEnd = System.currentTimeMillis();
			cRManager.DisplayInfoMessage(
					"[INFO]: Authenticated user's muted IDs dumped successfully to "
							+ TwitterCredentials.getOutputFile() + " in "
							+ String.valueOf((timeEnd - timeStart) / 1000F)
							+ " seconds.", true);
			
		} catch (TwitterException te) {
			cRManager.DisplayErrorMessage("[ERROR]: " + te.getMessage(), true);
		} 
		
		return objlist_return;
	}
	
	
	public List<Map<String, Object>> getMutesLists() throws TsakException {
		
		cRManager.DisplayInfoMessage("[INFO]: Checking Rate Limits Availibity.", true);

		int lmts[] = cRManager.rateLimitAnalyzer(twitter, lManager,
				LimitsEndPointsVector.MUTES_USERS_LIST);

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

			cRManager.DisplayInfoMessage("[INFO]: Getting muted users List...", true);
			
			timeStart = System.currentTimeMillis();
			
			long cursor = -1;
			
			do {
				
				users = twitter.getMutesList(cursor);
				
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

					maplists_return.add(user);
					
//					JSONObject json_user = new JSONObject(user);
//					ddManager.writeLine(json_user.toString(), true);
				}
				
				cursor = users.getNextCursor();
				 
			}while (users.size() > 0 && cursor < 14);

			timeEnd = System.currentTimeMillis();
			cRManager.DisplayInfoMessage(
					"[INFO]: Authenticated user's muted users List dumped successfully to "
							+ TwitterCredentials.getOutputFile() + " in "
							+ String.valueOf((timeEnd - timeStart) / 1000F)
							+ " seconds.", true);
			
		} catch (TwitterException te) {
			cRManager.DisplayErrorMessage("[ERROR]: " + te.getMessage(), true);
		} 
		
		return maplists_return;
	}
	
	public Map<String, Object> getAccountSettingsJson() throws TsakException {

		

		cRManager.DisplayInfoMessage("[INFO]: Checking Rate Limits Availibity.", true);

		int lmts[] = cRManager.rateLimitAnalyzer(twitter, lManager,
				LimitsEndPointsVector.ACCOUNT_SETTINGS);

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

			cRManager.DisplayInfoMessage("[INFO]: Getting account settings....", true);
			
			timeStart = System.currentTimeMillis();
			
			settings = twitter.getAccountSettings();

			mapObj_return.put("screen_name", settings.getScreenName());
			mapObj_return.put("sleep_time_enabled",
					settings.isSleepTimeEnabled());
			mapObj_return
					.put("sleep_end_enabled", settings.getSleepEndTime());
			mapObj_return.put("sleep_start_enabled",
					settings.getSleepStartTime());
			mapObj_return.put("geo_enabled", settings.isGeoEnabled());
			mapObj_return.put("email_discoverable",
					settings.isDiscoverableByEmail());
			mapObj_return.put("uses_https_always",
					settings.isAlwaysUseHttps());
			mapObj_return.put("access_level", settings.getAccessLevel());
			mapObj_return.put("language", settings.getLanguage());
			mapObj_return.put("time_zone", settings.getTimeZone());

			Location[] locations = settings.getTrendLocations();
			List<String> loc = new ArrayList<String>();
			loc = null;
			
			
			for (Location location : locations) {
				
				loc.add(location.getName());
			}
			
			
			mapObj_return.put("locations", loc);

			//json_settings = new JSONObject(mapObj_return);

			//ddManager.writeLine(json_settings.toString(), true);

			timeEnd = System.currentTimeMillis();
			cRManager.DisplayInfoMessage(
					"[INFO]: Account settings dumped successfully to "
							+ TwitterCredentials.getOutputFile() + " in "
							+ String.valueOf((timeEnd - timeStart) / 1000F)
							+ " seconds.", true);
			
		} catch (TwitterException te) {
			cRManager.DisplayErrorMessage("[ERROR]: " + te.getMessage(), true);
		} 
		
		return mapObj_return;
	}
	
	public List<Object> SavedSearches() throws TsakException {
		
		cRManager.DisplayInfoMessage("[INFO]: Checking Rate Limits Availibity.", true);
		
		int lmts[] = cRManager.rateLimitAnalyzer(twitter, lManager,
				LimitsEndPointsVector.SAVED_SEARCHES_LIST);

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
			
			cRManager.DisplayInfoMessage("[INFO]: Getting saved searches.....", true);
			
			timeStart = System.currentTimeMillis();
			
			savedSearches = twitter.getSavedSearches();

			for (SavedSearch savedSearch : savedSearches) {
				StringBuilder sb = new StringBuilder();
				sb.append("{");
				sb.append("\"id\":\"" + savedSearch.getId() + "\",");
				sb.append("\"name\":\"" + savedSearch.getName() + "\",");
				sb.append("\"query\":\"" + savedSearch.getQuery() + "\",");
				sb.append("\"position\":" + savedSearch.getPosition());
				sb.append("}");

				objlist_return.add(sb.toString());
				//ddManager.writeLine(sb.toString(), true);
				
			}

			timeEnd = System.currentTimeMillis();
			cRManager.DisplayInfoMessage(
					"[INFO]: Saved searches dumped Successfully to "
							+ TwitterCredentials.getOutputFile() + " in "
							+ String.valueOf((timeEnd - timeStart) / 1000F)
							+ " seconds.", true);
			
		} catch (TwitterException te) {
			cRManager.DisplayErrorMessage("[ERROR]: " + te.getMessage(), true);
		} 
		return objlist_return;
	}
	
	public List<Map<String, Object>> getFavourities() throws TsakException  {


		cRManager.DisplayInfoMessage(
				"[INFO]: Checking Rate Limits Availibity.", true);

		int lmts[] = cRManager.rateLimitAnalyzer(twitter, lManager,
				LimitsEndPointsVector.FAVORITES_LIST);

		int availableCalls = lmts[0];
		int availableTime = lmts[1];

		cRManager.DisplayInfoMessage("[INFO]: " + availableCalls
				+ " Calls Available in " + availableTime
				+ " Seconds. i.e. max " + availableCalls * 200
				+ " favourites can be dumped.", true);
		
		try {
			if (availableCalls == 0) {
				throw new TsakException(
						"[ERROE]: Twitter Rate Limit Exceeded. Please try again after "
								+ availableTime + " Seconds.");
			}

			cRManager.DisplayInfoMessage("[INFO]: Getting favourites list.....", true);
			
			timeStart = System.currentTimeMillis();


			
			do {
				
				statuses = twitter.getFavorites(page);
				
				for (Status status : statuses) {
					
					Map<String, Object> fav = new HashMap<String, Object>();
					User user = status.getUser();
					fav.put("name", user.getName());
					fav.put("screen_name", status.getUser().getScreenName());
					fav.put("tweet", status.getText());
					
					maplists_return.add(fav);
					//ddManager.writeLine((new JSONObject(fav)).toString(), true);
				}

				page.setPage(page.getPage() + 1);
			} while (statuses.size() > 0 && page.getPage() < 14);
              
			timeEnd = System.currentTimeMillis();
			cRManager.DisplayInfoMessage(
					"[INFO]: Favourites dumped successfully to "
							+ TwitterCredentials.getOutputFile() + " in "
							+ String.valueOf((timeEnd - timeStart) / 1000F)
							+ " seconds.", true);

		} catch (TwitterException te) {

			cRManager.DisplayInfoMessage("[ERROR]: " + te.getMessage(), true);
		}
		
		return maplists_return;
	}
	
	public List<Object> getBlockList() throws TsakException {

		cRManager.DisplayInfoMessage(
				"[INFO]: Checking Rate Limits Availibity.", true);

		int availableCalls = lManager.getRemainingLimitSpecific(twitter,
				LimitsEndPointsVector.BLOCKS_LIST,
				LimitCheckVector.LIMIT_REMAINING);
		int availableTime = lManager.getRemainingLimitSpecific(twitter,
				LimitsEndPointsVector.BLOCKS_LIST,
				LimitCheckVector.TIME_SECONDS_UNTIL_RESET);

		cRManager.DisplayInfoMessage("[INFO]: " + availableCalls
				+ " Calls Available in " + availableTime + " seconds.", true);

		cRManager.DisplayInfoMessage(
				"[INFO]: Getting Blocks List.....", true);
		
		try {

			if (availableCalls == 0) {
				throw new TsakException(
						"[ERROE]: Twitter Rate Limit Exceeded. Please try again after "
								+ availableTime + " Seconds.");
			}

			timeStart = System.currentTimeMillis();

			userslist = twitter.getBlocksList();
			for (User user : userslist) {
				
				objlist_return.add(user.getId() + "\t@" + user.getScreenName());
				//ddManager.writeLine(user.getId() + "\t@" + user.getScreenName(), true);
			}
			
			timeEnd = System.currentTimeMillis();
			
			cRManager.DisplayInfoMessage(
					"[INFO]: Blocks list dumped successfully to "
							+ TwitterCredentials.getOutputFile() + " in "
							+ String.valueOf((timeEnd - timeStart) / 1000F)
							+ " seconds.", true);

		} catch (TwitterException te) {
			cRManager.DisplayErrorMessage("[ERROR]: " + te.getMessage(), true);
		}
		
		return objlist_return;
	}
}
