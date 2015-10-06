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

import twitter4j.IDs;
import twitter4j.Paging;
import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.User;
import twitterhandler.LimitsManager;
import twitterhandler.StatusesManager;
import twitterhandler.TwitterCredentials;
import dto.ControlHandlers.LimitsEndPointsVector;
import dto.ControlHandlers.subCmdUpVector;
import dto.CrManager;
import dto.DDManager;
import dto.TsakException;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ StatusesManager.class, TwitterCredentials.class, Paging.class })
public class TestStatusManager {
	int lmts[] = { 10, 500 };

	@Mock
	CrManager cRManager;
	Twitter twitter;
	LimitsManager lManager;
	DDManager ddManager;
	Status status;
	ResponseList<Status> statuses;
	Iterator<Status> statusIterator;
	User user;
	IDs ids;
	Paging page;

	@SuppressWarnings("unchecked")
	@Before
	public void setup() {
		cRManager = Mockito.mock(CrManager.class);
		twitter = Mockito.mock(Twitter.class);
		lManager = Mockito.mock(LimitsManager.class);
		ddManager = Mockito.mock(DDManager.class);
		status = Mockito.mock(Status.class);
		PowerMockito.mockStatic(TwitterCredentials.class);
		user = Mockito.mock(User.class);
		ids = Mockito.mock(IDs.class);
		statuses = Mockito.mock(ResponseList.class);
		statusIterator = Mockito.mock(Iterator.class);
		page = Mockito.mock(Paging.class);
	}

	@Test
	public void getStatusById() throws TsakException, NumberFormatException,
			TwitterException {
		List<String> expected = new ArrayList<String>();
		expected.add(1010101L
				+ "\t JhonSmith"
				+ "\t BigDataDiary brings you latest news on BigData, NoSQL along with updates on relevant products and services.");

		String sID = "303030";
		Mockito.when(
				cRManager.rateLimitAnalyzer(twitter, lManager,
						LimitsEndPointsVector.STATUSES_SHOW_ID)).thenReturn(
				lmts);
		Mockito.when(twitter.showStatus(Long.parseLong(sID)))
				.thenReturn(status);
		Mockito.when(status.getId()).thenReturn(1010101L);
		Mockito.when(status.getUser()).thenReturn(user);
		Mockito.when(user.getScreenName()).thenReturn(" JhonSmith");
		Mockito.when(status.getText())
				.thenReturn(
						" BigDataDiary brings you latest news on BigData, NoSQL along with updates on relevant products and services.");
		StatusesManager statusManager = new StatusesManager(twitter, ddManager,
				cRManager, lManager);
		List<String> result = statusManager.getStatusById(sID);
		assertEquals(result, expected);
	}

	@Test
	public void getStatusRetweeters() throws NumberFormatException,
			TwitterException, TsakException {
		List<String> expected = new ArrayList<String>();
		expected.add(String.valueOf(1));

		String sID = "303030";
		Mockito.when(
				cRManager.rateLimitAnalyzer(twitter, lManager,
						LimitsEndPointsVector.STATUSES_RETWEETERS_IDS))
				.thenReturn(lmts);
		Mockito.when(twitter.getRetweeterIds(Long.parseLong(sID), -1L))
				.thenReturn(ids);
		Mockito.when(ids.getIDs()).thenReturn(new long[] { 1 });
		StatusesManager statusManager = new StatusesManager(twitter, ddManager,
				cRManager, lManager);
		List<String> result = statusManager.getStatusRetweeters(sID);
		assertEquals(result, expected);
	}

	@Test
	public void getMentionsTimeline() throws Exception {

		PowerMockito.whenNew(Paging.class).withArguments(1, 200)
				.thenReturn(page);
		Map<String, Object> expectedMap = new HashMap<String, Object>();
		Map<String, Object> tweet = new HashMap<String, Object>();
		Map<String, Object> userMap = new HashMap<String, Object>();
		List<String> expected = new ArrayList<String>();
		tweet.put("id", 1010101L);
		tweet.put("in_reply_to_screenname", "michael");
		tweet.put("text", "what about your new movie?");
		tweet.put("in_reply_to_status", 2);
		tweet.put("in_reply_to_user", 3);

		userMap.put("id", 1010101L);
		userMap.put("location", "London");
		userMap.put("profile_image", "http://someImageUrl/image.jpg");
		userMap.put("friends_count", 10);
		userMap.put("name", "Jhon Smith");
		userMap.put("screen_name", "JhonSmith");
		userMap.put("language", "english");
		userMap.put("followers_count", 10);

		expectedMap.put("user", userMap);
		expectedMap.put("tweet", tweet);

		JSONObject jObj = new JSONObject(expectedMap);
		String expectedString = jObj.toString();
		expected.add(expectedString);

		Mockito.when(
				cRManager.rateLimitAnalyzer(twitter, lManager,
						LimitsEndPointsVector.STATUSES_MENTIONS_TIMELINE))
				.thenReturn(lmts);
		Mockito.when(twitter.getMentionsTimeline(page)).thenReturn(statuses);

		Mockito.when(statusIterator.hasNext()).thenReturn(true, false);
		Mockito.when(statusIterator.next()).thenReturn(status);
		Mockito.when(statuses.iterator()).thenReturn(statusIterator);

		Mockito.when(status.getId()).thenReturn(1010101L);
		Mockito.when(status.getText()).thenReturn("what about your new movie?");
		Mockito.when(status.getInReplyToScreenName()).thenReturn("michael");
		Mockito.when(status.getInReplyToStatusId()).thenReturn(2L);
		Mockito.when(status.getInReplyToUserId()).thenReturn(3L);
		Mockito.when(status.getUser()).thenReturn(user);

		Mockito.when(user.getScreenName()).thenReturn("JhonSmith");
		Mockito.when(user.getName()).thenReturn("Jhon Smith");
		Mockito.when(user.getId()).thenReturn(1010101L);
		Mockito.when(user.getMiniProfileImageURL()).thenReturn(
				"http://someImageUrl/image.jpg");
		Mockito.when(user.getFriendsCount()).thenReturn(10);
		Mockito.when(user.getFollowersCount()).thenReturn(10);
		Mockito.when(user.getLocation()).thenReturn("London");
		Mockito.when(user.getLang()).thenReturn("english");

		StatusesManager statusManager = new StatusesManager(twitter, ddManager,
				cRManager, lManager);
		List<String> result = statusManager.getMentionsTimeline();
		assertEquals(result, expected);
	}

