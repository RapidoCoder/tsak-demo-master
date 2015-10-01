package twitterhandler;

import twitter4j.RateLimitStatus;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.conf.ConfigurationBuilder;

public class TwitterAuth {

	ConfigurationBuilder cBuilder;
	Twitter twitter;
	User user;
	RateLimitStatus status;
	int rateLimit = 0;

	public Twitter getTwitterInstance() {
		return twitter;
	}
	
	// getter setter
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public TwitterAuth() {
		cBuilder = new ConfigurationBuilder();
		cBuilder.setDebugEnabled(true)
				.setOAuthConsumerKey(TwitterCredentials.getConsumerKey())
				.setOAuthConsumerSecret(
						TwitterCredentials.getConsumerSecretKey())
				.setOAuthAccessToken(TwitterCredentials.getUserAccessToken())
				.setOAuthAccessTokenSecret(
						TwitterCredentials.getUserAccessSecretToken());

		twitter = new TwitterFactory(cBuilder.build()).getInstance();
	}

	public void getRemainingCalls() {
		status = user.getRateLimitStatus();
		rateLimit = status.getRemaining();
	}

	public boolean verifyKeys() {
		boolean isVerified = false;
		try {
			user = twitter.verifyCredentials();
			isVerified = true;
		} catch (TwitterException e) {
			isVerified = false;
		} finally {
		}
		return isVerified;
	}
}
