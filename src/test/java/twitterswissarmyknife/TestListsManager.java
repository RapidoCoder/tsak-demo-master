package twitterswissarmyknife;

import static org.junit.Assert.assertEquals;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
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

import twitter4j.GeoLocation;
import twitter4j.HashtagEntity;
import twitter4j.MediaEntity;
import twitter4j.PagableResponseList;
import twitter4j.Paging;
import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.User;
import twitter4j.UserList;
import twitter4j.UserMentionEntity;
import twitterhandler.LimitsManager;
import twitterhandler.ListsManager;
import twitterhandler.TwitterAuth;
import twitterhandler.TwitterCredentials;
import dto.ControlHandlers.LimitsEndPointsVector;
import dto.ControlHandlers.controlVectors;
import dto.ControlHandlers.subCmdUpVector;
import dto.CrManager;
import dto.DDManager;
import dto.TsakException;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ controlVectors.class, TwitterCredentials.class})
public class TestListsManager {
	@Mock
	CrManager cRManager;
	controlVectors cVectors;
	ArgsManager argManager;
	Twitter twitter;
	TwitterAuth tAuth;
	LimitsManager lManager;
	DDManager ddManager;
	
	Status getRetweetdStatus;
	
	Status status;
	ResponseList<Status> statuses;
	Iterator<Status> statusIterator;
	UserMentionEntity userMention;
	HashtagEntity hashtagEntity;
	MediaEntity mediaEntity;
	PagableResponseList<User> users;
	User user;
	Iterator<User> userIterator;
	GeoLocation geoLocation;
	Date date;
	
	UserList userlist;
	
	Iterator<UserList> userlistIterator;
	ResponseList<UserList> userlists;
	PagableResponseList<UserList> lists;
	
	@SuppressWarnings("unchecked")
	@Before
	public void setup(){
		cRManager = Mockito.mock(CrManager.class);

		cVectors = Mockito.mock(controlVectors.class);
		argManager = Mockito.mock(ArgsManager.class);
		tAuth = Mockito.mock(TwitterAuth.class);
		twitter = Mockito.mock(Twitter.class);
		lManager = Mockito.mock(LimitsManager.class);
		ddManager = Mockito.mock(DDManager.class);
		 
		PowerMockito.mockStatic(TwitterCredentials.class);
		userlists = Mockito.mock(ResponseList.class);
		lists = Mockito.mock(PagableResponseList.class);
		userlist = Mockito.mock(UserList.class);
		userlistIterator = Mockito.mock(Iterator.class);
		statuses = Mockito.mock(ResponseList.class);
		getRetweetdStatus = Mockito.mock(Status.class);
		userMention = Mockito.mock(UserMentionEntity.class);
		hashtagEntity = Mockito.mock(HashtagEntity.class);
		mediaEntity = Mockito.mock(MediaEntity.class);
		
		user = Mockito.mock(User.class);
		users = Mockito.mock(PagableResponseList.class);
		userIterator = Mockito.mock(Iterator.class);
		geoLocation = Mockito.mock(GeoLocation.class);
		
		status = Mockito.mock(Status.class);
		statusIterator = Mockito.mock(Iterator.class);
		date = Mockito.mock(Date.class);
		
		
		
		
	}
	
	
	@Test
	public void getListStatuses() throws TsakException, NumberFormatException, TwitterException{
		
		Map<String, Object> expectedMap = new HashMap<String, Object>();
		expectedMap.put("createdAt", null);
		expectedMap.put("id", 1);
		expectedMap.put("text", "some text");
		expectedMap.put("retweetedStatus", getRetweetdStatus);
		expectedMap.put("userMentionEntities", new UserMentionEntity[]{userMention});
		expectedMap.put("hashtagEntities",new HashtagEntity[]{hashtagEntity});
		expectedMap.put("mediaEntities", new MediaEntity[]{mediaEntity});
		expectedMap.put("user", user);
		expectedMap.put("geoLocation", geoLocation);
		JSONObject expectedJson = new JSONObject(expectedMap);
		List<String> expected = new ArrayList<String>();
		expected.add(expectedJson.toString());
		Paging page = new Paging(1, 50);
		int lmts[] = { 15, 1000 };
		Mockito.when( cRManager.rateLimitAnalyzer(twitter, lManager,
				LimitsEndPointsVector.LISTS_STATUSES)).thenReturn(lmts);
		Mockito.when(TwitterCredentials.getLid()).thenReturn("123");
		Mockito.when( twitter.getUserListStatuses(Long.parseLong(TwitterCredentials.getLid()), page)).thenReturn(statuses);
		
		Mockito.when(statusIterator.hasNext()).thenReturn(true, false);
		Mockito.when(statusIterator.next()).thenReturn(status);
		Mockito.when(statuses.iterator()).thenReturn(statusIterator);

		Mockito.when(status.getCreatedAt()).thenReturn(null);
		Mockito.when(status.getId()).thenReturn(1L);
		Mockito.when(status.getText()).thenReturn("some text");
		Mockito.when(status.getRetweetedStatus()).thenReturn(getRetweetdStatus);
		Mockito.when(status.getUserMentionEntities()).thenReturn(new UserMentionEntity[]{userMention});
		Mockito.when(status.getHashtagEntities()).thenReturn(new HashtagEntity[]{hashtagEntity});
		Mockito.when(status.getMediaEntities()).thenReturn(new MediaEntity[]{mediaEntity});
		Mockito.when(status.getUser()).thenReturn(user);
		Mockito.when(status.getGeoLocation()).thenReturn(geoLocation);
		
		ListsManager listManager = new ListsManager(twitter, ddManager, cRManager, lManager);

		List<String> result = listManager.getListStatuses();
		
		assertEquals(result, expected);
		
		
	}
	
