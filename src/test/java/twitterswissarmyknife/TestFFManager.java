package twitterswissarmyknife;

import static org.junit.Assert.assertEquals;
import jcommander.ArgsManager;

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
import twitterhandler.TwitterAuth;
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
	controlVectors cVectors;
	ArgsManager argManager;
	Twitter twitter;
	TwitterAuth tAuth;
	IDs followerIds;
	IDs friendsIds;
	LimitsManager lManager;
	DDManager ddManager;
	ResponseList<User> followers;
	ResponseList<User> followings;
	FFManager fmanager;
	ResponseList<User> ffretrn;
	@SuppressWarnings("unchecked")
	@Before
	public void setup() throws Exception {
		cRManager = Mockito.mock(CrManager.class);

		cVectors = Mockito.mock(controlVectors.class);
		argManager = Mockito.mock(ArgsManager.class);
		tAuth = Mockito.mock(TwitterAuth.class);
		twitter = Mockito.mock(Twitter.class);
		fmanager = Mockito.mock(FFManager.class);
		lManager = Mockito.mock(LimitsManager.class);
		PowerMockito.mockStatic(TwitterCredentials.class);
		followerIds = Mockito.mock(IDs.class);
		friendsIds = Mockito.mock(IDs.class);
		followers = Mockito.mock(ResponseList.class);
		followings = Mockito.mock(ResponseList.class);
		ddManager = Mockito.mock(DDManager.class);
		ffretrn = Mockito.mock(ResponseList.class);

	}
	// ////////////////////////
		@Test
		public void Test_FLWRZ_DUMP_BY_ID() throws Exception {
			String tuser = "12";
			int lmts[] = { 15, 1000 };
			subCmdUpVector sbv = subCmdUpVector.FLWRZ_DUMP_BY_ID;
			Mockito.when(twitter.getFollowersIDs(Long.parseLong(tuser) , -1L)).thenReturn(followerIds);
			Mockito.when(
					cRManager.rateLimitAnalyzer(twitter, lManager,
							LimitsEndPointsVector.FOLLOWERS_IDS)).thenReturn(lmts);
			FFManager ffmanager = new FFManager(twitter, ddManager,cRManager,lManager);
			Mockito.when(twitter .lookupUsers(followerIds.getIDs())).thenReturn(ffretrn);
			
			ResponseList<User> result = ffmanager.dumpFFToFile(tuser, sbv);
			assertEquals(result,ffretrn); 
		}
		// ////////////////////////
				@Test
				public void FLWRZ_DUMP_BY_SCREEN_NAME() throws Exception {
					String tuser = "some_screen_name";
					int lmts[] = { 15, 1000 };
					subCmdUpVector sbv = subCmdUpVector.FLWRZ_DUMP_BY_SCREEN_NAME;
					Mockito.when(twitter.getFollowersIDs(tuser , -1L)).thenReturn(followerIds);
					Mockito.when(
							cRManager.rateLimitAnalyzer(twitter, lManager,
									LimitsEndPointsVector.FOLLOWERS_IDS)).thenReturn(lmts);
					FFManager ffmanager = new FFManager(twitter, ddManager,cRManager,lManager);
					Mockito.when(twitter .lookupUsers(followerIds.getIDs())).thenReturn(ffretrn);
					
					ResponseList<User> result = ffmanager.dumpFFToFile(tuser, sbv);
					assertEquals(result,ffretrn); 
				}	
				
				@Test
				public void FRNDZ_DUMP_BY_ID() throws Exception {
					String tuser = "12";
					int lmts[] = { 15, 1000 };
					subCmdUpVector sbv = subCmdUpVector.FRNDZ_DUMP_BY_ID;
					Mockito.when(twitter.getFriendsIDs(Long.parseLong(tuser) , -1L)).thenReturn(followerIds);
					Mockito.when(
							cRManager.rateLimitAnalyzer(twitter, lManager,LimitsEndPointsVector.FRIENDS_IDS)).thenReturn(lmts);
					FFManager ffmanager = new FFManager(twitter, ddManager,cRManager,lManager);
					Mockito.when(twitter .lookupUsers(friendsIds.getIDs())).thenReturn(ffretrn);
					
					ResponseList<User> result = ffmanager.dumpFFToFile(tuser, sbv);
					assertEquals(result,ffretrn); 
				}	
				@Test
				public void FRNDZ_DUMP_BY_SCREEN_NAME() throws Exception {
					String tuser = "12";
					int lmts[] = { 15, 1000 };
					subCmdUpVector sbv = subCmdUpVector.FRNDZ_DUMP_BY_SCREEN_NAME;
					Mockito.when(twitter.getFriendsIDs(String.valueOf(tuser) , -1L)).thenReturn(followerIds);
					Mockito.when(
							cRManager.rateLimitAnalyzer(twitter, lManager,LimitsEndPointsVector.FRIENDS_IDS)).thenReturn(lmts);
					FFManager ffmanager = new FFManager(twitter, ddManager,cRManager,lManager);
					Mockito.when(twitter .lookupUsers(friendsIds.getIDs())).thenReturn(ffretrn);
					
					ResponseList<User> result = ffmanager.dumpFFToFile(tuser, sbv);
					assertEquals(result,ffretrn); 
				}		
}
