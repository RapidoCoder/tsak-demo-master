package twitterswissarmyknife;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import jcommander.ArgsManager;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import twitter4j.JSONObject;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.User;
import twitterhandler.LimitsManager;
import twitterhandler.SearchManager;
import twitterhandler.TwitterCredentials;
import dto.ControlHandlers.LimitsEndPointsVector;
import dto.ControlHandlers.controlVectors;
import dto.CrManager;
import dto.DDManager;
import dto.TsakException;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ SearchManager.class, controlVectors.class, TwitterCredentials.class })
public class TestSearchManager {

	@Mock
	CrManager cRManager;
	controlVectors cVectors;
	ArgsManager argManager;
	Twitter twitter;
	LimitsManager lManager;
	DDManager ddManager;

	Iterator<User> userIterator;
	Iterator<Status> tweetIterator;
	ResponseList<User> users;
	User tuser;
	Status tweet;
	QueryResult results;
	List<Status> tweets;

	@SuppressWarnings("unchecked")
	@Before
	public void setup() {
		cRManager = Mockito.mock(CrManager.class);
		cVectors = Mockito.mock(controlVectors.class);
		argManager = Mockito.mock(ArgsManager.class);
		twitter = Mockito.mock(Twitter.class);
		ddManager = Mockito.mock(DDManager.class);
		lManager = Mockito.mock(LimitsManager.class);

		PowerMockito.mockStatic(TwitterCredentials.class);
		users = Mockito.mock(ResponseList.class);
		userIterator = Mockito.mock(Iterator.class);
		tuser = Mockito.mock(User.class);
		results = Mockito.mock(QueryResult.class);
		tweets = Mockito.mock(List.class);
		tweetIterator = Mockito.mock(Iterator.class);
		tweet = Mockito.mock(Status.class);

	}

	@Test
	public void searchUsers() throws TwitterException, TsakException {

		Map<String, Object> expectedMap = new HashMap<String, Object>();
		expectedMap.put("screen_name", "userScreenName");
		expectedMap.put("name", "userName");
		expectedMap.put("id", 1L);
		expectedMap.put("profile_image", "http://someImageUrl.com");
		expectedMap.put("friends_count", 10);
		expectedMap.put("followers_count", 20);
		expectedMap.put("location", "london");
		expectedMap.put("language", "english");
		JSONObject expectedJson = new JSONObject(expectedMap);

		List<String> expected = new ArrayList<String>();
		expected.add(expectedJson.toString());

		int lmts[] = { 10, 500 };

		Mockito.when(
				cRManager.rateLimitAnalyzer(twitter, lManager,
						LimitsEndPointsVector.USERS_SEARCH)).thenReturn(lmts);
		String somename = "someName";

		Mockito.when(twitter.searchUsers(somename, -1)).thenReturn(users);

		Mockito.when(userIterator.hasNext()).thenReturn(true, false);
		Mockito.when(userIterator.next()).thenReturn(tuser);
		Mockito.when(users.iterator()).thenReturn(userIterator);

		Mockito.when(tuser.getScreenName()).thenReturn("userScreenName");
		Mockito.when(tuser.getName()).thenReturn("userName");
		Mockito.when(tuser.getId()).thenReturn(1L);
		Mockito.when(tuser.getBiggerProfileImageURL()).thenReturn(
				"http://someImageUrl.com");
		Mockito.when(tuser.getFriendsCount()).thenReturn(10);
		Mockito.when(tuser.getFollowersCount()).thenReturn(20);
		Mockito.when(tuser.getLocation()).thenReturn("london");
		Mockito.when(tuser.getLang()).thenReturn("english");

		SearchManager searcManager = new SearchManager(twitter, ddManager,
				cRManager, lManager);
		List<String> result = searcManager.searchUsers("someName");
		assertEquals(result, expected);
	}

	@Test
	public void searchTweets() throws TwitterException, TsakException {

		Map<String, String> expectedMap = new HashMap<String, String>();
		Map<String, Object> expected = new HashMap<String, Object>();

		expectedMap.put("Name", "userName");
		expectedMap.put("Screen_name", "userScreenName");
		expectedMap.put("ID", "123");
		expectedMap.put("Location", "london");
		expectedMap.put("imageURL", "http://someImageUrl.com");
		expectedMap.put("URL", "http://someurl.com");
		expectedMap.put("FriendsCount", "10");
		expectedMap.put("FriendsCount", "20");

		expected.put("user", expectedMap);
		expected.put("tweet", "resulted tweet");
		
		JSONObject json = new JSONObject(expected);
		List<String> expectedlist = new ArrayList<String>();
		expectedlist.add(json.toString());
		int lmts[] = { 10, 500 };

		Mockito.when(
				cRManager.rateLimitAnalyzer(twitter, lManager,
						LimitsEndPointsVector.SEARCH_TWEETS)).thenReturn(lmts);
		
		Query query = new Query("somequery");
		Mockito.when(twitter.search(query)).thenReturn(results);
		Mockito.when(results.getTweets()).thenReturn(tweets);
		
		Mockito.when(tweetIterator.hasNext()).thenReturn(true, false);
		Mockito.when(tweetIterator.next()).thenReturn(tweet);
		Mockito.when(tweets.iterator()).thenReturn(tweetIterator);
		Mockito.when(tweet.getUser()).thenReturn(tuser);
		
		Mockito.when(tuser.getName()).thenReturn("userName");
		Mockito.when(tuser.getScreenName()).thenReturn("userScreenName");
		Mockito.when(tuser.getId()).thenReturn(123L);
		Mockito.when(tuser.getLocation()).thenReturn("london");
		Mockito.when(tuser.getOriginalProfileImageURL()).thenReturn("http://someImageUrl.com");
		Mockito.when(tuser.getURL()).thenReturn("http://someurl.com");
		Mockito.when(tuser.getFriendsCount()).thenReturn(10);
		Mockito.when(tuser.getFollowersCount()).thenReturn(20);
		Mockito.when(tweet.getText()).thenReturn("resulted tweet");
		

		SearchManager searcManager = new SearchManager(twitter, ddManager,
				cRManager, lManager);
		List<String> result = searcManager.searchTweets("somequery");
		assertEquals(result, expectedlist);
		
	}
}
