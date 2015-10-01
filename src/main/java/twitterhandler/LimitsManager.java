package twitterhandler;

import java.util.HashMap;
import java.util.Map;

import twitter4j.RateLimitStatus;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import dto.ControlHandlers.LimitCheckVector;
import dto.ControlHandlers.LimitsEndPointsVector;
import dto.CrManager;

public class LimitsManager {

	Map<LimitsEndPointsVector, String> limitsVector;

	public LimitsManager() {

		limitsVector = new HashMap<LimitsEndPointsVector, String>();
		initMapVector(limitsVector);
	}

	public void displayRateLimits(Twitter twitter, CrManager cRHandlers) {

		try {
			Map<String, RateLimitStatus> rateLimitStatus = twitter
					.getRateLimitStatus();
			for (String endpoint : rateLimitStatus.keySet()) {
				RateLimitStatus status = rateLimitStatus.get(endpoint);
				
				cRHandlers.DisplayInfoMessage("Endpoint: " + endpoint,true);
				cRHandlers.DisplayInfoMessage(" Limit: " + status.getLimit(),true);
				cRHandlers.DisplayInfoMessage(" Remaining: " + status.getRemaining(),true);
				cRHandlers.DisplayInfoMessage(" ResetTimeInSeconds: "
						+ status.getResetTimeInSeconds(),true);
				cRHandlers.DisplayInfoMessage(" SecondsUntilReset: "
						+ status.getSecondsUntilReset(),true);
				
			}
		} catch (TwitterException te) {
			System.out.println("Failed to get rate limit status: "
					+ te.getMessage());
		}
	}

	public int getRemainingLimitSpecific(Twitter twitter,
			LimitsEndPointsVector EP_vector, LimitCheckVector LC_vector) {
		
		int returnLmt = 0;

		String Rootfamily = limitsVector.get(EP_vector).split("/", 3)[1];
		try {
			RateLimitStatus status = twitter.getRateLimitStatus(Rootfamily)
					.get(limitsVector.get(EP_vector));
			switch (LC_vector) {
			case LIMIT_TOTAL:
				returnLmt =  status.getLimit();
				break;
			case LIMIT_REMAINING:
				returnLmt = status.getRemaining();
				break;
			case RESET_TIME_IN_SECONDS:
				returnLmt = Math.abs(status.getResetTimeInSeconds());
				break;
			case TIME_SECONDS_UNTIL_RESET:
				returnLmt = Math.abs(status.getSecondsUntilReset());
				break;
			}
		} catch (TwitterException e) {

			e.printStackTrace();
		}
		
		return returnLmt;
	}

