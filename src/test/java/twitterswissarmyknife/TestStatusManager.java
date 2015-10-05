package twitterswissarmyknife;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
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
import dto.ControlHandlers.controlVectors;
import dto.ControlHandlers.subCmdUpVector;
import dto.CrManager;
import dto.DDManager;
import dto.TsakException;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ StatusesManager.class, controlVectors.class, TwitterCredentials.class, Paging.class })
public class TestStatusManager {
	@Mock
	CrManager cRManager;

	ArgsManager argManager;
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

		argManager = Mockito.mock(ArgsManager.class);

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
	public void getStatusById() throws TsakException, NumberFormatException, TwitterException {
		List<String> expected = new ArrayList<String>();
		expected.add( 1L+ "\tScreen Name" + "\ttext");
		int lmts[] = { 15, 1000 };
		String sID = "1";
		Mockito.when(
				cRManager.rateLimitAnalyzer(twitter, lManager,
						LimitsEndPointsVector.STATUSES_SHOW_ID)).thenReturn(
				lmts);
		Mockito.when(twitter.showStatus(Long.parseLong(sID))).thenReturn(status);
		Mockito.when(status.getId()).thenReturn(1L);
		Mockito.when( status.getUser()).thenReturn(user);
		Mockito.when(user.getScreenName()).thenReturn("Screen Name");
		Mockito.when(status.getText()).thenReturn("text");
		StatusesManager statusManager = new StatusesManager(twitter, ddManager,
				cRManager, lManager);
		List<String> result = statusManager.getStatusById(sID);
		assertEquals(result, expected);
	}
	
	@Test
	public void getStatusRetweeters() throws NumberFormatException, TwitterException, TsakException{
		List<String> expected = new ArrayList<String>();
		expected.add(String.valueOf(1));
		int lmts[] = { 15, 1000 };
		String sID = "1";
		Mockito.when(
				cRManager.rateLimitAnalyzer(twitter, lManager,LimitsEndPointsVector.STATUSES_RETWEETERS_IDS)).thenReturn(
				lmts);
		Mockito.when(twitter.getRetweeterIds(Long.parseLong(sID), -1L)).thenReturn(ids);
		Mockito.when(ids.getIDs()).thenReturn(new long[]{1});
		StatusesManager statusManager = new StatusesManager(twitter, ddManager,
				cRManager, lManager);
		List<String> result = statusManager.getStatusRetweeters(sID);
		assertEquals(result, expected);
	}
	
	@Test
	public void getMentionsTimeline() throws Exception{
		
		PowerMockito.whenNew(Paging.class).withArguments(1, 200).thenReturn(page);
		Map<String, Object> expectedMap = new HashMap<String, Object>();
		Map<String, Object> tweet = new HashMap<String, Object>();
		Map<String, Object> userMap = new HashMap<String, Object>();
		List<String> expected = new ArrayList<String>();
		tweet.put("id", 1L);
		tweet.put("in_reply_to_screenname", "ReplyToScreenName");
		tweet.put("text", "text");
		tweet.put("in_reply_to_status", 2);
		tweet.put("in_reply_to_user", 3);
		
		userMap.put("id", 1L);
		userMap.put("location","location");
		userMap.put("profile_image","url");
		userMap.put("friends_count",1);
		userMap.put("name","name");
		userMap.put("screen_name","screen_name");
		userMap.put("language","english");
		userMap.put("followers_count",1);
		
		expectedMap.put("user", userMap);
		expectedMap.put("tweet", tweet);
		
		JSONObject jObj = new JSONObject(expectedMap);
		String expectedString = jObj.toString();
		expected.add(expectedString);

		
		
		int lmts[] = { 15, 1000 };
		Mockito.when(
				 cRManager.rateLimitAnalyzer(twitter, lManager,LimitsEndPointsVector.STATUSES_MENTIONS_TIMELINE)).thenReturn(
				lmts);
		Mockito.when(twitter.getMentionsTimeline(page)).thenReturn(statuses);
		
		Mockito.when(statusIterator.hasNext()).thenReturn(true, false);
		Mockito.when(statusIterator.next()).thenReturn(status);
		Mockito.when(statuses.iterator()).thenReturn(statusIterator);
		
		Mockito.when(status.getId()).thenReturn(1L);
		Mockito.when(status.getText()).thenReturn("text");
		Mockito.when(status.getInReplyToScreenName()).thenReturn("ReplyToScreenName");
		Mockito.when(status.getInReplyToStatusId()).thenReturn(2L);
		Mockito.when(status.getInReplyToUserId()).thenReturn(3L);
		
		Mockito.when(status.getUser()).thenReturn(user);
		
		Mockito.when(user.getScreenName()).thenReturn("screen_name");
		Mockito.when(user.getName()).thenReturn("name");
		Mockito.when(user.getId()).thenReturn(1L);
		Mockito.when(user.getMiniProfileImageURL()).thenReturn("url");
		Mockito.when(user.getFriendsCount()).thenReturn(1);
		Mockito.when(user.getFollowersCount()).thenReturn(1);
		Mockito.when(user.getLocation()).thenReturn("location");
		Mockito.when(user.getLang()).thenReturn("english");
	
		
		StatusesManager statusManager = new StatusesManager(twitter, ddManager,
				cRManager, lManager);
		List<String> result = statusManager.getMentionsTimeline();
		
		assertEquals(result, expected);
		
	}
	
