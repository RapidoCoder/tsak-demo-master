package twitterswissarmyknife;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import twitter4j.IDs;
import twitter4j.ResponseList;
import twitter4j.Twitter;
import twitter4j.User;
import twitterhandler.FFManager;
import twitterhandler.LimitsManager;
import twitterhandler.TwitterCredentials;
import dto.ControlHandlers.LimitsEndPointsVector;
import dto.ControlHandlers.controlVectors;
import dto.ControlHandlers.subCmdUpVector;
import dto.CrManager;
import dto.DDManager;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ controlVectors.class, TwitterCredentials.class })
public class TestFFManager {
	@Mock
	CrManager cRManager;
	DDManager ddManager;
	Twitter twitter;
	LimitsManager lManager;
	
	IDs followerIds;
	IDs friendsIds;
	
	ResponseList<User> followers;
	ResponseList<User> followings;
	ResponseList<User> expected;

	@SuppressWarnings("unchecked")
	@Before
	public void setup() throws Exception {
		cRManager = Mockito.mock(CrManager.class);
		ddManager = Mockito.mock(DDManager.class);
		twitter = Mockito.mock(Twitter.class);
		lManager = Mockito.mock(LimitsManager.class);
		PowerMockito.mockStatic(TwitterCredentials.class);
		
		followerIds = Mockito.mock(IDs.class);
		friendsIds = Mockito.mock(IDs.class);
		followers = Mockito.mock(ResponseList.class);
		followings = Mockito.mock(ResponseList.class);
		expected = Mockito.mock(ResponseList.class);
	}

	@Test
	public void getFollowersByID() throws Exception {
		String uid = "010101";
		int lmts[] = { 10, 500 };
		subCmdUpVector sbv = subCmdUpVector.FLWRZ_DUMP_BY_ID;
		Mockito.when(twitter.getFollowersIDs(Long.parseLong(uid), -1L))
				.thenReturn(followerIds);
		Mockito.when(
				cRManager.rateLimitAnalyzer(twitter, lManager,
						LimitsEndPointsVector.FOLLOWERS_IDS)).thenReturn(lmts);
		FFManager ffmanager = new FFManager(twitter, ddManager, cRManager,
				lManager);
		Mockito.when(twitter.lookupUsers(followerIds.getIDs())).thenReturn(
				expected);

		ResponseList<User> result = ffmanager.dumpFFToFile(uid, sbv);
		assertEquals(result, expected);
	}

	@Test
	public void getFollowersByScreenName() throws Exception {
		String tuser = "JhonSmith";
		int lmts[] = { 10, 500 };
		subCmdUpVector sbv = subCmdUpVector.FLWRZ_DUMP_BY_SCREEN_NAME;
		Mockito.when(twitter.getFollowersIDs(tuser, -1L)).thenReturn(
				followerIds);
		Mockito.when(
				cRManager.rateLimitAnalyzer(twitter, lManager,
						LimitsEndPointsVector.FOLLOWERS_IDS)).thenReturn(lmts);
		FFManager ffmanager = new FFManager(twitter, ddManager, cRManager,
				lManager);
		Mockito.when(twitter.lookupUsers(followerIds.getIDs())).thenReturn(
				expected);

		ResponseList<User> result = ffmanager.dumpFFToFile(tuser, sbv);
		assertEquals(result, expected);
	}

	@Test
	public void getFriendsByID() throws Exception {
		String uid = "010101";
		int lmts[] = { 10, 500 };
		subCmdUpVector sbv = subCmdUpVector.FRNDZ_DUMP_BY_ID;
		Mockito.when(twitter.getFriendsIDs(Long.parseLong(uid), -1L))
				.thenReturn(followerIds);
		Mockito.when(
				cRManager.rateLimitAnalyzer(twitter, lManager,
						LimitsEndPointsVector.FRIENDS_IDS)).thenReturn(lmts);
		FFManager ffmanager = new FFManager(twitter, ddManager, cRManager,
				lManager);
		Mockito.when(twitter.lookupUsers(friendsIds.getIDs())).thenReturn(
				expected);

		ResponseList<User> result = ffmanager.dumpFFToFile(uid, sbv);
		assertEquals(result, expected);
	}

	@Test
	public void getFriendsByScreenName() throws Exception {
		String tuser = "JhonSmith";
		int lmts[] = { 10, 500 };
		subCmdUpVector sbv = subCmdUpVector.FRNDZ_DUMP_BY_SCREEN_NAME;
		Mockito.when(twitter.getFriendsIDs(String.valueOf(tuser), -1L))
				.thenReturn(followerIds);
		Mockito.when(
				cRManager.rateLimitAnalyzer(twitter, lManager,
						LimitsEndPointsVector.FRIENDS_IDS)).thenReturn(lmts);
		FFManager ffmanager = new FFManager(twitter, ddManager, cRManager,
				lManager);
		Mockito.when(twitter.lookupUsers(friendsIds.getIDs())).thenReturn(
				expected);

		ResponseList<User> result = ffmanager.dumpFFToFile(tuser, sbv);
		assertEquals(result, expected);
	}
}
