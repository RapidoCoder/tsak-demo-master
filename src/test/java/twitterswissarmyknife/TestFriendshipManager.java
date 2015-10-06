package twitterswissarmyknife;

import static org.junit.Assert.assertEquals;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jcommander.ArgsManager;

import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import twitter4j.Friendship;
import twitter4j.IDs;
import twitter4j.PagableResponseList;
import twitter4j.Relationship;
import twitter4j.ResponseList;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.User;
import twitterhandler.FriendshipManager;
import twitterhandler.LimitsManager;
import twitterhandler.TwitterCredentials;
import dto.ControlHandlers.LimitsEndPointsVector;
import dto.ControlHandlers.subCmdUpVector;
import dto.CrManager;
import dto.DDManager;
import dto.TsakException;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ TwitterCredentials.class })
public class TestFriendshipManager {
	int lmts[] = { 10, 500 };
	
	@Mock
	Twitter twitter;
	DDManager ddManager;
	CrManager cRManager;
	LimitsManager lManager;
	IDs ids;
	IDs idsEmpty;
	ResponseList<Friendship> friendships;
	Relationship relationship;
	PagableResponseList<User> users;
	ArgsManager argManager;
	User user;

	@SuppressWarnings("unchecked")
	@Before
	public void setup() throws Exception {
		twitter = Mockito.mock(Twitter.class);
		cRManager = Mockito.mock(CrManager.class);
		lManager = Mockito.mock(LimitsManager.class);
		ddManager = Mockito.mock(DDManager.class);
		PowerMockito.mockStatic(TwitterCredentials.class);
		ids = Mockito.mock(IDs.class);
		idsEmpty = Mockito.mock(IDs.class);
		argManager = Mockito.mock(ArgsManager.class);
		relationship = Mockito.mock(Relationship.class);
		users = Mockito.mock(PagableResponseList.class);
		user = Mockito.mock(User.class);
	}

	@Test
	public void inComingFriendships() throws TsakException, TwitterException {
	
		long[] getIds = { 2L };
		long[] emptyIds = {};
		subCmdUpVector sbv = subCmdUpVector.INCOMING_FRNDSHIP;
		Mockito.when(
				cRManager.rateLimitAnalyzer(twitter, lManager,
						LimitsEndPointsVector.FRIENDSHIPS_INCOMING))
				.thenReturn(lmts);
		Mockito.when(twitter.getIncomingFriendships(-1L)).thenReturn(ids);
		Mockito.when(twitter.getIncomingFriendships(0L)).thenReturn(idsEmpty);
		Mockito.when(ids.getIDs()).thenReturn(getIds);
		Mockito.when(idsEmpty.getIDs()).thenReturn(emptyIds);
		Mockito.when(ids.getNextCursor()).thenReturn(0L);
		
		FriendshipManager fManager = new FriendshipManager(twitter, ddManager,
				cRManager, lManager);
		List<Long> expected = new ArrayList<Long>();
		expected.add(2L);
		List<Long> result = fManager.iOFriendships(sbv);
		assertEquals(result, expected);
	}
	
	@Test
	public void outGoingFriendships() throws TsakException, TwitterException {
		
		long[] getIds = {2L};
		long[] emptyIds = {};
		subCmdUpVector sbv =subCmdUpVector.OUTGOING_FRNDSHIP;
		Mockito.when(
				cRManager.rateLimitAnalyzer(twitter, lManager,
						LimitsEndPointsVector.FRIENDSHIPS_OUTGOING)).thenReturn(lmts);
		Mockito.when(twitter.getOutgoingFriendships(-1L)).thenReturn(ids);
		Mockito.when(twitter.getOutgoingFriendships(0L)).thenReturn(idsEmpty);
		Mockito.when( ids.getIDs()).thenReturn(getIds);
		Mockito.when( idsEmpty.getIDs()).thenReturn(emptyIds);
		Mockito.when( ids.getNextCursor()).thenReturn(0L);
	
		FriendshipManager fManager = new FriendshipManager(twitter, ddManager,
				cRManager, lManager);
		List<Long> expected = new ArrayList<Long>();
		expected.add(2L);
		List<Long> result = fManager.iOFriendships(sbv);
		assertEquals(result,expected);
	}

