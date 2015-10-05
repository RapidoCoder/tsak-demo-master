package twitterswissarmyknife;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import jcommander.ArgsManager;
import twitter4j.ResponseList;
import twitter4j.TwitterException;
import twitter4j.User;
import twitterhandler.AccountManager;
import twitterhandler.FFManager;
import twitterhandler.FriendshipManager;
import twitterhandler.GisManager;
import twitterhandler.LimitsManager;
import twitterhandler.ListsManager;
import twitterhandler.SearchManager;
import twitterhandler.StatusesManager;
import twitterhandler.TwitterAuth;
import twitterhandler.TwitterCredentials;
import twitterhandler.UsersManager;
import dto.ControlHandlers.controlVectors;
import dto.ControlHandlers.subCmdUpVector;
import dto.CrManager;
import dto.DDManager;
import dto.TsakException;

public class AppLauncher {

	controlVectors cVectors;
	CrManager cRManager;
	ArgsManager argManager;
	DDManager ddManager;
	LimitsManager lManager;
	TwitterAuth tAuth;
	subCmdUpVector scObj;
	boolean isUserActive = true;
	boolean toBeVerifiedENV = false;
	boolean readFromENV = false;
	Scanner s;

	public AppLauncher() {

		cVectors = controlVectors.TSAK_GET_PARAMETERS_FROM_ENV_VAR;
		cRManager = new CrManager();
		argManager = new ArgsManager(cRManager);
		s = new Scanner(System.in);
		lManager = new LimitsManager();
	}

	public AppLauncher(controlVectors cVectors, CrManager cRManager,
			ArgsManager argManager, subCmdUpVector scObj, TwitterAuth tAuth,
			LimitsManager lManager, DDManager ddManager) {
		this.cVectors = cVectors;
		this.cRManager = cRManager;
		this.argManager = argManager;
		this.scObj = scObj;
		this.tAuth = tAuth;
		this.lManager = lManager;
		this.ddManager = ddManager;
	}

	public final void initLaunchSequence(String[] args) throws IOException,
			TwitterException, IllegalStateException {

		while (isUserActive) {
			switch (cVectors) {

			case TSAK_GET_PARAMETERS_FROM_ENV_VAR:

				readEnvVariable(args);
				ddManager = new DDManager(cRManager);

				break;
			case TSAK_TWITTER_ACCESS_VERIFICATION:

				cRManager.DisplayInfoMessage(
						"[INFO]: Verifying Twitter/Internet Access... ", false);

				if (!verifyTwitterAccess()) {
					cRManager.DisplayInfoMessage("NOT_ACCESSABLE", false);

					if (cRManager.readUserChoiceResponseNumber(s) == 2) {
						cVectors = controlVectors.TSAK_SYSTEM_EXIT_PHASE;
					} else {
						continue;
					}

				} else {
					cVectors = controlVectors.TSAK_VERIFY_USER_CREDENTIALS;
				}
				s.close();
				break;
			case TSAK_VERIFY_USER_CREDENTIALS:
				if (readFromENV) {
					if (toBeVerifiedENV && !TwitterCredentials.getIsVerified()) {

						if (verifyTwitterCredentials()) {
							cRManager.printUserProfile(tAuth.getUser(), true);
							cRManager.writeVerificationToConfFile(true);
							TwitterCredentials.setIsVerified(true);

						}
					} else {
						cVectors = controlVectors.TSAK_TWITTER_GET_DUMP;
					}
				} else {

					if (verifyTwitterCredentials()) {

						cRManager.printUserProfile(tAuth.getUser(), true);

						// cRManager.writeVerificationToConfFile(true);
					}
				}
				break;
			case TSAK_TWITTER_GET_DUMP:
				try {
					dumpToFile();
					cVectors = controlVectors.TSAK_SYSTEM_EXIT_PHASE;
				} catch (Exception e) {
					cRManager.DisplayErrorMessage(e.getMessage(), true);
					System.exit(-1);
				}
				break;

			case TSAK_SYSTEM_EXIT_PHASE:
				ddManager.closeWriter();
				exitApp();
				break;

			default:
				ddManager.closeWriter();
				System.exit(-1);
				break;
			}

		}
	}