	@Test
	public void getUserLists() throws NumberFormatException, TwitterException, TsakException{
		Map<String, Object> expectedMap = new HashMap<String, Object>();
		expectedMap.put("id", 1L);
		expectedMap.put("description", "description");
		expectedMap.put("name", "name");
		
		JSONObject expectedJson = new JSONObject(expectedMap);
		List<String> expected = new ArrayList<String>();
		expected.add(expectedJson.toString());
		int lmts[] = { 15, 1000 };
		Mockito.when( cRManager.rateLimitAnalyzer(twitter, lManager,
				LimitsEndPointsVector.LISTS_LIST)).thenReturn(lmts);
		Mockito.when(TwitterCredentials.getuID()).thenReturn("1");
		Mockito.when(twitter.getUserLists(Long
				.parseLong(TwitterCredentials.getuID()))).thenReturn(userlists);
		
		Mockito.when(userlistIterator.hasNext()).thenReturn(true, false);
		Mockito.when(userlistIterator.next()).thenReturn(userlist);
		Mockito.when(userlists.iterator()).thenReturn(userlistIterator);
		
		Mockito.when(userlist.getName()).thenReturn("name");
		Mockito.when(userlist.getId()).thenReturn(1L);
		Mockito.when(userlist.getDescription()).thenReturn("description");
		
		ListsManager listManager = new ListsManager(twitter, ddManager, cRManager, lManager);

		List<String> result = listManager.getUserLists();
		assertEquals(result, expected);
	}
	