	@Test
	public void userTimeLineHOME_TIMELINE() throws Exception{
		PowerMockito.whenNew(Paging.class).withArguments(1, 200).thenReturn(page);
		List<String> expected = new ArrayList<String>();
		expected.add(1L +"\ttext");
		String tuser = "123";
		int lmts[] = { 15, 1000 };
		subCmdUpVector sbv = subCmdUpVector.HOME_TIMELINE;
		Mockito.when(
				cRManager.rateLimitAnalyzer(twitter, lManager,LimitsEndPointsVector.STATUSES_HOME_TIMELINE)).thenReturn(
				lmts);
		Mockito.when(twitter.getHomeTimeline(page)).thenReturn(statuses);
		
		Mockito.when(statusIterator.hasNext()).thenReturn(true, false);
		Mockito.when(statusIterator.next()).thenReturn(status);
		Mockito.when(statuses.iterator()).thenReturn(statusIterator);
		
		Mockito.when(status.getId()).thenReturn(1L);
		Mockito.when(status.getText()).thenReturn("text");
		
		StatusesManager statusManager = new StatusesManager(twitter, ddManager,
				cRManager, lManager);
		
		List<String> result = statusManager.userTimeLine(tuser, sbv);
		 assertEquals(result, expected);
	}
	

	@Test
	public void userTimeLineUSER_TIMELINE() throws Exception{
		PowerMockito.whenNew(Paging.class).withArguments(1, 200).thenReturn(page);
		List<String> expected = new ArrayList<String>();
		expected.add("text");
		String tuser = "123";
		int lmts[] = { 15, 1000 };
		subCmdUpVector sbv = subCmdUpVector.USER_TIMELINE;
		Mockito.when(
				cRManager.rateLimitAnalyzer(twitter, lManager,LimitsEndPointsVector.STATUSES_USER_TIMELINE)).thenReturn(
				lmts);
		Mockito.when(TwitterCredentials.getuID()).thenReturn("1");
		Mockito.when( twitter.getUserTimeline(Long.parseLong(TwitterCredentials.getuID()), page)).thenReturn(statuses);
		
		Mockito.when(statusIterator.hasNext()).thenReturn(true, false);
		Mockito.when(statusIterator.next()).thenReturn(status);
		Mockito.when(statuses.iterator()).thenReturn(statusIterator);
		
		
		Mockito.when(status.getText()).thenReturn("text");
		
		StatusesManager statusManager = new StatusesManager(twitter, ddManager,
				cRManager, lManager);
		
		List<String> result = statusManager.userTimeLine(tuser, sbv);
		 assertEquals(result, expected);
	}
	@Test
	public void userTimeLineOWN_RETWEETS() throws Exception{
		PowerMockito.whenNew(Paging.class).withArguments(1, 100).thenReturn(page);
		List<String> expected = new ArrayList<String>();
		expected.add("text");
		String tuser = "123";
		int lmts[] = { 15, 1000 };
		subCmdUpVector sbv = subCmdUpVector.OWN_RETWEETS;
		Mockito.when(
				cRManager.rateLimitAnalyzer(twitter, lManager,LimitsEndPointsVector.STATUSES_RETWEETS_OF_ME)).thenReturn(
				lmts);
		Mockito.when(twitter.getRetweetsOfMe(page)).thenReturn(statuses);
		
		
		Mockito.when(statusIterator.hasNext()).thenReturn(true, false);
		Mockito.when(statusIterator.next()).thenReturn(status);
		Mockito.when(statuses.iterator()).thenReturn(statusIterator);
		
		
		Mockito.when(status.getText()).thenReturn("text");
		
		StatusesManager statusManager = new StatusesManager(twitter, ddManager,
				cRManager, lManager);
		
		List<String> result = statusManager.userTimeLine(tuser, sbv);
		assertEquals(result, expected);
	}
}
