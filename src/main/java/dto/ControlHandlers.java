package dto;

public class ControlHandlers {

	public enum controlVectors {

		TSAK_GET_PARAMETERS_FROM_ENV_VAR, TSAK_TWITTER_ACCESS_VERIFICATION, TSAK_VERIFY_USER_CREDENTIALS, TSAK_TWITTER_GET_DUMP, TSAK_SYSTEM_EXIT_PHASE
	}

	public enum subCmdUpVector {

		NOP, FLWRZ_DUMP_BY_ID, FLWRZ_DUMP_BY_SCREEN_NAME,FRNDZ_DUMP_BY_ID, FRNDZ_DUMP_BY_SCREEN_NAME,
		HOME_TIMELINE,USER_TIMELINE,OWN_RETWEETS,SEARCH_TWEETS, GET_STATUS_BY_ID, GET_STATUS_RETWEETERS, MENTIONS_TIMELINE,
		USERS_LOOKUP, BLOCK_LIST, SEARCH_USER, SHOW_FRNDSHIP,FRIENDS_LIST,FOLLOWERS_LIST, FAVOURITES, SUGGESTED_USER_CATS, SUGGESTION_SLUG,
		MEMBER_SUGGESTION, USER_LISTS, LIST_STATUSES, SAVED_SEARCHES, LOOKUP_FRNDSHIP, INCOMING_FRNDSHIP, OUTGOING_FRNDSHIP, GEO_DETAILS, SIMILAR_PLACES,
		SEARCH_PLACE_BY_CORD, ACCOUNT_SETTINGS, AVAILABLE_TRENDS, PLACE_TRENDS, CLOSEST_TRENDS, MUTES_IDS, MUTES_LIST, LISTS_MEMEBERSHIPS, LIST_SUBSCRIBERS, LIST_MEMBERS,
		LIST_SUBSCRIPTIONS
	}

	public enum exceptionVector {
		MISSING_OUTPUT_FILE, MISSING_SCREEN_NAME, MISSING_ID, BOTH_SCREEN_NAME_ID_MISSING, BOTH_SCREEN_NAME_ID_GIVEN, MISSING_SEARCH_KEYWORDS, MISSING_SID, MISSING_INPUT_FILE, MISSING_LID, MISSING_PID, MISSING_CORD_OR_PLACE, MISSING_CORD,
		MISSING_SRC_TRG_USER_SN_ID, MISSING_SLUG, MISSING_WOEID
	}

	public enum LimitsEndPointsVector {
		FRIENDSHIPS_NO_RETWEETS_IDS, LISTS_MEMBERS_SHOW, LISTS_OWNERSHIPS, FRIENDS_LIST, STATUSES_SHOW_ID, COLLECTIONS_ENTRIES, GEO_REVERSE_GEOCODE, FRIENDS_FOLLOWING_LIST, COLLECTIONS_LIST, SAVED_SEARCHES_LIST, HELP_PRIVACY, USERS_REPORT_SPAM, HELP_CONFIGURATION, STATUSES_FRIENDS, SAVED_SEARCHES_DESTROY_ID, MUTES_USERS_LIST, DIRECT_MESSAGES_SENT, USERS_SUGGESTIONS_SLUG, CONTACTS_UPLOADED_BY, LISTS_SUBSCRIBERS_SHOW, STATUSES_OEMBED, DIRECT_MESSAGES, FOLLOWERS_LIST, LISTS_SHOW, USERS_PROFILE_BANNER, HELP_LANGUAGES, TRENDS_AVAILABLE, BLOCKS_IDS, MUTES_USERS_IDS, GEO_SIMILAR_PLACES, STATUSES_RETWEETS_ID, STATUSES_LOOKUP, FRIENDSHIPS_OUTGOING, APPLICATION_RATE_LIMIT_STATUS, LISTS_SUBSCRIBERS, FRIENDS_FOLLOWING_IDS, FRIENDSHIPS_INCOMING, FAVORITES_LIST, ACCOUNT_SETTINGS, TRENDS_CLOSEST, USERS_SUGGESTIONS, STATUSES_RETWEETERS_IDS, LISTS_LIST, LISTS_MEMBERS, ACCOUNT_UPDATE_PROFILE, HELP_TOS, CONTACTS_USERS, HELP_SETTINGS, STATUSES_RETWEETS_OF_ME, BLOCKS_LIST, DIRECT_MESSAGES_SENT_AND_RECEIVED, USERS_SHOW_ID, GEO_ID_PLACE_ID, DEVICE_TOKEN, FRIENDSHIPS_SHOW, FOLLOWERS_IDS, LISTS_STATUSES, FRIENDSHIPS_LOOKUP, USERS_LOOKUP, STATUSES_USER_TIMELINE, TRENDS_PLACE, ACCOUNT_VERIFY_CREDENTIALS, CONTACTS_DELETE_STATUS, CONTACTS_ADDRESSBOOK, USERS_SEARCH, COLLECTIONS_SHOW, USERS_SUGGESTIONS_SLUG_MEMBERS, USERS_DERIVED_INFO, STATUSES_HOME_TIMELINE, SEARCH_TWEETS, SAVED_SEARCHES_SHOW_ID, DIRECT_MESSAGES_SHOW, STATUSES_MENTIONS_TIMELINE, LISTS_MEMBERSHIPS, FRIENDS_IDS, CONTACTS_USERS_AND_UPLOADED_BY, ACCOUNT_LOGIN_VERIFICATION_ENROLLMENT, GEO_SEARCH, LISTS_SUBSCRIPTIONS
	}

	public enum LimitCheckVector {
		LIMIT_TOTAL, LIMIT_REMAINING, RESET_TIME_IN_SECONDS, TIME_SECONDS_UNTIL_RESET
	}

}