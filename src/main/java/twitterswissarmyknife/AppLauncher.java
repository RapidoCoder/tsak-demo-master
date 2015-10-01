package twitterswissarmyknife;

import java.io.IOException;
import java.util.Scanner;

import jcommander.ArgsManager;
import twitter4j.IDs;
import twitter4j.Paging;
import twitter4j.TwitterException;
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

	public AppLauncher(controlVectors cVectors,
					   CrManager cRManager,
					   ArgsManager argManager,
					   subCmdUpVector scObj,
					   TwitterAuth tAuth,
					   LimitsManager lManager,
					   DDManager ddManager
			) {
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
		isUserActive = true;
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

						verifyTwitterCredentials();
						cRManager.printUserProfile(tAuth.getUser(), true);
						cRManager.writeVerificationToConfFile(true);
						TwitterCredentials.setIsVerified(true);
					}
				} else {

					verifyTwitterCredentials();
					cRManager.printUserProfile(tAuth.getUser(), true);
					cRManager.writeVerificationToConfFile(true);
				}
				cVectors = controlVectors.TSAK_TWITTER_GET_DUMP;
				break;
			case TSAK_TWITTER_GET_DUMP:
				try {
					System.out.println("DumptoFileFn");
					System.out.println("1: "+scObj);
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
				System.exit(-1);
				break;
			}

		}
	}

	private void dumpToFile() throws IOException, IllegalStateException,
			TsakException, TwitterException {
		System.out.println("2: "+scObj);
		if (scObj == subCmdUpVector.FLWRZ_DUMP_BY_ID) {
			System.out.println("Now dumping followers...");
			FFManager fmanager = new FFManager(tAuth.getTwitterInstance(),
					ddManager, cRManager, lManager);
			
			fmanager.dumpFFToFile(TwitterCredentials.getuID(), scObj);

		} else if (scObj == subCmdUpVector.FLWRZ_DUMP_BY_SCREEN_NAME) {

			FFManager fmanager = new FFManager(tAuth.getTwitterInstance(),
					ddManager, cRManager, lManager);
			fmanager.dumpFFToFile(TwitterCredentials.getuScreenName(), scObj);

		} else if (scObj == subCmdUpVector.FRNDZ_DUMP_BY_ID) {

			FFManager fmanager = new FFManager(tAuth.getTwitterInstance(),
					ddManager, cRManager, lManager);
			fmanager.dumpFFToFile(TwitterCredentials.getuID(), scObj);

		} else if (scObj == subCmdUpVector.FRNDZ_DUMP_BY_SCREEN_NAME) {

			FFManager fmanager = new FFManager(tAuth.getTwitterInstance(),
					ddManager, cRManager, lManager);
			fmanager.dumpFFToFile(TwitterCredentials.getuScreenName(), scObj);

		} else if (scObj == subCmdUpVector.HOME_TIMELINE) {
			StatusesManager stmanager = new StatusesManager(
					tAuth.getTwitterInstance(), ddManager, cRManager, lManager);
			stmanager.userTimeLine(tAuth.getTwitterInstance().getScreenName(),
					scObj);
		} else if (scObj == subCmdUpVector.USER_TIMELINE) {
			StatusesManager stmanager = new StatusesManager(
					tAuth.getTwitterInstance(), ddManager, cRManager, lManager);
			stmanager.userTimeLine(null, scObj);
		} else if (scObj == subCmdUpVector.OWN_RETWEETS) {
			StatusesManager stmanager = new StatusesManager(
					tAuth.getTwitterInstance(), ddManager, cRManager, lManager);
			stmanager.userTimeLine(tAuth.getTwitterInstance().getScreenName(),
					scObj);
		} else if (scObj == subCmdUpVector.GET_STATUS_BY_ID) {
			StatusesManager stmanager = new StatusesManager(
					tAuth.getTwitterInstance(), ddManager, cRManager, lManager);
			stmanager.getStatusById(TwitterCredentials.getsID());
		} else if (scObj == subCmdUpVector.GET_STATUS_RETWEETERS) {
			StatusesManager stmanager = new StatusesManager(
					tAuth.getTwitterInstance(), ddManager, cRManager, lManager);
			stmanager.getStatusRetweeters(TwitterCredentials.getsID());
		} else if (scObj == subCmdUpVector.MENTIONS_TIMELINE) {
			StatusesManager stmanager = new StatusesManager(
					tAuth.getTwitterInstance(), ddManager, cRManager, lManager);
			stmanager.getMentionsTimeline();

		} else if (scObj == subCmdUpVector.SEARCH_TWEETS) {
			SearchManager smanager = new SearchManager(
					tAuth.getTwitterInstance(), ddManager, cRManager, lManager);
			smanager.searchTweets(TwitterCredentials.getKeyWords());

		} else if (scObj == subCmdUpVector.SEARCH_USER) {
			SearchManager smanager = new SearchManager(
					tAuth.getTwitterInstance(), ddManager, cRManager, lManager);
			smanager.searchUsers(TwitterCredentials.getKeyWords());

		} else if (scObj == subCmdUpVector.USERS_LOOKUP) {

			UsersManager umanager = new UsersManager(
					tAuth.getTwitterInstance(), ddManager, cRManager, lManager);
			umanager.lookupUsers(TwitterCredentials.getInputFile());

		} else if (scObj == subCmdUpVector.BLOCK_LIST) {

			AccountManager amanager = new AccountManager(
					tAuth.getTwitterInstance(), ddManager, cRManager, lManager);
			amanager.getBlockList();

		} else if (scObj == subCmdUpVector.SHOW_FRNDSHIP) {
			FriendshipManager fmanager = new FriendshipManager(
					tAuth.getTwitterInstance(), ddManager, cRManager, lManager);
			fmanager.showFriendsShip();

		} else if (scObj == subCmdUpVector.FRIENDS_LIST) {
			FriendshipManager fmanager = new FriendshipManager(
					tAuth.getTwitterInstance(), ddManager, cRManager, lManager);
			fmanager.getFriendsList(scObj);
		} else if (scObj == subCmdUpVector.FOLLOWERS_LIST) {

			FriendshipManager fmanager = new FriendshipManager(
					tAuth.getTwitterInstance(), ddManager, cRManager, lManager);
			fmanager.getFriendsList(scObj);

		} else if (scObj == subCmdUpVector.FAVOURITES) {

			AccountManager amanager = new AccountManager(
					tAuth.getTwitterInstance(), ddManager, cRManager, lManager);
			amanager.getFavourities();

		} else if (scObj == subCmdUpVector.SUGGESTED_USER_CATS) {

			UsersManager umanager = new UsersManager(
					tAuth.getTwitterInstance(), ddManager, cRManager, lManager);
			umanager.getSuggestedCatagories();
		} else if (scObj == subCmdUpVector.SUGGESTION_SLUG) {

			UsersManager umanager = new UsersManager(
					tAuth.getTwitterInstance(), ddManager, cRManager, lManager);
			umanager.getUserSuggestions(TwitterCredentials.getcSlug());
		} else if (scObj == subCmdUpVector.MEMBER_SUGGESTION) {

			UsersManager umanager = new UsersManager(
					tAuth.getTwitterInstance(), ddManager, cRManager, lManager);
			umanager.getMemberSuggestions(TwitterCredentials.getcSlug());
		} else if (scObj == subCmdUpVector.USER_LISTS) {

			ListsManager lmanager = new ListsManager(
					tAuth.getTwitterInstance(), ddManager, cRManager, lManager);
			lmanager.getUserLists();
		} else if (scObj == subCmdUpVector.LIST_STATUSES) {

			ListsManager lmanager = new ListsManager(
					tAuth.getTwitterInstance(), ddManager, cRManager, lManager);
			lmanager.getListStatuses();
		} else if (scObj == subCmdUpVector.SAVED_SEARCHES) {

			AccountManager amanager = new AccountManager(
					tAuth.getTwitterInstance(), ddManager, cRManager, lManager);
			amanager.SavedSearches();

		} else if (scObj == subCmdUpVector.LOOKUP_FRNDSHIP) {

			FriendshipManager fmanager = new FriendshipManager(
					tAuth.getTwitterInstance(), ddManager, cRManager, lManager);
			fmanager.lookupFriendship();
		} else if (scObj == subCmdUpVector.INCOMING_FRNDSHIP) {
			FriendshipManager fmanager = new FriendshipManager(
					tAuth.getTwitterInstance(), ddManager, cRManager, lManager);
			fmanager.iOFriendships(scObj);

		} else if (scObj == subCmdUpVector.OUTGOING_FRNDSHIP) {
			FriendshipManager fmanager = new FriendshipManager(
					tAuth.getTwitterInstance(), ddManager, cRManager, lManager);
			fmanager.iOFriendships(scObj);
		} else if (scObj == subCmdUpVector.GEO_DETAILS) {

			GisManager gmanager = new GisManager(tAuth.getTwitterInstance(),
					ddManager, cRManager, lManager);
			gmanager.getGeoLocationInfo();

		} else if (scObj == subCmdUpVector.SIMILAR_PLACES) {

			GisManager gmanager = new GisManager(tAuth.getTwitterInstance(),
					ddManager, cRManager, lManager);
			gmanager.getSimilarPlaces();

		} else if (scObj == subCmdUpVector.SEARCH_PLACE_BY_CORD) {

			GisManager gmanager = new GisManager(tAuth.getTwitterInstance(),
					ddManager, cRManager, lManager);
			gmanager.searchPlace();
		} else if (scObj == subCmdUpVector.ACCOUNT_SETTINGS) {

			AccountManager amanager = new AccountManager(
					tAuth.getTwitterInstance(), ddManager, cRManager, lManager);
			amanager.getAccountSettingsJson();
		} else if (scObj == subCmdUpVector.AVAILABLE_TRENDS) {

			GisManager gmanager = new GisManager(tAuth.getTwitterInstance(),
					ddManager, cRManager, lManager);
			gmanager.getAvalilableTrends();
		} else if (scObj == subCmdUpVector.PLACE_TRENDS) {

			GisManager gmanager = new GisManager(tAuth.getTwitterInstance(),
					ddManager, cRManager, lManager);
			gmanager.getPlaceTrends();
		} else if (scObj == subCmdUpVector.CLOSEST_TRENDS) {

			GisManager gmanager = new GisManager(tAuth.getTwitterInstance(),
					ddManager, cRManager, lManager);
			gmanager.getClosestTrends();

		} else if (scObj == subCmdUpVector.MUTES_IDS) {

			AccountManager amanager = new AccountManager(
					tAuth.getTwitterInstance(), ddManager, cRManager, lManager);
			amanager.getMutesIds();
		} else if (scObj == subCmdUpVector.MUTES_LIST) {

			AccountManager amanager = new AccountManager(
					tAuth.getTwitterInstance(), ddManager, cRManager, lManager);
			amanager.getMutesLists();
		} else if (scObj == subCmdUpVector.LISTS_MEMEBERSHIPS) {

			AccountManager amanager = new AccountManager(
					tAuth.getTwitterInstance(), ddManager, cRManager, lManager);
			amanager.userListMemberships();
		} else if (scObj == subCmdUpVector.LIST_SUBSCRIBERS
				|| scObj == subCmdUpVector.LIST_MEMBERS) {

			ListsManager lmanager = new ListsManager(
					tAuth.getTwitterInstance(), ddManager, cRManager, lManager);
			lmanager.getUserListSubscribers(scObj);

		} else if (scObj == subCmdUpVector.LIST_SUBSCRIPTIONS) {

			ListsManager lmanager = new ListsManager(
					tAuth.getTwitterInstance(), ddManager, cRManager, lManager);
			lmanager.UserListSubsciptions();

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

	private void verifyTwitterCredentials() {

		cRManager.DisplayInfoMessage(
				"[INFO]: Verifying Twitter Credentials.... ", false);
		if (tAuth.verifyKeys()) {
			cRManager.DisplayInfoMessage("VERIFIED\n", false);
			cRManager
					.DisplayInfoMessage(
							"[INFO]: Saving Twitter Credentials for future use in conf.properties file.",
							true);
			cVectors = controlVectors.TSAK_TWITTER_GET_DUMP;

		} else {

			cRManager
					.DisplayErrorMessage(
							"FAILED\n[ERROR]: Failed to verify User Credentials.",
							true);
			cVectors = controlVectors.TSAK_SYSTEM_EXIT_PHASE;
		}
	}

}