	@Test
	public void getUserListSubscribers() throws TsakException, NumberFormatException, TwitterException{
		Map<String, Object> expectedMap = new HashMap<String, Object>();
		expectedMap.put("id", 1L);
		expectedMap.put("location", "location");
		expectedMap.put("profile_image", "url");
		expectedMap.put("friends_count", 1);
		expectedMap.put("name", "name");
		expectedMap.put("screen_name", "screen name");
		expectedMap.put("language", "english");
		expectedMap.put("followers_count", 1);
		
		
		JSONObject expectedJson = new JSONObject(expectedMap);
		List<String> expected = new ArrayList<String>();
		expected.add(expectedJson.toString());
		int lmts[] = { 15, 1000 };
		subCmdUpVector sbv = subCmdUpVector.LIST_SUBSCRIBERS;
		Mockito.when(cRManager.rateLimitAnalyzer(twitter, lManager,
				LimitsEndPointsVector.LISTS_SUBSCRIBERS)).thenReturn(lmts);
		Mockito.when(TwitterCredentials.getLid()).thenReturn("1");
		Mockito.when(twitter.getUserListSubscribers(Long.parseLong(TwitterCredentials.getLid()),  -1L)).thenReturn(users);
		Mockito.when(userIterator.hasNext()).thenReturn(true, false);
		Mockito.when(userIterator.next()).thenReturn(user);
		Mockito.when(users.iterator()).thenReturn(userIterator);
		
		Mockito.when(user.getScreenName()).thenReturn("screen name");
		Mockito.when(user.getName()).thenReturn("name");
		Mockito.when(user.getId()).thenReturn(1L);
		Mockito.when(user.getBiggerProfileImageURL()).thenReturn("url");
		Mockito.when(user.getFriendsCount()).thenReturn(1);
		Mockito.when(user.getFollowersCount()).thenReturn(1);
		Mockito.when(user.getLocation()).thenReturn("location");
		Mockito.when(user.getLang()).thenReturn("english");
		
		
		ListsManager listManager = new ListsManager(twitter, ddManager, cRManager, lManager);

		List<String> result = listManager.getUserListSubscribers(sbv);
		assertEquals(result, expected);
	}
	@Test
	public void getUserListSubscribersByMembers() throws TsakException, NumberFormatException, TwitterException{
		Map<String, Object> expectedMap = new HashMap<String, Object>();
		expectedMap.put("id", 1L);
		expectedMap.put("location", "location");
		expectedMap.put("profile_image", "url");
		expectedMap.put("friends_count", 1);
		expectedMap.put("name", "name");
		expectedMap.put("screen_name", "screen name");
		expectedMap.put("language", "english");
		expectedMap.put("followers_count", 1);
		
		
		JSONObject expectedJson = new JSONObject(expectedMap);
		List<String> expected = new ArrayList<String>();
		expected.add(expectedJson.toString());
		int lmts[] = { 15, 1000 };
		subCmdUpVector sbv = subCmdUpVector.LIST_MEMBERS;
		Mockito.when(cRManager.rateLimitAnalyzer(twitter, lManager,
				LimitsEndPointsVector.LISTS_MEMBERS)).thenReturn(lmts);
		Mockito.when(TwitterCredentials.getLid()).thenReturn("1");
		Mockito.when(twitter.getUserListMembers(Long.parseLong(TwitterCredentials.getLid()),  -1L)).thenReturn(users);
		Mockito.when(userIterator.hasNext()).thenReturn(true, false);
		Mockito.when(userIterator.next()).thenReturn(user);
		Mockito.when(users.iterator()).thenReturn(userIterator);
		
		Mockito.when(user.getScreenName()).thenReturn("screen name");
		Mockito.when(user.getName()).thenReturn("name");
		Mockito.when(user.getId()).thenReturn(1L);
		Mockito.when(user.getBiggerProfileImageURL()).thenReturn("url");
		Mockito.when(user.getFriendsCount()).thenReturn(1);
		Mockito.when(user.getFollowersCount()).thenReturn(1);
		Mockito.when(user.getLocation()).thenReturn("location");
		Mockito.when(user.getLang()).thenReturn("english");
		
		
		ListsManager listManager = new ListsManager(twitter, ddManager, cRManager, lManager);

		List<String> result = listManager.getUserListSubscribers(sbv);
		assertEquals(result, expected);
	}
	@Test
	public void UserListSubsciptions() throws TsakException, TwitterException, URISyntaxException{
		URI uri = new URI("www.google.com");
		Map<String, Object> expectedMap = new HashMap<String, Object>();
		expectedMap.put("id", 1L);
		expectedMap.put("members_count", 1);
		expectedMap.put("description", "description");
		expectedMap.put("name", "name");
		expectedMap.put("subscribers_count", 1);
		expectedMap.put("slug", "slug");
		expectedMap.put("uri", "www.google.com");
		
		
		
		JSONObject expectedJson = new JSONObject(expectedMap);
		List<String> expected = new ArrayList<String>();
		expected.add(expectedJson.toString());
		int lmts[] = { 15, 1000 };
		
		Mockito.when(cRManager.rateLimitAnalyzer(twitter, lManager,
				LimitsEndPointsVector.LISTS_SUBSCRIPTIONS)).thenReturn(lmts);
		Mockito.when(TwitterCredentials.getuScreenName()).thenReturn("screen name");
		Mockito.when(twitter.getUserListSubscriptions(TwitterCredentials.getuScreenName(), -1L)).thenReturn(lists);
		Mockito.when(userlistIterator.hasNext()).thenReturn(true, false);
		Mockito.when(userlistIterator.next()).thenReturn(userlist);
		Mockito.when(lists.iterator()).thenReturn(userlistIterator);
		
		Mockito.when(userlist.getId()).thenReturn(1L);
		Mockito.when(userlist.getSlug()).thenReturn("slug");
		Mockito.when(userlist.getName()).thenReturn("name");
		Mockito.when(userlist.getMemberCount()).thenReturn(1);
		Mockito.when(userlist.getSubscriberCount()).thenReturn(1);
		Mockito.when(userlist.getDescription()).thenReturn("description");
		Mockito.when(userlist.getURI()).thenReturn(uri);
		
		ListsManager listManager = new ListsManager(twitter, ddManager, cRManager, lManager);

		List<String> result = listManager.UserListSubsciptions();
		
		assertEquals(result, expected);
	}
}
