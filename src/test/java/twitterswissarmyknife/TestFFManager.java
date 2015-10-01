package twitterswissarmyknife;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import jcommander.ArgsManager;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import twitter4j.IDs;
import twitter4j.Paging;
import twitter4j.ResponseList;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.User;
import twitterhandler.FFManager;
import twitterhandler.LimitsManager;
import twitterhandler.TwitterAuth;
import twitterhandler.TwitterCredentials;
import dto.ControlHandlers.LimitsEndPointsVector;
import dto.ControlHandlers.controlVectors;
import dto.ControlHandlers.subCmdUpVector;
import dto.CrManager;
import dto.DDManager;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ controlVectors.class, TwitterCredentials.class, Paging.class })
public class TestFFManager {
	@Mock
	CrManager cRManager;
	controlVectors cVectors;
	ArgsManager argManager;
	Twitter twitter;
	TwitterAuth tAuth;
	IDs followerIds;
	IDs friendsIds;
	LimitsManager lManager;
	DDManager ddManager;
	ResponseList<User> followers;
	ResponseList<User> followings;
	FFManager fmanager;
	Paging page;

	@Before
	public void setup() throws Exception {
		cRManager = Mockito.mock(CrManager.class);

		cVectors = Mockito.mock(controlVectors.class);
		argManager = Mockito.mock(ArgsManager.class);
		tAuth = Mockito.mock(TwitterAuth.class);
		twitter = Mockito.mock(Twitter.class);
		fmanager = Mockito.mock(FFManager.class);
		lManager = Mockito.mock(LimitsManager.class);
		PowerMockito.mockStatic(TwitterCredentials.class);
		followerIds = Mockito.mock(IDs.class);
		friendsIds = Mockito.mock(IDs.class);
		followers = Mockito.mock(ResponseList.class);
		followings = Mockito.mock(ResponseList.class);
		ddManager = Mockito.mock(DDManager.class);
		PowerMockito.whenNew(Paging.class).withArguments(1, 200)
				.thenReturn(page);
		page = Mockito.mock(Paging.class);
	}

	// ////////////////////////
	@Test
	public void Test_FLWRZ_DUMP_BY_ID() throws Exception {

		cVectors = controlVectors.TSAK_TWITTER_GET_DUMP;
		String args[] = { "tsak", "dumpFollowerIDs", "-uid", "123", "-o",
				"output.csv" };
		int lmts[] = { 15, 1000 };
		Mockito.when(cRManager.getCredentialsFromENV()).thenReturn(true);

		Mockito.when(TwitterCredentials.getIsVerified()).thenReturn(true);
		Mockito.when(TwitterCredentials.getOutputFile()).thenReturn("abc.txt");
		Mockito.when(argManager.getCmdArgs(args, false)).thenReturn(
				subCmdUpVector.FLWRZ_DUMP_BY_ID);
		Mockito.when(tAuth.getTwitterInstance()).thenReturn(twitter);
		Mockito.when(TwitterCredentials.getuID()).thenReturn("123");

		Mockito.when(twitter.getFollowersIDs(Long.parseLong("123"), -1L))
				.thenReturn(followerIds);

		Mockito.when(
				cRManager.rateLimitAnalyzer(twitter, lManager,
						LimitsEndPointsVector.FOLLOWERS_IDS)).thenReturn(lmts);
		subCmdUpVector scObj = argManager.getCmdArgs(args, false);
		Mockito.when(twitter.lookupUsers(followerIds.getIDs())).thenReturn(
				followers);
		Mockito.doNothing().when(ddManager).closeWriter();
		Mockito.when(
				fmanager.dumpFFToFile("123", subCmdUpVector.FLWRZ_DUMP_BY_ID))
				.thenReturn(followers);

		AppLauncher obj = new AppLauncher(cVectors, cRManager, argManager,
				scObj, tAuth, lManager, ddManager,page);
		obj.initLaunchSequence(args);
		assertEquals(
				fmanager.dumpFFToFile("123", subCmdUpVector.FLWRZ_DUMP_BY_ID),
				followers);

	}

	@Test
	public void Test_FLWRZ_DUMP_BY_SCREEN_NAME() throws TwitterException,
			IOException {
		cVectors = controlVectors.TSAK_TWITTER_GET_DUMP;
		String args[] = { "tsak", "dumpFollowerIDs", "-uname", "ScreenName",
				"-o", "output.csv" };
		int lmts[] = { 15, 1000 };
		Mockito.when(cRManager.getCredentialsFromENV()).thenReturn(true);

		Mockito.when(TwitterCredentials.getIsVerified()).thenReturn(true);
		Mockito.when(TwitterCredentials.getOutputFile()).thenReturn("abc.txt");
		Mockito.when(argManager.getCmdArgs(args, false)).thenReturn(
				subCmdUpVector.FLWRZ_DUMP_BY_SCREEN_NAME);
		Mockito.when(tAuth.getTwitterInstance()).thenReturn(twitter);
		Mockito.when(TwitterCredentials.getuScreenName()).thenReturn(
				"ScreenName");

		Mockito.when(twitter.getFollowersIDs("ScreenName", -1L)).thenReturn(
				followerIds);

		Mockito.when(
				cRManager.rateLimitAnalyzer(twitter, lManager,
						LimitsEndPointsVector.FOLLOWERS_IDS)).thenReturn(lmts);
		subCmdUpVector scObj = argManager.getCmdArgs(args, false);
		Mockito.when(twitter.lookupUsers(followerIds.getIDs())).thenReturn(
				followers);
		Mockito.doNothing().when(ddManager).closeWriter();
		Mockito.when(
				fmanager.dumpFFToFile("ScreenName",
						subCmdUpVector.FLWRZ_DUMP_BY_SCREEN_NAME)).thenReturn(
				followers);

		AppLauncher obj = new AppLauncher(cVectors, cRManager, argManager,
				scObj, tAuth, lManager, ddManager,page);
		obj.initLaunchSequence(args);
		assertEquals(fmanager.dumpFFToFile("ScreenName",
				subCmdUpVector.FLWRZ_DUMP_BY_SCREEN_NAME), followers);
	}