	@Test
	public void userTimeLineHOME_TIMELINE() throws Exception {
		PowerMockito.whenNew(Paging.class).withArguments(1, 200)
				.thenReturn(page);
		List<String> expected = new ArrayList<String>();
		expected.add(202020L + "\tGoogle was founded by Larry Page and Sergey Brin while they were Ph.D. students at Stanford University.");

		subCmdUpVector sbv = subCmdUpVector.HOME_TIMELINE;
		Mockito.when(
				cRManager.rateLimitAnalyzer(twitter, lManager,
						LimitsEndPointsVector.STATUSES_HOME_TIMELINE))
				.thenReturn(lmts);
		Mockito.when(twitter.getHomeTimeline(page)).thenReturn(statuses);
		Mockito.when(statusIterator.hasNext()).thenReturn(true, false);
		Mockito.when(statusIterator.next()).thenReturn(status);
		Mockito.when(statuses.iterator()).thenReturn(statusIterator);

		Mockito.when(status.getId()).thenReturn(202020L);
		Mockito.when(status.getText())
				.thenReturn(
						"Google was founded by Larry Page and Sergey Brin while they were Ph.D. students at Stanford University.");
		StatusesManager statusManager = new StatusesManager(twitter, ddManager,
				cRManager, lManager);
		List<String> result = statusManager.userTimeLine("JhonSmith", sbv);
		assertEquals(result, expected);
	}

	@Test
	public void userTimeLineUSER_TIMELINE() throws Exception {
		PowerMockito.whenNew(Paging.class).withArguments(1, 200)
				.thenReturn(page);
		List<String> expected = new ArrayList<String>();
		expected.add("Enhance Big Data Performance With Latest Version Of Talend.");

		subCmdUpVector sbv = subCmdUpVector.USER_TIMELINE;
		Mockito.when(
				cRManager.rateLimitAnalyzer(twitter, lManager,
						LimitsEndPointsVector.STATUSES_USER_TIMELINE))
				.thenReturn(lmts);
		Mockito.when(TwitterCredentials.getuID()).thenReturn("1010101");
		Mockito.when(
				twitter.getUserTimeline(
						Long.parseLong(TwitterCredentials.getuID()), page))
				.thenReturn(statuses);
		Mockito.when(statusIterator.hasNext()).thenReturn(true, false);
		Mockito.when(statusIterator.next()).thenReturn(status);
		Mockito.when(statuses.iterator()).thenReturn(statusIterator);
		Mockito.when(status.getText()).thenReturn(
				"Enhance Big Data Performance With Latest Version Of Talend.");

		StatusesManager statusManager = new StatusesManager(twitter, ddManager,
				cRManager, lManager);
		List<String> result = statusManager.userTimeLine("JhonSmith", sbv);
		assertEquals(result, expected);
	}

	@Test
	public void userTimeLineOWN_RETWEETS() throws Exception {
		PowerMockito.whenNew(Paging.class).withArguments(1, 100)
				.thenReturn(page);
		List<String> expected = new ArrayList<String>();
		expected.add("Enhance Big Data Performance With Latest Version Of Talend.");
		String tuser = "1010101";

		subCmdUpVector sbv = subCmdUpVector.OWN_RETWEETS;
		Mockito.when(
				cRManager.rateLimitAnalyzer(twitter, lManager,
						LimitsEndPointsVector.STATUSES_RETWEETS_OF_ME))
				.thenReturn(lmts);
		Mockito.when(twitter.getRetweetsOfMe(page)).thenReturn(statuses);

		Mockito.when(statusIterator.hasNext()).thenReturn(true, false);
		Mockito.when(statusIterator.next()).thenReturn(status);
		Mockito.when(statuses.iterator()).thenReturn(statusIterator);
		Mockito.when(status.getText()).thenReturn(
				"Enhance Big Data Performance With Latest Version Of Talend.");
		
		StatusesManager statusManager = new StatusesManager(twitter, ddManager,
				cRManager, lManager);
		List<String> result = statusManager.userTimeLine(tuser, sbv);
		assertEquals(result, expected);
	}
}
