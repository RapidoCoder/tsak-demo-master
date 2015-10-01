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

import twitter4j.Friendship;
import twitter4j.IDs;
import twitter4j.PagableResponseList;
import twitter4j.Relationship;
import twitter4j.ResponseList;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.User;
import dto.ControlHandlers.LimitsEndPointsVector;
import dto.ControlHandlers.subCmdUpVector;
import dto.CrManager;
import dto.DDManager;
import dto.TsakException;

public class FriendshipManager {
	Twitter twitter;
	DDManager ddManager;
	CrManager cRManager;
	LimitsManager lManager;
	Long timeStart, timeEnd;

	IDs ids;
	List<Long> idsList;
	List<String> screenNames;
	ResponseList<Friendship> friendships;
	BufferedReader bReader;
	Map<String, Object> obj;
	Map<String, Object> tar;
	JSONObject jObj;
	List<String> ffReturn;
	Relationship relationship;
	Map<String, Object> rel;
	PagableResponseList<User> users;

	public FriendshipManager(Twitter twtr, DDManager ddm, CrManager crm,
			LimitsManager lmg) {

		ddManager = ddm;
		cRManager = crm;
		lManager = lmg;
		twitter = twtr;
		idsList = new ArrayList<Long>();
		ffReturn = new ArrayList<String>();
	}

	public List<Long> iOFriendships(subCmdUpVector sbv) throws TsakException {

		cRManager.DisplayInfoMessage(
				"[INFO]: Checking Rate Limits Availibity.", true);

		int availableCalls = 0;
		int availableTime = 0;

		if (sbv == subCmdUpVector.INCOMING_FRNDSHIP) {

			int lmts[] = cRManager.rateLimitAnalyzer(twitter, lManager,
					LimitsEndPointsVector.FRIENDSHIPS_INCOMING);
			availableCalls = lmts[0];
			availableTime = lmts[1];
		} else if (sbv == subCmdUpVector.OUTGOING_FRNDSHIP) {

			int lmts[] = cRManager.rateLimitAnalyzer(twitter, lManager,
					LimitsEndPointsVector.FRIENDSHIPS_OUTGOING);
			availableCalls = lmts[0];
			availableTime = lmts[1];
		}

		cRManager.DisplayInfoMessage("[INFO]: " + availableCalls
				+ " Calls Available in " + availableTime + " Seconds.", true);

		try {

			if (availableCalls == 0) {
				throw new TsakException(
						"[ERROE]: Twitter Rate Limit Exceeded. Please try again after "
								+ availableTime + " Seconds.");
			}

			cRManager.DisplayInfoMessage(
					"[INFO]: Getting friendship links....", true);

			timeStart = System.currentTimeMillis();
			long cursor = -1;

			do {

				if (sbv == subCmdUpVector.INCOMING_FRNDSHIP) {

					ids = twitter.getIncomingFriendships(cursor);

				} else if (sbv == subCmdUpVector.OUTGOING_FRNDSHIP) {
					ids = twitter.getOutgoingFriendships(cursor);
				}
				for (Long id : ids.getIDs()) {

					idsList.add(id);
					// ddManager.writeLine(String.valueOf(id) ,true);
				}

				cursor = ids.getNextCursor();

			} while (ids.getIDs().length > 0);

			timeEnd = System.currentTimeMillis();
			cRManager.DisplayInfoMessage(
					"[INFO]: Friendships links dumped successfully to "
							+ TwitterCredentials.getOutputFile() + " in "
							+ String.valueOf((timeEnd - timeStart) / 1000F)
							+ " seconds.", true);

		} catch (TwitterException e) {

			cRManager.DisplayInfoMessage("[ERROR]: " + e.getMessage(), true);
		} catch (ClassCastException e) {

			cRManager.DisplayInfoMessage("[ERROR]: " + e.getMessage(), true);
		}

		return idsList;
	}

