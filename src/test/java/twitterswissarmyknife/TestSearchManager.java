package twitterswissarmyknife;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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
import dto.CrManager;
import dto.DDManager;
import dto.TsakException;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ SearchManager.class, TwitterCredentials.class })
public class TestSearchManager {
	int lmts[] = { 10, 500 };
	
	@Mock
	CrManager cRManager;
	Twitter twitter;
	LimitsManager lManager;
	DDManager ddManager;
	Iterator<User> userIterator;
	Iterator<Status> tweetIterator;
	ResponseList<User> users;
	User user;
	Status tweet;
	QueryResult results;
	List<Status> tweets;

	@SuppressWarnings("unchecked")
	@Before
	public void setup() {
		cRManager = Mockito.mock(CrManager.class);
		twitter = Mockito.mock(Twitter.class);
		ddManager = Mockito.mock(DDManager.class);
		lManager = Mockito.mock(LimitsManager.class);
		PowerMockito.mockStatic(TwitterCredentials.class);
		users = Mockito.mock(ResponseList.class);
		userIterator = Mockito.mock(Iterator.class);
		user = Mockito.mock(User.class);
		results = Mockito.mock(QueryResult.class);
		tweets = Mockito.mock(List.class);
		tweetIterator = Mockito.mock(Iterator.class);
		tweet = Mockito.mock(Status.class);
	}

	@Test
	public void searchUsers() throws TwitterException, TsakException {

		Map<String, Object> expectedMap = new HashMap<String, Object>();
		expectedMap.put("id", 1010101L);
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

		Mockito.when(
				cRManager.rateLimitAnalyzer(twitter, lManager,
						LimitsEndPointsVector.USERS_SEARCH)).thenReturn(lmts);
		
		Mockito.when(twitter.searchUsers("JhonSmith", -1)).thenReturn(users);
		Mockito.when(userIterator.hasNext()).thenReturn(true, false);
		Mockito.when(userIterator.next()).thenReturn(user);
		Mockito.when(users.iterator()).thenReturn(userIterator);

		Mockito.when(user.getScreenName()).thenReturn("JhonSmith");
		Mockito.when(user.getName()).thenReturn("Jhon Smith");
		Mockito.when(user.getId()).thenReturn(1010101L);
		Mockito.when(user.getBiggerProfileImageURL()).thenReturn(
				"http://someImageUrl/image.jpg");
		Mockito.when(user.getFriendsCount()).thenReturn(10);
		Mockito.when(user.getFollowersCount()).thenReturn(20);
		Mockito.when(user.getLocation()).thenReturn("London");
		Mockito.when(user.getLang()).thenReturn("english");

		SearchManager searcManager = new SearchManager(twitter, ddManager,
				cRManager, lManager);
		List<String> result = searcManager.searchUsers("JhonSmith");
		assertEquals(result, expected);
	}

	@Test
	public void searchTweets() throws TwitterException, TsakException {

		Map<String, String> expectedMap = new HashMap<String, String>();
		Map<String, Object> expected = new HashMap<String, Object>();

		expectedMap.put("Name", "Larry Page");
		expectedMap.put("Screen_name", "LarryPage");
		expectedMap.put("ID", "1010101");
		expectedMap.put("Location", "America");
		expectedMap.put("imageURL", "http://someImageUrl/image.jpg");
		expectedMap.put("URL", "http://someurl.com");
		expectedMap.put("FriendsCount", "10");
		expectedMap.put("FriendsCount", "20");

		expected.put("user", expectedMap);
		expected.put("tweet",
				"In August, Larry Page and Sergey Brin announced their plans to reengineer");
		JSONObject json = new JSONObject(expected);
		List<String> expectedlist = new ArrayList<String>();
		expectedlist.add(json.toString());
		Mockito.when(
				cRManager.rateLimitAnalyzer(twitter, lManager,
						LimitsEndPointsVector.SEARCH_TWEETS)).thenReturn(lmts);

		Query query = new Query("Larry Page");
		Mockito.when(twitter.search(query)).thenReturn(results);
		Mockito.when(results.getTweets()).thenReturn(tweets);

		Mockito.when(tweetIterator.hasNext()).thenReturn(true, false);
		Mockito.when(tweetIterator.next()).thenReturn(tweet);
		Mockito.when(tweets.iterator()).thenReturn(tweetIterator);
		Mockito.when(tweet.getUser()).thenReturn(user);

		Mockito.when(user.getName()).thenReturn("Larry Page");
		Mockito.when(user.getScreenName()).thenReturn("LarryPage");
		Mockito.when(user.getId()).thenReturn(1010101L);
		Mockito.when(user.getLocation()).thenReturn("America");
		Mockito.when(user.getOriginalProfileImageURL()).thenReturn(
				"http://someImageUrl/image.jpg");
		Mockito.when(user.getURL()).thenReturn("http://someurl.com");
		Mockito.when(user.getFriendsCount()).thenReturn(10);
		Mockito.when(user.getFollowersCount()).thenReturn(20);
		Mockito.when(tweet.getText())
				.thenReturn(
						"In August, Larry Page and Sergey Brin announced their plans to reengineer");

		SearchManager searcManager = new SearchManager(twitter, ddManager,
				cRManager, lManager);
		List<String> result = searcManager.searchTweets("Larry Page");
		assertEquals(result, expectedlist);
	}
}