	private void dumpToFile() throws IOException, IllegalStateException,
			TsakException, TwitterException {

		if (scObj == subCmdUpVector.FLWRZ_DUMP_BY_ID) {

			FFManager fmanager = new FFManager(tAuth.getTwitterInstance(),
					ddManager, cRManager, lManager);
			ResponseList<User> flwrz = fmanager.dumpFFToFile(
					TwitterCredentials.getuID(), scObj);

			ddManager.userIteratorWriter(flwrz);

		} else if (scObj == subCmdUpVector.FLWRZ_DUMP_BY_SCREEN_NAME) {

			FFManager fmanager = new FFManager(tAuth.getTwitterInstance(),
					ddManager, cRManager, lManager);
			ResponseList<User> flwrz = fmanager.dumpFFToFile(
					TwitterCredentials.getuScreenName(), scObj);

			ddManager.userIteratorWriter(flwrz);

		} else if (scObj == subCmdUpVector.FRNDZ_DUMP_BY_ID) {

			FFManager fmanager = new FFManager(tAuth.getTwitterInstance(),
					ddManager, cRManager, lManager);
			ResponseList<User> flwings = fmanager.dumpFFToFile(
					TwitterCredentials.getuID(), scObj);

			ddManager.userIteratorWriter(flwings);

		} else if (scObj == subCmdUpVector.FRNDZ_DUMP_BY_SCREEN_NAME) {

			FFManager fmanager = new FFManager(tAuth.getTwitterInstance(),
					ddManager, cRManager, lManager);
			ResponseList<User> flwings = fmanager.dumpFFToFile(
					TwitterCredentials.getuScreenName(), scObj);

			ddManager.userIteratorWriter(flwings);

		} else if (scObj == subCmdUpVector.HOME_TIMELINE) {

			StatusesManager stmanager = new StatusesManager(
					tAuth.getTwitterInstance(), ddManager, cRManager, lManager);
			List<String> utimeline = stmanager.userTimeLine(tAuth.getTwitterInstance().getScreenName(),
					scObj);
			ddManager.listObjectWriter(utimeline);

		} else if (scObj == subCmdUpVector.USER_TIMELINE) {
			StatusesManager stmanager = new StatusesManager(
					tAuth.getTwitterInstance(), ddManager, cRManager, lManager);
			List<String> utimeline = stmanager.userTimeLine(null, scObj);
			ddManager.listObjectWriter(utimeline);
			
		} else if (scObj == subCmdUpVector.OWN_RETWEETS) {
			
			StatusesManager stmanager = new StatusesManager(
					tAuth.getTwitterInstance(), ddManager, cRManager, lManager);
			List<String> ownretweeters = stmanager.userTimeLine(tAuth.getTwitterInstance().getScreenName(),scObj);
			ddManager.listObjectWriter(ownretweeters);
			
		} else if (scObj == subCmdUpVector.GET_STATUS_BY_ID) {
			StatusesManager stmanager = new StatusesManager(
					tAuth.getTwitterInstance(), ddManager, cRManager, lManager);
			List<String> status = stmanager.getStatusById(TwitterCredentials.getsID());
			ddManager.listObjectWriter(status);
			
		} else if (scObj == subCmdUpVector.GET_STATUS_RETWEETERS) {
			StatusesManager stmanager = new StatusesManager(
					tAuth.getTwitterInstance(), ddManager, cRManager, lManager);
			List<String> retweeters = stmanager.getStatusRetweeters(TwitterCredentials.getsID());
			ddManager.listObjectWriter(retweeters);
			
		} else if (scObj == subCmdUpVector.MENTIONS_TIMELINE) {
			StatusesManager stmanager = new StatusesManager(
					tAuth.getTwitterInstance(), ddManager, cRManager, lManager);
			List<String> mentiontl = stmanager.getMentionsTimeline();
			ddManager.listObjectWriter(mentiontl);
			
		} else if (scObj == subCmdUpVector.SEARCH_TWEETS) {
			
			SearchManager smanager = new SearchManager(tAuth.getTwitterInstance(), ddManager, cRManager, lManager);
			List<String> tweets = smanager.searchTweets(TwitterCredentials.getKeyWords());
			ddManager.listObjectWriter(tweets);
			
		} else if (scObj == subCmdUpVector.SEARCH_USER) {
			SearchManager smanager = new SearchManager(
					tAuth.getTwitterInstance(), ddManager, cRManager, lManager);
			List<String> users = smanager.searchUsers(TwitterCredentials.getKeyWords());
			ddManager.listObjectWriter(users);

		} else if (scObj == subCmdUpVector.USERS_LOOKUP) {

			UsersManager umanager = new UsersManager(
					tAuth.getTwitterInstance(), ddManager, cRManager, lManager);
			List <String> users = umanager.lookupUsers(TwitterCredentials.getInputFile());
			ddManager.listObjectWriter(users);
			
		} else if (scObj == subCmdUpVector.BLOCK_LIST) {

			AccountManager amanager = new AccountManager(
					tAuth.getTwitterInstance(), ddManager, cRManager, lManager);
			List<Object> blocklst = amanager.getBlockList();

			ddManager.listObjectWriter(blocklst);
		} else if (scObj == subCmdUpVector.SHOW_FRNDSHIP) {

			FriendshipManager fmanager = new FriendshipManager(
					tAuth.getTwitterInstance(), ddManager, cRManager, lManager);
			String frndship = fmanager.showFriendsShip();

			ddManager.writeLine(frndship, true);

		} else if (scObj == subCmdUpVector.FRIENDS_LIST) {
			FriendshipManager fmanager = new FriendshipManager(
					tAuth.getTwitterInstance(), ddManager, cRManager, lManager);
			List<String> frndslist = fmanager.getFriendsList(scObj);
			ddManager.listObjectWriter(frndslist);

		} else if (scObj == subCmdUpVector.FOLLOWERS_LIST) {

			FriendshipManager fmanager = new FriendshipManager(
					tAuth.getTwitterInstance(), ddManager, cRManager, lManager);
			fmanager.getFriendsList(scObj);

		} else if (scObj == subCmdUpVector.FAVOURITES) {

			AccountManager amanager = new AccountManager(
					tAuth.getTwitterInstance(), ddManager, cRManager, lManager);
			List<Map<String, Object>> fav = amanager.getFavourities();

			ddManager.listMapsJsonWriter(fav);
		} else if (scObj == subCmdUpVector.SUGGESTED_USER_CATS) {

			UsersManager umanager = new UsersManager(
					tAuth.getTwitterInstance(), ddManager, cRManager, lManager);
			List<String> sgstedCats = umanager.getSuggestedCatagories();
			ddManager.listObjectWriter(sgstedCats);
			
		} else if (scObj == subCmdUpVector.SUGGESTION_SLUG) {

			UsersManager umanager = new UsersManager(
					tAuth.getTwitterInstance(), ddManager, cRManager, lManager);
			List<String> usuggstions = umanager.getUserSuggestions(TwitterCredentials.getcSlug());
			ddManager.listObjectWriter(usuggstions);
			
		} else if (scObj == subCmdUpVector.MEMBER_SUGGESTION) {

			UsersManager umanager = new UsersManager(
					tAuth.getTwitterInstance(), ddManager, cRManager, lManager);
			List<String> msuggstions = umanager.getMemberSuggestions(TwitterCredentials.getcSlug());
			ddManager.listObjectWriter(msuggstions);
			
		} else if (scObj == subCmdUpVector.USER_LISTS) {

			ListsManager lmanager = new ListsManager(
					tAuth.getTwitterInstance(), ddManager, cRManager, lManager);
			List<String> ulists = lmanager.getUserLists();
			ddManager.listObjectWriter(ulists);

		} else if (scObj == subCmdUpVector.LIST_STATUSES) {

			ListsManager lmanager = new ListsManager(
					tAuth.getTwitterInstance(), ddManager, cRManager, lManager);
			List<String> lstatuses = lmanager.getListStatuses();
			ddManager.listObjectWriter(lstatuses);

		} else if (scObj == subCmdUpVector.SAVED_SEARCHES) {

			AccountManager amanager = new AccountManager(
					tAuth.getTwitterInstance(), ddManager, cRManager, lManager);
			List<Object> savedsearches = amanager.SavedSearches();

			ddManager.listObjectWriter(savedsearches);

		} else if (scObj == subCmdUpVector.LOOKUP_FRNDSHIP) {

			FriendshipManager fmanager = new FriendshipManager(
					tAuth.getTwitterInstance(), ddManager, cRManager, lManager);
			List<String> lookupfrndshp = fmanager.lookupFriendship();
			ddManager.listObjectWriter(lookupfrndshp);

		} else if (scObj == subCmdUpVector.INCOMING_FRNDSHIP) {
			FriendshipManager fmanager = new FriendshipManager(
					tAuth.getTwitterInstance(), ddManager, cRManager, lManager);
			List<Long> iofrndshp = fmanager.iOFriendships(scObj);
			ddManager.listObjectWriter(iofrndshp);

		} else if (scObj == subCmdUpVector.OUTGOING_FRNDSHIP) {
			FriendshipManager fmanager = new FriendshipManager(
					tAuth.getTwitterInstance(), ddManager, cRManager, lManager);
			List<Long> iofrndshp = fmanager.iOFriendships(scObj);
			ddManager.listObjectWriter(iofrndshp);

		} else if (scObj == subCmdUpVector.GEO_DETAILS) {

			GisManager gmanager = new GisManager(tAuth.getTwitterInstance(),
					ddManager, cRManager, lManager);
			String ginfo = gmanager.getGeoLocationInfo();
			ddManager.writeLine(ginfo, true);

		} else if (scObj == subCmdUpVector.SIMILAR_PLACES) {

			GisManager gmanager = new GisManager(tAuth.getTwitterInstance(),
					ddManager, cRManager, lManager);
			List<String> gsplaces = gmanager.getSimilarPlaces();
			ddManager.listObjectWriter(gsplaces);

		} else if (scObj == subCmdUpVector.SEARCH_PLACE_BY_CORD) {

			GisManager gmanager = new GisManager(tAuth.getTwitterInstance(),
					ddManager, cRManager, lManager);
			List<String> geosrchd = gmanager.searchPlace();
			ddManager.listObjectWriter(geosrchd);

		} else if (scObj == subCmdUpVector.ACCOUNT_SETTINGS) {

			AccountManager amanager = new AccountManager(
					tAuth.getTwitterInstance(), ddManager, cRManager, lManager);
			Map<String, Object> settings = amanager.getAccountSettingsJson();

			ddManager.mapJsonWriter(settings);

		} else if (scObj == subCmdUpVector.AVAILABLE_TRENDS) {

			GisManager gmanager = new GisManager(tAuth.getTwitterInstance(),
					ddManager, cRManager, lManager);
			List<String> trends = gmanager.getAvalilableTrends();
			ddManager.listObjectWriter(trends);

		} else if (scObj == subCmdUpVector.PLACE_TRENDS) {

			GisManager gmanager = new GisManager(tAuth.getTwitterInstance(),
					ddManager, cRManager, lManager);
			List<String> ptrends = gmanager.getPlaceTrends();
			ddManager.listObjectWriter(ptrends);

		} else if (scObj == subCmdUpVector.CLOSEST_TRENDS) {

			GisManager gmanager = new GisManager(tAuth.getTwitterInstance(),
					ddManager, cRManager, lManager);
			List<String> gCtrends = gmanager.getClosestTrends();
			ddManager.listObjectWriter(gCtrends);

		} else if (scObj == subCmdUpVector.MUTES_IDS) {

			AccountManager amanager = new AccountManager(
					tAuth.getTwitterInstance(), ddManager, cRManager, lManager);
			List<Object> ids = amanager.getMutesIds();

			ddManager.listObjectWriter(ids);

		} else if (scObj == subCmdUpVector.MUTES_LIST) {

			AccountManager amanager = new AccountManager(
					tAuth.getTwitterInstance(), ddManager, cRManager, lManager);
			List<Map<String, Object>> umutelists = amanager.getMutesLists();

			ddManager.listMapsJsonWriter(umutelists);

		} else if (scObj == subCmdUpVector.LISTS_MEMEBERSHIPS) {

			AccountManager amanager = new AccountManager(
					tAuth.getTwitterInstance(), ddManager, cRManager, lManager);
			List<Map<String, Object>> ulistmaps = amanager
					.userListMemberships();

			ddManager.listMapsJsonWriter(ulistmaps);

		} else if (scObj == subCmdUpVector.LIST_SUBSCRIBERS
				|| scObj == subCmdUpVector.LIST_MEMBERS) {

			ListsManager lmanager = new ListsManager(
					tAuth.getTwitterInstance(), ddManager, cRManager, lManager);
			List<String> ulsub = lmanager.getUserListSubscribers(scObj);
			ddManager.listObjectWriter(ulsub);

		} else if (scObj == subCmdUpVector.LIST_SUBSCRIPTIONS) {

			ListsManager lmanager = new ListsManager(
					tAuth.getTwitterInstance(), ddManager, cRManager, lManager);
			List<String> ulsub = lmanager.UserListSubsciptions();
			ddManager.listObjectWriter(ulsub);

		} else {

			cRManager.DisplayErrorMessage(
					"[ERROR]: Unknown Handler Vector received.", true);
		}

	}

