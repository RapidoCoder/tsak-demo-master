package twitterswissarmyknife;

import jcommander.ArgsManager;

import org.junit.Before;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;

import twitter4j.IDs;
import twitter4j.ResponseList;
import twitter4j.Twitter;
import twitter4j.User;
import twitterhandler.FFManager;
import twitterhandler.LimitsManager;
import twitterhandler.TwitterAuth;
import twitterhandler.TwitterCredentials;
import dto.ControlHandlers.controlVectors;
import dto.CrManager;
import dto.DDManager;

public class TestSearchManager {
	
	
	@Mock
	CrManager cRManager;
	controlVectors cVectors;
	ArgsManager argManager;
	Twitter twitter;
	TwitterAuth tAuth;
	IDs followerIds;
	LimitsManager lManager;
	DDManager ddManager;
	ResponseList<User> followers;
	FFManager fmanager;

	@Before
	public void setup() {
		cRManager = Mockito.mock(CrManager.class);

		cVectors = Mockito.mock(controlVectors.class);
		argManager = Mockito.mock(ArgsManager.class);
		tAuth = Mockito.mock(TwitterAuth.class);
		twitter = Mockito.mock(Twitter.class);
		fmanager = Mockito.mock(FFManager.class);
		lManager = Mockito.mock(LimitsManager.class);
		PowerMockito.mockStatic(TwitterCredentials.class);
		followerIds = Mockito.mock(IDs.class);
		followers = Mockito.mock(ResponseList.class);
		ddManager = Mockito.mock(DDManager.class);
	}

	
}