	@Test
	public void showFriendsShip() throws FileNotFoundException, TsakException,
			TwitterException {
		
		HashMap<String, Object> expectedMap = new HashMap<String, Object>();
		expectedMap.put("source", "JhonSmith");
		expectedMap.put("target", "michael");
		expectedMap.put("source_blocking_target", false);
		expectedMap.put("source_followed_by_target", true);
		expectedMap.put("source_following_target", true);
		expectedMap.put("source_muting_target", false);
		expectedMap.put("source_notification_enabled", true);
		expectedMap.put("canSourceDm", true);

		JSONObject expectedJson = new JSONObject(expectedMap);
		String expected = expectedJson.toString();
		Mockito.when(
				cRManager.rateLimitAnalyzer(twitter, lManager,
						LimitsEndPointsVector.FRIENDSHIPS_SHOW)).thenReturn(
				lmts);
		Mockito.when(TwitterCredentials.gettSname()).thenReturn("1234");
		Mockito.when(TwitterCredentials.gettTname()).thenReturn("5678");
		Mockito.when(twitter.showFriendship(1234L, 5678L)).thenReturn(relationship);

		Mockito.when(relationship.getSourceUserScreenName()).thenReturn(
				"JhonSmith");
		Mockito.when(relationship.getTargetUserScreenName()).thenReturn(
				"michael");
		Mockito.when(relationship.isSourceBlockingTarget()).thenReturn(false);
		Mockito.when(relationship.isSourceFollowedByTarget()).thenReturn(true);
		Mockito.when(relationship.isSourceFollowingTarget()).thenReturn(true);
		Mockito.when(relationship.isSourceMutingTarget()).thenReturn(false);
		Mockito.when(relationship.isSourceNotificationsEnabled()).thenReturn(
				true);
		Mockito.when(relationship.canSourceDm()).thenReturn(true);

		FriendshipManager fManager = new FriendshipManager(twitter, ddManager,
				cRManager, lManager);
		String result = fManager.showFriendsShip();

		assertEquals(result, expected);
	}

	@Test
	public void getFriendsList() throws TsakException, NumberFormatException,
			TwitterException {
		
		Map<String, Object> expectedMap = new HashMap<String, Object>();
		expectedMap.put("id", 010101L);
		expectedMap.put("location", "London");
		expectedMap.put("profile_image", "http://someImageUrl/image.jpg");
		expectedMap.put("friends_count", 10);
		expectedMap.put("name", "Jhon Smith");
		expectedMap.put("screen_name", "JhonSmith");
		expectedMap.put("language", "english");
		expectedMap.put("followers_count", 20);
		JSONObject expectedJson = new JSONObject(expectedMap);
		List<String> expected = new ArrayList<String>();
		expected.add(expectedJson.toString());
	
		subCmdUpVector sbv = subCmdUpVector.FRIENDS_LIST;
		Mockito.when(
				cRManager.rateLimitAnalyzer(twitter, lManager,
						LimitsEndPointsVector.FRIENDS_LIST)).thenReturn(lmts);
		Mockito.when(TwitterCredentials.getuID()).thenReturn("010101");
		Mockito.when(twitter.getFriendsList(Long.parseLong("010101"), -1L))
				.thenReturn(users);
		Mockito.when(users.size()).thenReturn(1);
		Mockito.when(users.get(0)).thenReturn(user);
		Mockito.when(user.getScreenName()).thenReturn("JhonSmith");
		Mockito.when(user.getName()).thenReturn("Jhon Smith");
		Mockito.when(user.getId()).thenReturn(010101L);
		Mockito.when(user.getBiggerProfileImageURL()).thenReturn("http://someImageUrl/image.jpg");
		Mockito.when(user.getFriendsCount()).thenReturn(10);
		Mockito.when(user.getFollowersCount()).thenReturn(20);
		Mockito.when(user.getLocation()).thenReturn("London");
		Mockito.when(user.getLang()).thenReturn("english");
	
		FriendshipManager fManager = new FriendshipManager(twitter, ddManager,
				cRManager, lManager);
		List<String> result = fManager.getFriendsList(sbv);
		assertEquals(result, expected);
	}
}