	private boolean verifyTwitterAccess() {

		if (cRManager.isTwitterAccessable()) {
			cRManager.DisplayInfoMessage("ACCESSABLE\n", false);
			cVectors = controlVectors.TSAK_VERIFY_USER_CREDENTIALS;
			return true;
		} else {
			return false;
		}
	}

	private void readEnvVariable(String[] args) {

		if (cRManager.getCredentialsFromENV()) {

			readFromENV = true;
			if (!TwitterCredentials.getIsVerified()) {

				toBeVerifiedENV = true;
			}
			scObj = argManager.getCmdArgs(args, false);
		} else {

			cRManager.DisplayInfoMessage(
					"[INFO]: Reading Twitter Credentials from input args.",
					true);
			scObj = argManager.getCmdArgs(args, true);
		}

		tAuth = new TwitterAuth();
		cVectors = controlVectors.TSAK_TWITTER_ACCESS_VERIFICATION;

		if (scObj == subCmdUpVector.NOP) {
			cVectors = controlVectors.TSAK_SYSTEM_EXIT_PHASE;
		}

	}

	private void exitApp() {
		cRManager
				.DisplayInfoMessage(
						"\n[INFO]: --------------------------------------------------------",
						true);
		cRManager.DisplayInfoMessage(
				"\n               Thank You for using Twiter Swiss Army Knife",
				true);
		cRManager
				.DisplayInfoMessage(
						"\n[INFO]: --------------------------------------------------------",
						true);
		isUserActive = false;
	}

	private boolean verifyTwitterCredentials() {

		boolean verified = false;
		cRManager.DisplayInfoMessage(
				"[INFO]: Verifying Twitter Credentials.... ", false);
		if (tAuth.verifyKeys()) {
			cRManager.DisplayInfoMessage("VERIFIED\n", false);
			cRManager
					.DisplayInfoMessage(
							"[INFO]: Saving Twitter Credentials for future use in conf.properties file.",
							true);
			cVectors = controlVectors.TSAK_TWITTER_GET_DUMP;
			verified = true;
		} else {

			cRManager
					.DisplayErrorMessage(
							"FAILED\n[ERROR]: Failed to verify User Credentials.",
							true);
			verified = false;
			cVectors = controlVectors.TSAK_SYSTEM_EXIT_PHASE;
		}

		return verified;
	}

}