	public List<String> lookupFriendship() throws IOException, TsakException {

		cRManager.DisplayInfoMessage(
				"[INFO]: Checking Rate Limits Availibity.", true);

		int lmts[] = cRManager.rateLimitAnalyzer(twitter, lManager,
				LimitsEndPointsVector.FRIENDSHIPS_LOOKUP);

		int availableCalls = lmts[0];
		int availableTime = lmts[1];

		cRManager.DisplayInfoMessage("[INFO]: " + availableCalls
				+ " Calls Available in " + availableTime + " Seconds. i.e. "
				+ availableCalls * 100 + " users can be Looked up.", true);

		try {

			if (availableCalls == 0) {
				throw new TsakException(
						"[ERROE]: Twitter Rate Limit Exceeded. Please try again after "
								+ availableTime + " Seconds.");
			}

			timeStart = System.currentTimeMillis();

			bReader = new BufferedReader(new FileReader(new File(
					TwitterCredentials.getInputFile())));

			String line;
			screenNames = new ArrayList<String>();
			// List<Long> ids = new ArrayList<Long>();

			while ((line = bReader.readLine()) != null) {

				if (!line.isEmpty()) {
					try {

						Long id = Long.parseLong(line);
						idsList.add(id);

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

					friendships = twitter.lookupFriendships(stringArray);

					// dump now
					for (Friendship friendship : friendships) {

						obj = new HashMap<String, Object>();
						tar = new HashMap<String, Object>();

						obj.put("source", twitter.getScreenName());

						tar.put("id", friendship.getId());
						tar.put("name", friendship.getName());
						tar.put("screenName", friendship.getScreenName());
						tar.put("following", friendship.isFollowing());
						tar.put("followedBy", friendship.isFollowedBy());

						obj.put("target", tar);

						jObj = new JSONObject(obj);

						// ddManager.writeLine(jObj.toString(), true);
						ffReturn.add(jObj.toString());
					}
				}

				// processing remaining less than 100
				int c = counter;
				page = new String[(screenNames.size() - c)];
				for (int k = 0; k < (screenNames.size() - c); k++) {

					page[k] = screenNames.get(counter++);
				}

				String[] stringArray = Arrays.copyOf(screenNames.toArray(),
						screenNames.size(), String[].class);

				friendships = twitter.lookupFriendships(stringArray);

				// dump now
				for (Friendship friendship : friendships) {

					obj = new HashMap<String, Object>();
					tar = new HashMap<String, Object>();

					obj.put("source", twitter.getScreenName());

					tar.put("id", friendship.getId());
					tar.put("name", friendship.getName());
					tar.put("screenName", friendship.getScreenName());
					tar.put("following", friendship.isFollowing());
					tar.put("followedBy", friendship.isFollowedBy());

					obj.put("target", tar);

					jObj = new JSONObject(obj);

					// ddManager.writeLine(jObj.toString(), true);
					ffReturn.add(jObj.toString());

				}

			}

			if (idsList.size() != 0) {

				Long[] idsLong = Arrays.copyOf(idsList.toArray(),
						idsList.size(), Long[].class);

				// Long[] idsLong = ((Long[]) ids.toArray());

				int mod = idsLong.length / 100;
				int counter = 0;
				Long[] page = new Long[100];
				for (int i = 0; i < mod; i++) {

					for (int j = 0; j < 100; j++) {
						page[j] = idsLong[counter++];
					}

					friendships = twitter.lookupFriendships(ArrayUtils
							.toPrimitive(page));

					// dump now
					for (Friendship friendship : friendships) {

						obj = new HashMap<String, Object>();
						tar = new HashMap<String, Object>();

						obj.put("source", twitter.getScreenName());

						tar.put("id", friendship.getId());
						tar.put("name", friendship.getName());
						tar.put("screenName", friendship.getScreenName());
						tar.put("following", friendship.isFollowing());
						tar.put("followedBy", friendship.isFollowedBy());

						obj.put("target", tar);

						jObj = new JSONObject(obj);

						// ddManager.writeLine(jObj.toString(), true);
						ffReturn.add(jObj.toString());
					}

				}

				int c = counter;
				page = new Long[(idsLong.length - c)];
				for (int k = 0; k < (idsLong.length - c); k++) {

					page[k] = idsLong[counter++];
				}

				friendships = twitter.lookupFriendships(ArrayUtils
						.toPrimitive(page));

				// dump now
				for (Friendship friendship : friendships) {

					obj = new HashMap<String, Object>();
					tar = new HashMap<String, Object>();

					obj.put("source", twitter.getScreenName());

					tar.put("id", friendship.getId());
					tar.put("name", friendship.getName());
					tar.put("screenName", friendship.getScreenName());
					tar.put("following", friendship.isFollowing());
					tar.put("followedBy", friendship.isFollowedBy());

					obj.put("target", tar);

					jObj = new JSONObject(obj);

					// ddManager.writeLine(jObj.toString(), true);
					ffReturn.add(jObj.toString());
				}

			}

			timeEnd = System.currentTimeMillis();
			cRManager.DisplayInfoMessage(
					"[INFO]: Dumped Data Successfully to "
							+ TwitterCredentials.getOutputFile() + " in "
							+ String.valueOf((timeEnd - timeStart) / 1000F)
							+ " seconds.", true);

		} catch (FileNotFoundException e) {

			cRManager.DisplayInfoMessage("[ERROR]: " + e.getMessage(), true);
		} catch (TwitterException e) {

			cRManager.DisplayInfoMessage("[ERROR]: " + e.getMessage(), true);
		} catch (ClassCastException e) {

			cRManager.DisplayInfoMessage("[ERROR]: " + e.getMessage(), true);
		} finally {
			bReader.close();
		}

		return ffReturn;
	}

	public String showFriendsShip() throws FileNotFoundException, TsakException {

		Long timeStart = null, timeEnd = null;

		cRManager.DisplayInfoMessage(
				"[INFO]: Checking Rate Limits Availibity.", true);

		int lmts[] = cRManager.rateLimitAnalyzer(twitter, lManager,
				LimitsEndPointsVector.FRIENDSHIPS_SHOW);

		int availableCalls = lmts[0];
		int availableTime = lmts[1];

		cRManager.DisplayInfoMessage("[INFO]: " + availableCalls
				+ " Calls Available in " + availableTime + " Seconds.", true);

		try {

			if (availableCalls == 0) {
				throw new TsakException(
						"[ERROE]: Twitter Rate Limit Exceeded. Please try again after "
								+ availableTime + " Seconds.");
			}

			cRManager.DisplayInfoMessage(
					"[INFO]: Looking up Friendship link....", true);

			timeStart = System.currentTimeMillis();

			Long sid = null;
			Long tid = null;

			try {

				sid = Long.parseLong(TwitterCredentials.gettSname());
				tid = Long.parseLong(TwitterCredentials.gettTname());
			} catch (ClassCastException e) {
			} catch (NumberFormatException e) {
			}

			if (sid != null && tid != null) {
				relationship = twitter.showFriendship(sid, tid);
			} else if (TwitterCredentials.gettSname() != null
					&& TwitterCredentials.gettTname() != null) {
				relationship = twitter.showFriendship(
						TwitterCredentials.gettSname(),
						TwitterCredentials.gettTname());
			} else {
				throw new TsakException(
						"[ERROE]: Please Provide either both Screen Name or both IDs for source and target users at a time.");
			}

			rel = new HashMap<String, Object>();

			rel.put("source", relationship.getSourceUserScreenName());
			rel.put("target", relationship.getTargetUserScreenName());

			rel.put("source_blocking_target",
					relationship.isSourceBlockingTarget());
			rel.put("source_followed_by_target",
					relationship.isSourceFollowedByTarget());
			rel.put("source_following_target",
					relationship.isSourceFollowingTarget());
			rel.put("source_muting_target", relationship.isSourceMutingTarget());
			rel.put("source_notification_enabled",
					relationship.isSourceNotificationsEnabled());
			rel.put("canSourceDm", relationship.canSourceDm());

			jObj = new JSONObject(rel);

			// ddManager.writeLine(jObj.toString(), true);

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

		return jObj.toString();
	}

	public List<String> getFriendsList(subCmdUpVector sbv) throws TsakException {

		Long timeStart = null, timeEnd = null;

		cRManager.DisplayInfoMessage(
				"[INFO]: Checking Rate Limits Availibity.", true);

		int availableCalls = 0;
		int availableTime = 0;

		if (sbv == subCmdUpVector.FRIENDS_LIST) {
			int lmts[] = cRManager.rateLimitAnalyzer(twitter, lManager,
					LimitsEndPointsVector.FRIENDS_LIST);
			availableCalls = lmts[0];
			availableTime = lmts[1];
		} else if (sbv == subCmdUpVector.FOLLOWERS_LIST) {
			int lmts[] = cRManager.rateLimitAnalyzer(twitter, lManager,
					LimitsEndPointsVector.FOLLOWERS_LIST);
			availableCalls = lmts[0];
			availableTime = lmts[1];
		}

		cRManager.DisplayInfoMessage("[INFO]: " + availableCalls
				+ " Calls Available in " + availableTime + " seconds.", true);

		try {

			if (availableCalls == 0) {
				throw new TsakException(
						"[ERROE]: Twitter Rate Limit Exceeded. Please try again after "
								+ availableTime + " Seconds.");
			}

			cRManager.DisplayInfoMessage("[INFO]: Getting List....", true);

			timeStart = System.currentTimeMillis();

			long cursor = -1;

			do {

				try {

					if (sbv == subCmdUpVector.FRIENDS_LIST) {

						users = twitter.getFriendsList(
								Long.parseLong(TwitterCredentials.getuID()),
								cursor);
					} else if (sbv == subCmdUpVector.FOLLOWERS_LIST) {
						users = twitter.getFollowersList(
								Long.parseLong(TwitterCredentials.getuID()),
								cursor);
					}
				} catch (ClassCastException e) {

					if (sbv == subCmdUpVector.FRIENDS_LIST) {
						users = twitter.getFriendsList(
								TwitterCredentials.getuScreenName(), cursor);
					} else if (sbv == subCmdUpVector.FOLLOWERS_LIST) {
						users = twitter.getFollowersList(
								TwitterCredentials.getuScreenName(), cursor);
					}

				} catch (NumberFormatException e) {

					if (sbv == subCmdUpVector.FRIENDS_LIST) {
						users = twitter.getFriendsList(
								TwitterCredentials.getuScreenName(), cursor);
					} else if (sbv == subCmdUpVector.FOLLOWERS_LIST) {
						users = twitter.getFollowersList(
								TwitterCredentials.getuScreenName(), cursor);
					}
				}
				for (int i = 0; i < users.size(); i++) {
					

					Map<String, Object> user = new HashMap<String, Object>();
                   
					user.put("screen_name", users.get(i).getScreenName());
					user.put("name", users.get(i).getName());
					user.put("id", users.get(i).getId());
					user.put("profile_image", users.get(i)
							.getBiggerProfileImageURL());
					user.put("friends_count", users.get(i).getFriendsCount());
					user.put("followers_count", users.get(i)
							.getFollowersCount());
					user.put("location", users.get(i).getLocation());
					user.put("language", users.get(i).getLang());

					jObj = new JSONObject(user);

					ffReturn.add(jObj.toString());
					// ddManager.writeLine(json_user.toString(), true);
				}
			} while ((cursor = users.getNextCursor()) != 0);

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

		return ffReturn;
	}

}
