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

import twitter4j.IDs;
import twitter4j.PagableResponseList;
import twitter4j.Paging;
import twitter4j.ResponseList;
import twitter4j.SavedSearch;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.User;
import twitter4j.UserList;
import twitterhandler.AccountManager;
import twitterhandler.LimitsManager;
import twitterhandler.TwitterCredentials;
import dto.ControlHandlers.LimitCheckVector;
import dto.ControlHandlers.LimitsEndPointsVector;
import dto.ControlHandlers.controlVectors;
import dto.CrManager;
import dto.DDManager;
import dto.TsakException;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ controlVectors.class, TwitterCredentials.class })
public class TestAccountManager {
	int lmts[] = { 10, 500 };
	
	@Mock
	Twitter twitter;
	DDManager ddManager;
	CrManager cRManager;
	LimitsManager lManager;

	UserList list;
	Status status;
	User user;
	IDs ids;
	PagableResponseList<UserList> lists;
	PagableResponseList<User> users;
	ResponseList<Status> statuses;
	ResponseList<SavedSearch> savedSearches;
	Iterator<UserList> userlistIterator;
	Iterator<Status> statusIterator;
	Iterator<SavedSearch> savedSearchIterator;
	Iterator<User> userIterator;
	SavedSearch savedSearch;

	@SuppressWarnings("unchecked")
	@Before
	public void setup() throws Exception {
		twitter = Mockito.mock(Twitter.class);
		ddManager = Mockito.mock(DDManager.class);
		cRManager = Mockito.mock(CrManager.class);
		lManager = Mockito.mock(LimitsManager.class);
		PowerMockito.mockStatic(TwitterCredentials.class);

		list = Mockito.mock(UserList.class);
		lists = Mockito.mock(PagableResponseList.class);
		userlistIterator = Mockito.mock(Iterator.class);
		ids = Mockito.mock(IDs.class);
		users = Mockito.mock(PagableResponseList.class);
		savedSearches = Mockito.mock(ResponseList.class);
		savedSearch = Mockito.mock(SavedSearch.class);
		savedSearchIterator = Mockito.mock(Iterator.class);
		statuses = Mockito.mock(ResponseList.class);
		user = Mockito.mock(User.class);
		userIterator = Mockito.mock(Iterator.class);
		status = Mockito.mock(Status.class);
		statusIterator = Mockito.mock(Iterator.class);
	}

	@Test
	public void userListMemberships() throws Exception {

		Map<String, Object> expextedMap = new HashMap<String, Object>();
		expextedMap.put("id", 1010101L);
		expextedMap.put("description", "List description");
		expextedMap.put("name", "Jhon smith");
		expextedMap.put("subscribers_count", 10);
		expextedMap.put("slug", "global-education");
		expextedMap.put("members_count", 10);
		expextedMap.put("uri", null);
		List<Map<String, Object>> expected = new ArrayList<Map<String, Object>>();
		expected.add(expextedMap);

		Mockito.when(TwitterCredentials.getuID()).thenReturn("1010101");
		Mockito.when(
				twitter.getUserListMemberships(
						Long.parseLong(TwitterCredentials.getuID()), -1L))
				.thenReturn(lists);
		Mockito.when(
				cRManager.rateLimitAnalyzer(twitter, lManager,
						LimitsEndPointsVector.LISTS_MEMBERSHIPS)).thenReturn(
				lmts);
		Mockito.when(
				twitter.getUserListMemberships(
						TwitterCredentials.getuScreenName(), -1L)).thenReturn(
				lists);
		Mockito.when(userlistIterator.hasNext()).thenReturn(true, false);
		Mockito.when(userlistIterator.next()).thenReturn(list);
		Mockito.when(lists.iterator()).thenReturn(userlistIterator);
		Mockito.when(list.getId()).thenReturn(1010101L);
		Mockito.when(list.getSlug()).thenReturn("global-education");
		Mockito.when(list.getName()).thenReturn("Jhon smith");
		Mockito.when(list.getMemberCount()).thenReturn(10);
		Mockito.when(list.getSubscriberCount()).thenReturn(10);
		Mockito.when(list.getDescription()).thenReturn("List description");
		Mockito.when(list.getURI()).thenReturn(null);
		AccountManager accountManager = new AccountManager(twitter, ddManager,
				cRManager, lManager);

		List<Map<String, Object>> result = accountManager.userListMemberships();
		assertEquals(expected, result);
	}

	@Test
	public void getMutesIds() throws TsakException, TwitterException {

		long[] idsValue = { 2L };
		List<Object> expected = new ArrayList<Object>();
		expected.add(String.valueOf(idsValue[0]));
		Mockito.when(
				cRManager.rateLimitAnalyzer(twitter, lManager,
						LimitsEndPointsVector.MUTES_USERS_IDS))
				.thenReturn(lmts);
		Mockito.when(twitter.getMutesIDs(-1L)).thenReturn(ids);
		Mockito.when(ids.getIDs()).thenReturn(idsValue);
		Mockito.when(ids.getNextCursor()).thenReturn(15L);
		AccountManager accountManager = new AccountManager(twitter, ddManager,
				cRManager, lManager);
		List<Object> result = accountManager.getMutesIds();
		assertEquals(result.get(0), expected.get(0));
	}

