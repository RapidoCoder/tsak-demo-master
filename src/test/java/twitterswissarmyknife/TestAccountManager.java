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

import twitter4j.AccountSettings;
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
@PrepareForTest({ controlVectors.class, TwitterCredentials.class, Paging.class })
public class TestAccountManager {

	@Mock
	Twitter twitter;
	DDManager ddManager;
	CrManager cRManager;
	LimitsManager lManager;
	LimitsEndPointsVector limitVecotr;
	// Location location;
	PagableResponseList<UserList> lists;
	IDs ids;

	AccountSettings settings;
	SavedSearch savedSearch;
	ResponseList<SavedSearch> savedSearches;
	Iterator<SavedSearch> savedSearchIterator;

	Status status;
	ResponseList<Status> statuses;
	Iterator<Status> statusIterator;

	
	List<Map<String, Object>> maplists_return;
	List<Object> objlist_return;
	Map<String, Object> mapObj_return;

	UserList list;

	Iterator<UserList> userlistIterator;

	PagableResponseList<User> users;
	User tuser;
	Iterator<User> userIterator;
	Paging page;

	@Before
	public void setup() throws Exception {
		list = Mockito.mock(UserList.class);

		lists = Mockito.mock(PagableResponseList.class);
		userlistIterator = Mockito.mock(Iterator.class);

		twitter = Mockito.mock(Twitter.class);
		ddManager = Mockito.mock(DDManager.class);
		cRManager = Mockito.mock(CrManager.class);
		lManager = Mockito.mock(LimitsManager.class);
		settings = Mockito.mock(AccountSettings.class);
		PowerMockito.mockStatic(TwitterCredentials.class);
		ids = Mockito.mock(IDs.class);
		lists = Mockito.mock(PagableResponseList.class);
		users = Mockito.mock(PagableResponseList.class);
		savedSearches = Mockito.mock(ResponseList.class);

		savedSearch = Mockito.mock(SavedSearch.class);
		savedSearchIterator = Mockito.mock(Iterator.class);
		statuses = Mockito.mock(ResponseList.class);
		
		maplists_return = Mockito.mock(List.class);
		// locations = Mockito.mock(Location[].class);

		tuser = Mockito.mock(User.class);
		users = Mockito.mock(PagableResponseList.class);
		userIterator = Mockito.mock(Iterator.class);
		status = Mockito.mock(Status.class);
		statusIterator = Mockito.mock(Iterator.class);
		objlist_return = Mockito.mock(List.class);
		mapObj_return = Mockito.mock(Map.class);

		
	PowerMockito.whenNew(Paging.class).withArguments(1, 200)
		.thenReturn(page);
	page = Mockito.mock(Paging.class);

	}

	@Test
	public void userListMemberships() throws Exception {

		int lmts[] = { 15, 1000 };

		Mockito.when(TwitterCredentials.getuID()).thenReturn("123");

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
		// Twitter twtr, DDManager ddm, CrManager crm, LimitsManager lmg
		Mockito.when(userlistIterator.hasNext()).thenReturn(true, false);

		Mockito.when(userlistIterator.next()).thenReturn(list);
		Mockito.when(lists.iterator()).thenReturn(userlistIterator);
		Mockito.when(list.getId()).thenReturn(1L);
		Mockito.when(list.getSlug()).thenReturn("get slug");
		Mockito.when(list.getName()).thenReturn("some name");
		Mockito.when(list.getMemberCount()).thenReturn(1);
		Mockito.when(list.getSubscriberCount()).thenReturn(1);
		Mockito.when(list.getDescription()).thenReturn("some description");
		// Mockito.when(list.getURI()).thenReturn(/RodResearch/lists/global-citizen-main-stage);
		AccountManager accountManager = new AccountManager(twitter, ddManager,
				cRManager, lManager,page);

		Map<String, Object> expextedMap = new HashMap<String, Object>();
		expextedMap.put("id", 1L);
		expextedMap.put("description", "some description");
		expextedMap.put("name", "some name");
		expextedMap.put("subscribers_count", 1);
		expextedMap.put("slug", "get slug");
		expextedMap.put("members_count", 1);
		expextedMap.put("uri", null);
		List<Map<String, Object>> expected = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> result = accountManager.userListMemberships();

		expected.add(expextedMap);
		assertEquals(expected, result);

	}

