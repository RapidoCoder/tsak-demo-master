package twitterswissarmyknife;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import twitter4j.Category;
import twitter4j.HashtagEntity;
import twitter4j.MediaEntity;
import twitter4j.PagableResponseList;
import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.User;
import twitter4j.UserMentionEntity;
import twitterhandler.LimitsManager;
import twitterhandler.TwitterCredentials;
import twitterhandler.UsersManager;
import dto.ControlHandlers.LimitsEndPointsVector;
import dto.ControlHandlers.controlVectors;
import dto.CrManager;
import dto.DDManager;
import dto.TsakException;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ controlVectors.class, TwitterCredentials.class })
public class TestUsersManager {

	@Mock
	Twitter twitter;
	DDManager ddManager;
	CrManager cRManager;
	LimitsManager lManager;
	Iterator catIterator;
	Status status;
	ResponseList<Category> categories;
	Category category;
	ResponseList<User> users;
	User user;
	Iterator<User> userIterator;
	

	@Before
	public void setup() {
		twitter = Mockito.mock(Twitter.class);
		cRManager = Mockito.mock(CrManager.class);
		lManager = Mockito.mock(LimitsManager.class);
		ddManager = Mockito.mock(DDManager.class);
		PowerMockito.mockStatic(TwitterCredentials.class);

		categories = Mockito.mock(ResponseList.class);
		category = Mockito.mock(Category.class);
		catIterator = Mockito.mock(Iterator.class);
		user = Mockito.mock(User.class);
		users = Mockito.mock(ResponseList.class);
		userIterator = Mockito.mock(Iterator.class);
	}

	@Test
	public void getSuggestedCatagories() throws TwitterException, TsakException {

		List<String> expected = new ArrayList<String>();
		expected.add("Twitter Catagory" + " \t " + "Twitter Catagory slug"
				+ " \t " + 10);

		int lmts[] = { 10, 500 };
		Mockito.when(
				cRManager.rateLimitAnalyzer(twitter, lManager,
						LimitsEndPointsVector.USERS_SUGGESTIONS)).thenReturn(
				lmts);

		Mockito.when(twitter.getSuggestedUserCategories()).thenReturn(categories);
		Mockito.when(catIterator.hasNext()).thenReturn(true, false);
		Mockito.when(catIterator.next()).thenReturn(category);
		Mockito.when(categories.iterator()).thenReturn(catIterator);
		
		Mockito.when(category.getName()).thenReturn("Twitter Catagory");
		Mockito.when(category.getSlug()).thenReturn("Twitter Catagory slug");
		Mockito.when(category.getSize()).thenReturn(10);
		
		UsersManager usersManager = new UsersManager(twitter, ddManager, cRManager, lManager);

		List<String> result = usersManager.getSuggestedCatagories();
		
		assertEquals(result, expected);
	}
	
	@Test
	public void getUserSuggestions() throws TwitterException, TsakException{
		
		Map<String, Object> expectedMap = new HashMap<String, Object>();
		expectedMap.put("screen_name", "jhonSmith");
		expectedMap.put("name", "Jhon Smith");
		expectedMap.put("id", 1L);
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
						LimitsEndPointsVector.USERS_SUGGESTIONS_SLUG)).thenReturn(
				lmts);
		
		Mockito.when(twitter.getUserSuggestions("slug")).thenReturn(users);
		
		Mockito.when(userIterator.hasNext()).thenReturn(true, false);
		Mockito.when(userIterator.next()).thenReturn(user);
		Mockito.when(users.iterator()).thenReturn(userIterator);
		
		Mockito.when(user.getScreenName()).thenReturn("jhonSmith");
		Mockito.when(user.getName()).thenReturn("Jhon Smith");
		Mockito.when(user.getId()).thenReturn(1L);
		Mockito.when(user.getFriendsCount()).thenReturn(10);
		Mockito.when(user.getFollowersCount()).thenReturn(20);
		Mockito.when(user.getLocation()).thenReturn("london");
		Mockito.when(user.getLang()).thenReturn("english");
		
		UsersManager usersManager = new UsersManager(twitter, ddManager, cRManager, lManager);

		List<String> result = usersManager.getUserSuggestions("slug");
		//System.out.println(result);
		assertEquals(result, expected);
	}
	
	@Test
	public void getMemberSuggestions() throws TsakException, TwitterException{
		Map<String, Object> expectedMap = new HashMap<String, Object>();
		expectedMap.put("screen_name", "jhonSmith");
		expectedMap.put("status", "Private User");
		expectedMap.put("name", "Jhon Smith");
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
						LimitsEndPointsVector.USERS_SUGGESTIONS_SLUG_MEMBERS)).thenReturn(
				lmts);
		
		Mockito.when(twitter.getMemberSuggestions("slug")).thenReturn(users);
		
		Mockito.when(userIterator.hasNext()).thenReturn(true, false);
		Mockito.when(userIterator.next()).thenReturn(user);
		Mockito.when(users.iterator()).thenReturn(userIterator);
		
		Mockito.when(user.getScreenName()).thenReturn("jhonSmith");
		Mockito.when(user.getStatus()).thenReturn(status);
		Mockito.when(user.getName()).thenReturn("Jhon Smith");
		Mockito.when(user.getId()).thenReturn(1L);
		Mockito.when(user.getMiniProfileImageURL()).thenReturn("http://someImageUrl.com");
		Mockito.when(user.getFriendsCount()).thenReturn(10);
		Mockito.when(user.getFollowersCount()).thenReturn(20);
		Mockito.when(user.getLocation()).thenReturn("london");
		Mockito.when(user.getLang()).thenReturn("english");
		
		UsersManager usersManager = new UsersManager(twitter, ddManager, cRManager, lManager);

		List<String> result = usersManager.getMemberSuggestions("slug");
		assertEquals(result, expected);
	}
}