	@Test
	public void getMutesLists() throws Exception {

		List<Object> expected = new ArrayList<Object>();
		Map<String, Object> expectedMap = new HashMap<String, Object>();
		expectedMap.put("id", 1010101L);
		expectedMap.put("location", "London");
		expectedMap.put("profile_image", "http://someImageUrl/image.jpg");
		expectedMap.put("friends_count", 10);

		expectedMap.put("name", "Jhon Smith");
		expectedMap.put("screen_name", "JhonSmith");
		expectedMap.put("language", "english");
		expectedMap.put("followers_count", 10);
		expected.add(expectedMap);

		Mockito.when(
				cRManager.rateLimitAnalyzer(twitter, lManager,
						LimitsEndPointsVector.MUTES_USERS_LIST)).thenReturn(
				lmts);
		Mockito.when(twitter.getMutesList(-1L)).thenReturn(users);
		Mockito.when(userIterator.hasNext()).thenReturn(true, false);
		Mockito.when(userIterator.next()).thenReturn(user);
		Mockito.when(users.iterator()).thenReturn(userIterator);
		Mockito.when(user.getScreenName()).thenReturn("JhonSmith");
		Mockito.when(user.getName()).thenReturn("Jhon Smith");
		Mockito.when(user.getId()).thenReturn(1010101L);
		Mockito.when(user.getBiggerProfileImageURL()).thenReturn(
				"http://someImageUrl/image.jpg");
		Mockito.when(user.getFriendsCount()).thenReturn(10);
		Mockito.when(user.getFollowersCount()).thenReturn(10);
		Mockito.when(user.getLocation()).thenReturn("London");
		Mockito.when(user.getLang()).thenReturn("english");

		AccountManager accountManager = new AccountManager(twitter, ddManager,
				cRManager, lManager);
		List<Map<String, Object>> result = accountManager.getMutesLists();
		assertEquals(result, expected);
	}

	@Test
	public void SavedSearches() throws TwitterException, TsakException {

		StringBuilder expected = new StringBuilder();
		expected.append("{");
		expected.append("\"id\":\"" + 1010101 + "\",");
		expected.append("\"name\":\"" + "Jhon Smith" + "\",");
		expected.append("\"query\":\"" + "Emerging technologies" + "\",");
		expected.append("\"position\":" + 5);
		expected.append("}");
		List<String> expectedResult = new ArrayList<String>();
		expectedResult.add(expected.toString());

		Mockito.when(
				cRManager.rateLimitAnalyzer(twitter, lManager,
						LimitsEndPointsVector.SAVED_SEARCHES_LIST)).thenReturn(
				lmts);
		Mockito.when(twitter.getSavedSearches()).thenReturn(savedSearches);
		Mockito.when(savedSearchIterator.hasNext()).thenReturn(true, false);
		Mockito.when(savedSearchIterator.next()).thenReturn(savedSearch);
		Mockito.when(savedSearches.iterator()).thenReturn(savedSearchIterator);
		Mockito.when(savedSearch.getId()).thenReturn(1010101);
		Mockito.when(savedSearch.getName()).thenReturn("Jhon Smith");
		Mockito.when(savedSearch.getQuery())
				.thenReturn("Emerging technologies");
		Mockito.when(savedSearch.getPosition()).thenReturn(5);

		AccountManager accountManager = new AccountManager(twitter, ddManager,
				cRManager, lManager);
		List<Object> result = accountManager.SavedSearches();
		assertEquals(result.get(0), expectedResult.get(0));
	}

	@Test
	public void getFavourities() throws Exception {

		List<Map<String, Object>> expected = new ArrayList<Map<String, Object>>();
		Map<String, Object> expectedMap = new HashMap<String, Object>();
		expectedMap.put("name", "Jhon Smith");
		expectedMap.put("tweet", "pandas is the best data analysis toolkit");
		expectedMap.put("screen_name", "JhonSmith");
		expected.add(expectedMap);

		Mockito.when(
				cRManager.rateLimitAnalyzer(twitter, lManager,
						LimitsEndPointsVector.FAVORITES_LIST)).thenReturn(lmts);
		Paging page = new Paging(1, 200);
		Mockito.when(twitter.getFavorites(page)).thenReturn(statuses);
		Mockito.when(status.getUser()).thenReturn(user);
		Mockito.when(statusIterator.hasNext()).thenReturn(true, false);
		Mockito.when(statusIterator.next()).thenReturn(status);
		Mockito.when(statuses.iterator()).thenReturn(statusIterator);
		Mockito.when(user.getName()).thenReturn("Jhon Smith");
		Mockito.when(user.getScreenName()).thenReturn("JhonSmith");
		Mockito.when(status.getText()).thenReturn(
				"pandas is the best data analysis toolkit");
		AccountManager accountManager = new AccountManager(twitter, ddManager,
				cRManager, lManager);
		List<Map<String, Object>> result = accountManager.getFavourities();
		assertEquals(result.get(0), expected.get(0));
	}

	@Test
	public void getBlockList() throws TsakException, TwitterException {

		List<Object> expected = new ArrayList<Object>();
		expected.add(1010101L + "\t@JhonSmith");
		Mockito.when(
				lManager.getRemainingLimitSpecific(twitter,
						LimitsEndPointsVector.BLOCKS_LIST,
						LimitCheckVector.LIMIT_REMAINING)).thenReturn(15);
		Mockito.when(
				lManager.getRemainingLimitSpecific(twitter,
						LimitsEndPointsVector.BLOCKS_LIST,
						LimitCheckVector.TIME_SECONDS_UNTIL_RESET)).thenReturn(
				15);
		Mockito.when(twitter.getBlocksList()).thenReturn(users);
		Mockito.when(userIterator.hasNext()).thenReturn(true, false);
		Mockito.when(userIterator.next()).thenReturn(user);
		Mockito.when(users.iterator()).thenReturn(userIterator);
		Mockito.when(user.getId()).thenReturn(1010101L);
		Mockito.when(user.getScreenName()).thenReturn("JhonSmith");

		AccountManager accountManager = new AccountManager(twitter, ddManager,
				cRManager, lManager);
		List<Object> result = accountManager.getBlockList();
		assertEquals(result, expected);
	}
}