	@Test
	public void getMutesIds() throws TsakException, TwitterException {
		int lmts[] = { 15, 1000 };
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
				cRManager, lManager,page);
		List<Object> result = accountManager.getMutesIds();

		assertEquals(result.get(0), expected.get(0));

	}

	@Test
	public void getMutesLists() throws Exception {
		int lmts[] = { 15, 1000 };
		Mockito.when(
				cRManager.rateLimitAnalyzer(twitter, lManager,
						LimitsEndPointsVector.MUTES_USERS_LIST)).thenReturn(
				lmts);
		Mockito.when(twitter.getMutesList(-1L)).thenReturn(users);
		Mockito.when(userIterator.hasNext()).thenReturn(true, false);
		Mockito.when(userIterator.next()).thenReturn(tuser).thenReturn(tuser);
		Mockito.when(users.iterator()).thenReturn(userIterator);
		Mockito.when(tuser.getScreenName()).thenReturn("screen_name");
		Mockito.when(tuser.getName()).thenReturn("name");
		Mockito.when(tuser.getId()).thenReturn(1L);
		Mockito.when(tuser.getBiggerProfileImageURL()).thenReturn(
				"profile image url");
		Mockito.when(tuser.getFriendsCount()).thenReturn(1);
		Mockito.when(tuser.getFollowersCount()).thenReturn(1);
		Mockito.when(tuser.getLocation()).thenReturn("location");
		Mockito.when(tuser.getLang()).thenReturn("get lang");

		List<Object> expected = new ArrayList<Object>();
		Map<String, Object> expectedMap = new HashMap<String, Object>();
		expectedMap.put("id", 1L);
		expectedMap.put("location", "location");
		expectedMap.put("profile_image", "profile image url");
		expectedMap.put("friends_count", 1);

		expectedMap.put("name", "name");
		expectedMap.put("screen_name", "screen_name");
		expectedMap.put("language", "get lang");
		expectedMap.put("followers_count", 1);
		expected.add(expectedMap);

		AccountManager accountManager = new AccountManager(twitter, ddManager,
				cRManager, lManager,page);
		List<Map<String, Object>> result = accountManager.getMutesLists();
		assertEquals(result, expected);
	}

	// @Test
	// public void getAccountSettingsJson() throws TwitterException,
	// TsakException{
	// int lmts[] = { 15, 1000 };
	// Map<String, Object> expected = new HashMap<String, Object>();
	// List<String> loc = new ArrayList<String>();
	//
	// expected.put("time_zone", null);
	// expected.put("access_level", 1L);
	// expected.put("locations" ,locations);
	// expected.put("geo_enabled", true);
	// expected.put("sleep_end_enabled", 100);
	// expected.put("screen_name", "screen_name");
	// expected.put("language", "english");
	// expected.put(" email_discoverable", true);
	// expected.put(" uses_https_always", true);
	// expected.put(" sleep_start_enabled", 100);
	// expected.put(" sleep_time_enabled", false);
	// twitter4j.TimeZone timeZone = settings.getTimeZone();
	//
	// Mockito.when(cRManager.rateLimitAnalyzer(twitter, lManager,
	// LimitsEndPointsVector.ACCOUNT_SETTINGS)).thenReturn(lmts);
	// Mockito.when(twitter.getAccountSettings()).thenReturn(settings);
	//
	// Mockito.when(settings.getScreenName()).thenReturn("screen_name");
	// Mockito.when(settings.isSleepTimeEnabled()).thenReturn(false);
	// Mockito.when(settings.getSleepEndTime()).thenReturn("100");
	// Mockito.when(settings.getSleepStartTime()).thenReturn("100");
	// Mockito.when( settings.isGeoEnabled()).thenReturn(true);
	// Mockito.when(settings.isDiscoverableByEmail()).thenReturn(true);
	// Mockito.when(settings.isAlwaysUseHttps()).thenReturn(true);
	// Mockito.when(settings.getAccessLevel()).thenReturn(1);
	// Mockito.when(settings.getLanguage()).thenReturn("english");
	//
	//
	// Mockito.when(settings.getTrendLocations()).thenReturn(locations);
	// // Mockito.when(location.getName()).thenReturn("some location");
	// // Mockito.when( settings.getTimeZone()).thenReturn( timeZone);
	//
	// AccountManager accountManager = new AccountManager(twitter, ddManager ,
	// cRManager, lManager);
	// Map<String, Object> result = accountManager.getAccountSettingsJson();
	// //assertEquals(result, expected);
	// System.out.println(result);
	// }

	@Test
	public void SavedSearches() throws TwitterException, TsakException {
		int lmts[] = { 15, 1000 };
		Mockito.when(
				cRManager.rateLimitAnalyzer(twitter, lManager,
						LimitsEndPointsVector.SAVED_SEARCHES_LIST)).thenReturn(
				lmts);
		Mockito.when(twitter.getSavedSearches()).thenReturn(savedSearches);
		Mockito.when(savedSearchIterator.hasNext()).thenReturn(true, false);
		Mockito.when(savedSearchIterator.next()).thenReturn(savedSearch);
		Mockito.when(savedSearches.iterator()).thenReturn(savedSearchIterator);

		Mockito.when(savedSearch.getId()).thenReturn(1);
		Mockito.when(savedSearch.getName()).thenReturn("some name");
		Mockito.when(savedSearch.getQuery()).thenReturn("some query");
		Mockito.when(savedSearch.getPosition()).thenReturn(9);
		StringBuilder expected = new StringBuilder();
		expected.append("{");
		expected.append("\"id\":\"" + 1 + "\",");
		expected.append("\"name\":\"" + "some name" + "\",");
		expected.append("\"query\":\"" + "some query" + "\",");
		expected.append("\"position\":" + 9);
		expected.append("}");
		List<String> expectedResult = new ArrayList<String>();
		expectedResult.add(expected.toString());
		AccountManager accountManager = new AccountManager(twitter, ddManager,
				cRManager, lManager,page);
		List<Object> result = accountManager.SavedSearches();
		assertEquals(result.get(0), expectedResult.get(0));
	}

	@Test
	public void getFavourities() throws Exception {
		
		int lmts[] = { 15, 1000 };
		List<Map<String, Object>> expected = new ArrayList<Map<String, Object>>();
		Map<String, Object> expectedMap = new HashMap<String, Object>();
		expectedMap.put("name", "some user name");
		expectedMap.put("tweet", "some tweet");
		expectedMap.put("screen_name", "some screen_name");
		expected.add(expectedMap);
		Mockito.when(
				cRManager.rateLimitAnalyzer(twitter, lManager,
						LimitsEndPointsVector.FAVORITES_LIST)).thenReturn(lmts);
	
		Mockito.when(twitter.getFavorites(page)).thenReturn(statuses);

		Mockito.when(statusIterator.hasNext()).thenReturn(true, false);
		Mockito.when(statusIterator.next()).thenReturn(status);
		Mockito.when(statuses.iterator()).thenReturn(statusIterator);
		Mockito.when(status.getUser()).thenReturn(tuser);
	    Mockito.when(tuser.getName()).thenReturn("some user name");
		 Mockito.when(tuser.getScreenName()).thenReturn("some screen_name");
		Mockito.when(status.getText()).thenReturn("some tweet");
		AccountManager accountManager = new AccountManager(twitter, ddManager,
				cRManager, lManager,page);
		List<Map<String, Object>> result = accountManager.getFavourities();
		assertEquals(result.get(0), expected.get(0));

	}
	@Test
	public void getBlockList() throws TsakException, TwitterException{
		int lmts[] = { 15, 1000 };
		List<Object> expected = new ArrayList<Object>();
		expected.add("1\t@some screen name");
		Mockito.when(
				lManager.getRemainingLimitSpecific(twitter,
						LimitsEndPointsVector.BLOCKS_LIST,
						LimitCheckVector.LIMIT_REMAINING)).thenReturn(15);
		Mockito.when(
				lManager.getRemainingLimitSpecific(twitter,
						LimitsEndPointsVector.BLOCKS_LIST,
						LimitCheckVector.TIME_SECONDS_UNTIL_RESET)).thenReturn(15);
		 Mockito.when(twitter.getBlocksList()).thenReturn(users);
		 
			Mockito.when(userIterator.hasNext()).thenReturn(true, false);
			Mockito.when(userIterator.next()).thenReturn(tuser).thenReturn(tuser);
			Mockito.when(users.iterator()).thenReturn(userIterator);
			Mockito.when(tuser.getId()).thenReturn(1L);
			Mockito.when(tuser.getScreenName()).thenReturn("some screen name");
		
		AccountManager accountManager = new AccountManager(twitter, ddManager,
				cRManager, lManager,page);
		List<Object> result = accountManager.getBlockList();
		assertEquals(result, expected);
		
		
	}
}