	@Test
	public void Test_FRIENDS_DUMP_BY_SCREEN_NAME() throws TwitterException,
			IOException {
		cVectors = controlVectors.TSAK_TWITTER_GET_DUMP;
		String args[] = { "tsak", "dumpFriendIDs", "-uname", "ScreenName",
				"-o", "output.csv" };
		int lmts[] = { 15, 1000 };
		Mockito.when(cRManager.getCredentialsFromENV()).thenReturn(true);

		Mockito.when(TwitterCredentials.getIsVerified()).thenReturn(true);
		Mockito.when(TwitterCredentials.getOutputFile()).thenReturn("def.txt");
		Mockito.when(argManager.getCmdArgs(args, false)).thenReturn(
				subCmdUpVector.FRNDZ_DUMP_BY_SCREEN_NAME);
		Mockito.when(tAuth.getTwitterInstance()).thenReturn(twitter);
		Mockito.when(TwitterCredentials.getuScreenName()).thenReturn(
				"ScreenName");

		Mockito.when(twitter.getFriendsIDs("ScreenName", -1L)).thenReturn(
				friendsIds);

		Mockito.when(
				cRManager.rateLimitAnalyzer(twitter, lManager,
						LimitsEndPointsVector.FRIENDS_IDS)).thenReturn(lmts);
		subCmdUpVector scObj = argManager.getCmdArgs(args, false);
		Mockito.when(twitter.lookupUsers(friendsIds.getIDs())).thenReturn(
				followings);
		Mockito.doNothing().when(ddManager).closeWriter();
		Mockito.when(
				fmanager.dumpFFToFile("ScreenName",
						subCmdUpVector.FRNDZ_DUMP_BY_SCREEN_NAME)).thenReturn(
				followings);

		AppLauncher obj = new AppLauncher(cVectors, cRManager, argManager,
				scObj, tAuth, lManager, ddManager,page);
		obj.initLaunchSequence(args);
		assertEquals(fmanager.dumpFFToFile("ScreenName",
				subCmdUpVector.FRNDZ_DUMP_BY_SCREEN_NAME), followings);
	}

	@Test
	public void Test_FRIENDS_DUMP_BY_ID() throws TwitterException, IOException {
		cVectors = controlVectors.TSAK_TWITTER_GET_DUMP;
		String args[] = { "tsak", "dumpFriendIDs", "-uid", "123", "-o",
				"output.csv" };
		int lmts[] = { 15, 1000 };
		Mockito.when(cRManager.getCredentialsFromENV()).thenReturn(true);

		Mockito.when(TwitterCredentials.getIsVerified()).thenReturn(true);
		Mockito.when(TwitterCredentials.getOutputFile()).thenReturn("def.txt");
		Mockito.when(argManager.getCmdArgs(args, false)).thenReturn(
				subCmdUpVector.FRNDZ_DUMP_BY_ID);
		Mockito.when(tAuth.getTwitterInstance()).thenReturn(twitter);
		Mockito.when(TwitterCredentials.getuID()).thenReturn("123");

		Mockito.when(twitter.getFriendsIDs(Long.parseLong("123"), -1L))
				.thenReturn(friendsIds);

		Mockito.when(
				cRManager.rateLimitAnalyzer(twitter, lManager,
						LimitsEndPointsVector.FRIENDS_IDS)).thenReturn(lmts);
		subCmdUpVector scObj = argManager.getCmdArgs(args, false);
		Mockito.when(twitter.lookupUsers(friendsIds.getIDs())).thenReturn(
				followings);
		Mockito.doNothing().when(ddManager).closeWriter();
		Mockito.when(
				fmanager.dumpFFToFile("123", subCmdUpVector.FRNDZ_DUMP_BY_ID))
				.thenReturn(followings);

		AppLauncher obj = new AppLauncher(cVectors, cRManager, argManager,
				scObj, tAuth, lManager, ddManager,page);
		obj.initLaunchSequence(args);
		assertEquals(
				fmanager.dumpFFToFile("123", subCmdUpVector.FRNDZ_DUMP_BY_ID),
				followings);
	}
}