	private void initMapVector(Map<LimitsEndPointsVector, String> map) {

		map.put(LimitsEndPointsVector.FRIENDSHIPS_NO_RETWEETS_IDS,
				"/friendships/no_retweets/ids");
		map.put(LimitsEndPointsVector.LISTS_MEMBERS_SHOW, "/lists/members/show");
		map.put(LimitsEndPointsVector.LISTS_OWNERSHIPS, "/lists/ownerships");
		map.put(LimitsEndPointsVector.FRIENDS_LIST, "/friends/list");
		map.put(LimitsEndPointsVector.STATUSES_SHOW_ID, "/statuses/show/:id");
		map.put(LimitsEndPointsVector.COLLECTIONS_ENTRIES, "/collections/entries");
		map.put(LimitsEndPointsVector.GEO_REVERSE_GEOCODE, "/geo/reverse_geocode");
		map.put(LimitsEndPointsVector.FRIENDS_FOLLOWING_LIST, "/friends/following/list");
		map.put(LimitsEndPointsVector.COLLECTIONS_LIST, "/collections/list");
		map.put(LimitsEndPointsVector.SAVED_SEARCHES_LIST, "/saved_searches/list");
		map.put(LimitsEndPointsVector.HELP_PRIVACY, "/help/privacy");
		map.put(LimitsEndPointsVector.USERS_REPORT_SPAM, "/users/report_spam");
		map.put(LimitsEndPointsVector.HELP_CONFIGURATION, "/help/configuration");
		map.put(LimitsEndPointsVector.STATUSES_FRIENDS, "/statuses/friends");
		map.put(LimitsEndPointsVector.SAVED_SEARCHES_DESTROY_ID, "/saved_searches/destroy/:id");
		map.put(LimitsEndPointsVector.MUTES_USERS_LIST, "/mutes/users/list");
		map.put(LimitsEndPointsVector.DIRECT_MESSAGES_SENT, "/direct_messages/sent");
		map.put(LimitsEndPointsVector.USERS_SUGGESTIONS_SLUG, "/users/suggestions/:slug");
		map.put(LimitsEndPointsVector.CONTACTS_UPLOADED_BY, "/contacts/uploaded_by");
		map.put(LimitsEndPointsVector.LISTS_SUBSCRIBERS_SHOW, "/lists/subscribers/show");
		map.put(LimitsEndPointsVector.STATUSES_OEMBED, "/statuses/oembed");
		map.put(LimitsEndPointsVector.DIRECT_MESSAGES, "/direct_messages");
		map.put(LimitsEndPointsVector.FOLLOWERS_LIST, "/followers/list");
		map.put(LimitsEndPointsVector.LISTS_SHOW, "/lists/show");
		map.put(LimitsEndPointsVector.USERS_PROFILE_BANNER, "/users/profile_banner");
		map.put(LimitsEndPointsVector.HELP_LANGUAGES, "/help/languages");
		map.put(LimitsEndPointsVector.TRENDS_AVAILABLE, "/trends/available");
		map.put(LimitsEndPointsVector.BLOCKS_IDS, "/blocks/ids");
		map.put(LimitsEndPointsVector.MUTES_USERS_IDS, "/mutes/users/ids");
		map.put(LimitsEndPointsVector.GEO_SIMILAR_PLACES, "/geo/similar_places");
		map.put(LimitsEndPointsVector.STATUSES_RETWEETS_ID, "/statuses/retweets/:id");
		map.put(LimitsEndPointsVector.STATUSES_LOOKUP, "/statuses/lookup");
		map.put(LimitsEndPointsVector.FRIENDSHIPS_OUTGOING, "/friendships/outgoing");
		map.put(LimitsEndPointsVector.APPLICATION_RATE_LIMIT_STATUS, "/application/rate_limit_status");
		map.put(LimitsEndPointsVector.LISTS_SUBSCRIBERS, "/lists/subscribers");
		map.put(LimitsEndPointsVector.FRIENDS_FOLLOWING_IDS, "/friends/following/ids");
		map.put(LimitsEndPointsVector.FRIENDSHIPS_INCOMING, "/friendships/incoming");
		map.put(LimitsEndPointsVector.FAVORITES_LIST, "/favorites/list");
		map.put(LimitsEndPointsVector.ACCOUNT_SETTINGS, "/account/settings");
		map.put(LimitsEndPointsVector.TRENDS_CLOSEST, "/trends/closest");
		map.put(LimitsEndPointsVector.USERS_SUGGESTIONS, "/users/suggestions");
		map.put(LimitsEndPointsVector.STATUSES_RETWEETERS_IDS, "/statuses/retweeters/ids");
		map.put(LimitsEndPointsVector.LISTS_LIST, "/lists/list");
		map.put(LimitsEndPointsVector.LISTS_MEMBERS, "/lists/members");
		map.put(LimitsEndPointsVector.ACCOUNT_UPDATE_PROFILE, "/account/update_profile");
		map.put(LimitsEndPointsVector.HELP_TOS, "/help/tos");
		map.put(LimitsEndPointsVector.CONTACTS_USERS, "/contacts/users");
		map.put(LimitsEndPointsVector.HELP_SETTINGS, "/help/settings");
		map.put(LimitsEndPointsVector.STATUSES_RETWEETS_OF_ME, "/statuses/retweets_of_me");
		map.put(LimitsEndPointsVector.BLOCKS_LIST, "/blocks/list");
		map.put(LimitsEndPointsVector.DIRECT_MESSAGES_SENT_AND_RECEIVED, "/direct_messages/sent_and_received");
		map.put(LimitsEndPointsVector.USERS_SHOW_ID, "/users/show/:id");
		map.put(LimitsEndPointsVector.GEO_ID_PLACE_ID, "/geo/id/:place_id");
		map.put(LimitsEndPointsVector.DEVICE_TOKEN, "/device/token");
		map.put(LimitsEndPointsVector.FRIENDSHIPS_SHOW, "/friendships/show");
		map.put(LimitsEndPointsVector.FOLLOWERS_IDS, "/followers/ids");
		map.put(LimitsEndPointsVector.LISTS_STATUSES, "/lists/statuses");
		map.put(LimitsEndPointsVector.FRIENDSHIPS_LOOKUP, "/friendships/lookup");
		map.put(LimitsEndPointsVector.USERS_LOOKUP, "/users/lookup");
		map.put(LimitsEndPointsVector.STATUSES_USER_TIMELINE, "/statuses/user_timeline");
		map.put(LimitsEndPointsVector.TRENDS_PLACE, "/trends/place");
		map.put(LimitsEndPointsVector.ACCOUNT_VERIFY_CREDENTIALS, "/account/verify_credentials");
		map.put(LimitsEndPointsVector.CONTACTS_DELETE_STATUS, "/contacts/delete/status");
		map.put(LimitsEndPointsVector.CONTACTS_ADDRESSBOOK, "/contacts/addressbook");
		map.put(LimitsEndPointsVector.USERS_SEARCH, "/users/search");
		map.put(LimitsEndPointsVector.COLLECTIONS_SHOW, "/collections/show");
		map.put(LimitsEndPointsVector.USERS_SUGGESTIONS_SLUG_MEMBERS, "/users/suggestions/:slug/members");
		map.put(LimitsEndPointsVector.USERS_DERIVED_INFO, "/users/derived_info");
		map.put(LimitsEndPointsVector.STATUSES_HOME_TIMELINE, "/statuses/home_timeline");
		map.put(LimitsEndPointsVector.SEARCH_TWEETS, "/search/tweets");
		map.put(LimitsEndPointsVector.SAVED_SEARCHES_SHOW_ID, "/saved_searches/show/:id");
		map.put(LimitsEndPointsVector.DIRECT_MESSAGES_SHOW, "/direct_messages/show");
		map.put(LimitsEndPointsVector.STATUSES_MENTIONS_TIMELINE, "/statuses/mentions_timeline");
		map.put(LimitsEndPointsVector.LISTS_MEMBERSHIPS, "/lists/memberships");
		map.put(LimitsEndPointsVector.FRIENDS_IDS, "/friends/ids");
		map.put(LimitsEndPointsVector.CONTACTS_USERS_AND_UPLOADED_BY, "/contacts/users_and_uploaded_by");
		map.put(LimitsEndPointsVector.ACCOUNT_LOGIN_VERIFICATION_ENROLLMENT, "/account/login_verification_enrollment");
		map.put(LimitsEndPointsVector.GEO_SEARCH, "/geo/search");
		map.put(LimitsEndPointsVector.LISTS_SUBSCRIPTIONS, "/lists/subscriptions");
	}

}
