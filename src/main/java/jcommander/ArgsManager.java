package jcommander;

import twitter4j.TwitterException;
import twitterhandler.TwitterAuth;
import twitterhandler.TwitterCredentials;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;

import dto.ControlHandlers.exceptionVector;
import dto.ControlHandlers.subCmdUpVector;
import dto.CrManager;
import dto.TsakException;

public class ArgsManager {

	CrManager cRManager;

	// CMD Commands
	private final Tuser followersdump = new Tuser();
	private final Tuser friendsdump = new Tuser();
	private final FDbase hometimeline = new FDbase();
	private final Tuser usertimeline = new Tuser();
	private final FDbase myretweets = new FDbase();
	private final KWsearch searchtweets = new KWsearch();
	private final Tstatus showstatus = new Tstatus();
	private final Tstatus retweeters = new Tstatus();
	private final FDbase mentiontimeline = new FDbase();
	private final Tusers userslookup = new Tusers();
	private final FDbase blocklist = new FDbase();
	private final KWsearch searchusers = new KWsearch();
	private final Tlink showfrndship = new Tlink();
	private final Tuser friendslist = new Tuser();
	private final Tuser followerslist = new Tuser();
	private final FDbase favorities = new FDbase();
	private final FDbase suggestedusercats = new FDbase();
	private final Tslug usersuggestions = new Tslug();
	private final Tslug membersuggestions = new Tslug();
	private final Tuser userlists = new Tuser();
	private final Tlist liststatuses = new Tlist();
	private final FDbase savedsearches = new FDbase();
	private final Tusers lookupfrndship = new Tusers();
	private final FDbase incomingfrndship = new FDbase();
	private final FDbase outgoingfrndship = new FDbase();
	private final Tgeo geodetails = new Tgeo();
	private final Tgeo similarplaces = new Tgeo();
	private final Tgeo searchplace = new Tgeo();
	private final FDbase accountsettings = new FDbase();
	private final FDbase availabletrends = new FDbase();
	private final Ttrends placetrends = new Ttrends();
	private final Tgeo closest_trends = new Tgeo();
	private final FDbase mutesIds = new FDbase();
	private final FDbase mutesList = new FDbase();
	private final Tuser listmembership = new Tuser();
	private final Tlist userlistsubscriber = new Tlist();
	private final Tlist userlistmembers = new Tlist();
	private final Tuser listssubscritons = new Tuser();

	
	
	public ArgsManager(CrManager crm) {

		cRManager = crm;
	}

	public subCmdUpVector getCmdArgs(String[] args, boolean getRootParam) {

		TsakException tsakExp;
		subCmdUpVector returnObj = subCmdUpVector.NOP;
		Tsak tsak = new Tsak();

		JCommander cmdRootTsak = new JCommander();

		cmdRootTsak.setAcceptUnknownOptions(false);

		cmdRootTsak.addCommand("tsak", tsak);

		JCommander cmdSub = cmdRootTsak.getCommands().get("tsak");

		cmdSub.setAcceptUnknownOptions(false);

		activateSubCommands(cmdSub);

		try {
			cmdRootTsak.parse(args);

			if (new String("tsak").equals(cmdRootTsak.getParsedCommand())) {

				if (getRootParam) {
					TwitterCredentials.setConsumerKey(tsak.getConsumerKey());
					TwitterCredentials.setConsumerSecretKey(tsak
							.getConsumerSecretKey());
					TwitterCredentials.setUserAccessToken(tsak.getAccessKey());
					TwitterCredentials.setUserAccessSecretToken(tsak
							.getAccessSecretKey());

					if (!TwitterCredentialsAvailable()) {

						tsakExp = new TsakException(
								"[ERROR]: Missing Twitter Credentials Keys.");
						throw tsakExp;
					}

				}

				subCmdUpVector subVector = getUpCommand(cmdSub
						.getParsedCommand());

				if (subVector != null && subVector != subCmdUpVector.NOP) {
					returnObj = subVector;
				} else {
					tsakExp = new TsakException(
							"Bad command. Error parsing command "
									+ cmdSub.getParsedCommand() + ".");
					throw tsakExp;
				}
			}

		} catch (ParameterException e) {
			cRManager.DisplayErrorMessage("[ERROR]: " + e.getMessage(), true);
			System.exit(-1);
		} catch (TsakException e) {

			cRManager.DisplayErrorMessage("[ERROR]: " + e.getMessage(), true);
		} catch (Exception e) {

			cRManager.DisplayErrorMessage("[ERROR]: " + e.getMessage(), true);
		}

		return returnObj;

	}

	private void activateSubCommands(JCommander cmdSub) {

		cmdSub.addCommand("dumpFollowerIDs", followersdump);

		cmdSub.addCommand("dumpFriendIDs", friendsdump);

		cmdSub.addCommand("dumpHomeTimeLine", hometimeline);

		cmdSub.addCommand("dumpUserTimeLine", usertimeline);

		cmdSub.addCommand("dumpOwnRetweets", myretweets);

		cmdSub.addCommand("dumpTweets", searchtweets);

		cmdSub.addCommand("dumpStatus", showstatus);

		cmdSub.addCommand("dumpRetweeters", retweeters);

		cmdSub.addCommand("dumpMentionsTimeLine", mentiontimeline);

		cmdSub.addCommand("dumpUsersLookup", userslookup);

		cmdSub.addCommand("dumpBlockList", blocklist);

		cmdSub.addCommand("searchUsers", searchusers);

		cmdSub.addCommand("showFriendShip", showfrndship);

		cmdSub.addCommand("dumpFriendsList", friendslist);

		cmdSub.addCommand("dumpFollowersList", followerslist);

		cmdSub.addCommand("dumpFavourites", favorities);

		cmdSub.addCommand("dumpSugeestedUserCats", suggestedusercats);

		cmdSub.addCommand("dumpUserSuggestions", usersuggestions);

		cmdSub.addCommand("dumpMemberSuggestions", membersuggestions);

		cmdSub.addCommand("dumpUserLists", userlists);

		cmdSub.addCommand("dumpListStatuses", liststatuses);

		cmdSub.addCommand("dumpSavedSearches", savedsearches);

		cmdSub.addCommand("lookupFriendShip", lookupfrndship);

		cmdSub.addCommand("dumpIncomingFriendships", incomingfrndship);

		cmdSub.addCommand("dumpOutgoingFriendships", outgoingfrndship);

		cmdSub.addCommand("dumpGeoDetails", geodetails);

		cmdSub.addCommand("dumpSimilarPlaces", similarplaces);
		
		cmdSub.addCommand("searchPlace", searchplace);
		
		cmdSub.addCommand("dumpAccountSettings", accountsettings);
		
		cmdSub.addCommand("dumpAvailableTrends", availabletrends);
		
		cmdSub.addCommand("dumpPlaceTrends", placetrends);
	
		cmdSub.addCommand("dumpClosestTrends", closest_trends);
		
		cmdSub.addCommand("dumpMutesIDs", mutesIds);
		
		cmdSub.addCommand("dumpMutesList", mutesList);
		
		cmdSub.addCommand("dumpUserListMemberships", listmembership);
		
		cmdSub.addCommand("dumpUserListSubscribers", userlistsubscriber);
	
		cmdSub.addCommand("dumpUserListMembers", userlistmembers);
		
		cmdSub.addCommand("dumpUserListSubscriptions", listssubscritons);
		
	}

	private subCmdUpVector getUpCommand(String command) throws TsakException,
			IllegalStateException, TwitterException {

		subCmdUpVector subVector = null;
		TsakException tsakExp = null;

		// if none of sub Action Command given
		if (command == null | command.trim().isEmpty()) {

			tsakExp = new TsakException(
					"[ERROR]: Missing Action Command. e.g. dumpFollowerIDs");
			throw tsakExp;

		} else if (command.equals("dumpFollowerIDs")) {

			subVector = tUserCred(followersdump, command, tsakExp);

		} else if (command.equals("dumpFriendIDs")) {

			subVector = tUserCred(friendsdump, command, tsakExp);

		} else if (command.equals("dumpHomeTimeLine")) {

			opFileParam(hometimeline, tsakExp);
			cRManager.DisplayInfoMessage(
					"[OBJECTIVE]: Dump user "
							+ new TwitterAuth().getTwitterInstance()
									.getScreenName() + "'s Home Timeline to "
							+ TwitterCredentials.getOutputFile() + ".", true);
			subVector = subCmdUpVector.HOME_TIMELINE;

		} else if (command.equals("dumpOwnRetweets")) {

			opFileParam(myretweets, tsakExp);
			cRManager.DisplayInfoMessage("[OBJECTIVE]: Dump user "
					+ new TwitterAuth().getTwitterInstance().getScreenName()
					+ "'s Retweets to " + TwitterCredentials.getOutputFile()
					+ ".", true);
			subVector = subCmdUpVector.OWN_RETWEETS;

		} else if (command.equals("dumpMentionsTimeLine")) {

			opFileParam(mentiontimeline, tsakExp);
			cRManager.DisplayInfoMessage(
					"[OBJECTIVE]: Dump user "
							+ new TwitterAuth().getTwitterInstance()
									.getScreenName()
							+ "'s Mentions Timeline to "
							+ TwitterCredentials.getOutputFile() + ".", true);
			subVector = subCmdUpVector.MENTIONS_TIMELINE;

		} else if (command.equals("dumpBlockList")) {

			opFileParam(blocklist, tsakExp);
			cRManager.DisplayInfoMessage("[OBJECTIVE]: Dump user "
					+ new TwitterAuth().getTwitterInstance().getScreenName()
					+ "'s Blocks List to " + TwitterCredentials.getOutputFile()
					+ ".", true);
			subVector = subCmdUpVector.BLOCK_LIST;

		} else if (command.equals("dumpUserTimeLine")) {

			subVector = tUserCred(usertimeline, command, tsakExp);

		} else if (command.equals("dumpTweets")
				|| command.equals("searchUsers")) {

			if (command.equals("dumpTweets")) {
				subVector = kwsreach(searchtweets, command, tsakExp);

			} else if (command.equals("searchUsers")) {
				subVector = kwsreach(searchusers, command, tsakExp);
			}

		} else if (command.equals("dumpStatus")
				|| command.equals("dumpRetweeters")) {

			if (command.equals("dumpStatus")) {
				subVector = tstatus(showstatus, command, tsakExp);
			} else if (command.equals("dumpRetweeters")) {
				subVector = tstatus(retweeters, command, tsakExp);
			}
		} else if (command.equals("dumpUsersLookup")) {

			subVector = tusers(userslookup, command, tsakExp);
		} else if (command.equals("showFriendShip")) {

			subVector = showfriendship(showfrndship, tsakExp);
		} else if (command.equals("dumpFriendsList")) {

			subVector = tUserCred(friendslist, command, tsakExp);
		} else if (command.equals("dumpFollowersList")) {

			subVector = tUserCred(followerslist, command, tsakExp);

		} else if (command.equals("dumpFavourites")) {

			opFileParam(favorities, tsakExp);
			cRManager.DisplayInfoMessage("[OBJECTIVE]: Dump user "
					+ new TwitterAuth().getTwitterInstance().getScreenName()
					+ "'s Favourites to " + TwitterCredentials.getOutputFile()
					+ ".", true);

			subVector = subCmdUpVector.FAVOURITES;

		} else if (command.equals("dumpSugeestedUserCats")) {

			opFileParam(suggestedusercats, tsakExp);
			cRManager.DisplayInfoMessage(
					"[OBJECTIVE]: Dump list of suggested user categories to "
							+ TwitterCredentials.getOutputFile() + ".", true);

			subVector = subCmdUpVector.SUGGESTED_USER_CATS;

		} else if (command.equals("dumpUserSuggestions")) {

			opFileParam(usersuggestions, tsakExp);
			subVector = tslug(usersuggestions, command, tsakExp);

		} else if (command.equals("dumpMemberSuggestions")) {

			opFileParam(membersuggestions, tsakExp);
			subVector = tslug(membersuggestions, command, tsakExp);

		} else if (command.equals("dumpUserLists")) {

			opFileParam(userlists, tsakExp);
			subVector = tUserCred(userlists, command, tsakExp);

		} else if (command.equals("dumpListStatuses")) {

			opFileParam(liststatuses, tsakExp);
			subVector = tlist(liststatuses, command, tsakExp);

		} else if (command.equals("dumpSavedSearches")) {

			opFileParam(savedsearches, tsakExp);
			cRManager.DisplayInfoMessage(
					"[OBJECTIVE]: Dump authenticated user saved searches to "
							+ TwitterCredentials.getOutputFile() + ".", true);

			subVector = subCmdUpVector.SAVED_SEARCHES;

		} else if (command.equals("lookupFriendShip")) {

			opFileParam(lookupfrndship, tsakExp);
			subVector = tusers(lookupfrndship, command, tsakExp);

		} else if (command.equals("dumpIncomingFriendships")) {

			opFileParam(incomingfrndship, tsakExp);
			cRManager.DisplayInfoMessage(
					"[OBJECTIVE]: Dump incoming friendships of authenticated user to "
							+ TwitterCredentials.getOutputFile() + ".", true);
			subVector = subCmdUpVector.INCOMING_FRNDSHIP;

		} else if (command.equals("dumpOutgoingFriendships")) {

			opFileParam(outgoingfrndship, tsakExp);
			cRManager.DisplayInfoMessage(
					"[OBJECTIVE]: Dump outgoing friendships of authenticated user to "
							+ TwitterCredentials.getOutputFile() + ".", true);
			subVector = subCmdUpVector.OUTGOING_FRNDSHIP;

		} else if (command.equals("dumpGeoDetails")) {

			opFileParam(geodetails, tsakExp);
			subVector = tgeo(geodetails, command, tsakExp);
			
		} else if (command.equals("dumpSimilarPlaces")) {

			opFileParam(similarplaces, tsakExp);
			subVector = tgeo(similarplaces, command, tsakExp);
			
		}else if (command.equals("searchPlace")) {

			opFileParam(searchplace, tsakExp);
			subVector = tgeo(searchplace, command, tsakExp);
			
		}else if (command.equals("dumpAccountSettings")) {

			opFileParam(accountsettings, tsakExp);
			cRManager.DisplayInfoMessage(
					"[OBJECTIVE]: Dump account settings to "
							+ TwitterCredentials.getOutputFile() + ".", true);
			subVector = subCmdUpVector.ACCOUNT_SETTINGS;
			
		}else if (command.equals("dumpAvailableTrends")) {

			opFileParam(availabletrends, tsakExp);
			cRManager.DisplayInfoMessage(
					"[OBJECTIVE]: Dump available trends to "
							+ TwitterCredentials.getOutputFile() + ".", true);
			subVector = subCmdUpVector.AVAILABLE_TRENDS;
			
		}else if (command.equals("dumpPlaceTrends")) {

			opFileParam(placetrends, tsakExp);
			subVector = ttrends(placetrends, command, tsakExp);
		}else if (command.equals("dumpClosestTrends")) {

			opFileParam(closest_trends, tsakExp);
			subVector = tgeo(closest_trends, command, tsakExp);
		}else if (command.equals("dumpMutesIDs")) {

			opFileParam(mutesIds, tsakExp);
			
			cRManager.DisplayInfoMessage(
					"[OBJECTIVE]: Dump authenticated user muted IDs to "
							+ TwitterCredentials.getOutputFile() + ".", true);
			
			subVector = subCmdUpVector.MUTES_IDS;
			
		}else if (command.equals("dumpMutesList")) {

			opFileParam(mutesList, tsakExp);
			
			cRManager.DisplayInfoMessage(
					"[OBJECTIVE]: Dump authenticated user muted usres list to "
							+ TwitterCredentials.getOutputFile() + ".", true);
			subVector = subCmdUpVector.MUTES_LIST;
			
		}else if (command.equals("dumpUserListMemberships")) {

			opFileParam(listmembership, tsakExp);
			subVector = tUserCred(listmembership, command, tsakExp);
		
		}else if (command.equals("dumpUserListSubscribers")) {

			opFileParam(userlistsubscriber, tsakExp);
			
			subVector = tlist(userlistsubscriber, command, tsakExp);
		}else if (command.equals("dumpUserListMembers")) {

			opFileParam(userlistmembers, tsakExp);
			
			subVector = tlist(userlistmembers, command, tsakExp);
		}else if (command.equals("dumpUserListSubscriptions")) {

			opFileParam(listssubscritons, tsakExp);
			subVector = tUserCred(listssubscritons, command, tsakExp);
		}
		
		
				
		return subVector;
	}

	
	private subCmdUpVector ttrends (Ttrends trd, String command, TsakException tsakExp) throws TsakException {
		
		subCmdUpVector sbv = null;
		
		if (trd.getWoeid() == null || trd.getWoeid().trim().isEmpty()) {
			
			throwMissingParamException(tsakExp, exceptionVector.MISSING_WOEID);
			
		}else {
			
			TwitterCredentials.setWoeid(trd.getWoeid());
			cRManager.DisplayInfoMessage("[OBJECTIVE]: Dump "+trd.getWoeid()+"'s trends to "+ TwitterCredentials.getOutputFile() + ".", true);
			sbv = subCmdUpVector.PLACE_TRENDS;
		}
		
		return sbv;
		
	}
	private subCmdUpVector tgeo(Tgeo tgo, String command, TsakException tsakExp)
			throws TsakException {

		subCmdUpVector sbv = null;

		if (command.equals("dumpGeoDetails")) {

			if (tgo.getPlaceId() == null || tgo.getPlaceId().trim().isEmpty()) {
				throwMissingParamException(tsakExp, exceptionVector.MISSING_PID);

			} else {

				TwitterCredentials.setPid(tgo.getPlaceId());
				cRManager.DisplayInfoMessage(
						"[OBJECTIVE]: Dump Geo Details of " + tgo.getPlaceId()
								+ " to " + TwitterCredentials.getOutputFile()
								+ ".", true);
				sbv = subCmdUpVector.GEO_DETAILS;
			}

		}else if (command.equals("dumpSimilarPlaces")) {

			if (tgo.getLatitude() == null || tgo.getLatitude().trim().isEmpty() || tgo.getLongitude() == null || tgo.getLongitude().trim().isEmpty() ||
					tgo.getPlaceName() == null || tgo.getPlaceName().trim().isEmpty()) {
				throwMissingParamException(tsakExp, exceptionVector.MISSING_CORD_OR_PLACE);

			} else {

				TwitterCredentials.setLatitude(tgo.getLatitude());
				TwitterCredentials.setLongitude(tgo.getLongitude());
				TwitterCredentials.setPlaceName(tgo.getPlaceName());
				
				cRManager.DisplayInfoMessage(
						"[OBJECTIVE]: Dump similar places of {"+tgo.getPlaceName()+"," + tgo.getLatitude() +","+
								tgo.getLongitude()+ "} to " + TwitterCredentials.getOutputFile()
								+ ".", true);
				
				sbv = subCmdUpVector.SIMILAR_PLACES;
			}

		} else if (command.equals("searchPlace") || command.equals("dumpClosestTrends")) {

			if (tgo.getLatitude() == null || tgo.getLatitude().trim().isEmpty() || tgo.getLongitude() == null || tgo.getLongitude().trim().isEmpty()) {
				throwMissingParamException(tsakExp, exceptionVector.MISSING_CORD);

			} else {

				TwitterCredentials.setLatitude(tgo.getLatitude());
				TwitterCredentials.setLongitude(tgo.getLongitude());
				
				if (command.equals("searchPlace")) {
				cRManager.DisplayInfoMessage(
						"[OBJECTIVE]: Search geo place with coordinates {"+ tgo.getLatitude() +","+
								tgo.getLongitude()+ "} and dump to " + TwitterCredentials.getOutputFile()
								+ ".", true);
				
				sbv = subCmdUpVector.SEARCH_PLACE_BY_CORD;
				
				}else if (command.equals("dumpClosestTrends")) {
					
					cRManager.DisplayInfoMessage(
							"[OBJECTIVE]: Get Closest trends of place with coordinates {"+ tgo.getLatitude() +","+
									tgo.getLongitude()+ "} and dump to " + TwitterCredentials.getOutputFile()
									+ ".", true);
					
					sbv = subCmdUpVector.CLOSEST_TRENDS;
				}
			}

		} 

		return sbv;
	}

	private subCmdUpVector tlist(Tlist tl, String command, TsakException tsakExp)
			throws TsakException {

		subCmdUpVector sbv = null;

		if (tl.getlId() == null && tl.getlId().trim().isEmpty()) {
			throwMissingParamException(tsakExp, exceptionVector.MISSING_LID);
		} else {

			TwitterCredentials.setLid(tl.getlId());
			if (command.equals("dumpListStatuses")) {
				cRManager.DisplayInfoMessage(
						"[OBJECTIVE]: Dump list " + tl.getlId()
								+ " statuses to "
								+ TwitterCredentials.getOutputFile() + ".",
						true);
				sbv = subCmdUpVector.LIST_STATUSES;
			} else if (command.equals("dumpUserListSubscribers")) {

				cRManager.DisplayInfoMessage(
						"[OBJECTIVE]: Dump list " + tl.getlId()
								+ " subscribers to "
								+ TwitterCredentials.getOutputFile() + ".",
						true);
				sbv = subCmdUpVector.LIST_SUBSCRIBERS;
			} else if (command.equals("dumpUserListMembers")) {

				cRManager.DisplayInfoMessage(
						"[OBJECTIVE]: Dump list " + tl.getlId()
								+ " members to "
								+ TwitterCredentials.getOutputFile() + ".",
						true);
				sbv = subCmdUpVector.LIST_MEMBERS;
			}
		}

		return sbv;
	}
	
	private subCmdUpVector tslug(Tslug ts, String command, TsakException tsakExp)
			throws TsakException {

		subCmdUpVector sbv = null;

		if (ts.getcSlug() != null && ts.getcSlug().trim().isEmpty()) {
			throwMissingParamException(tsakExp, exceptionVector.MISSING_SLUG);
		} else {

			TwitterCredentials.setcSlug(ts.getcSlug());
			if (command.equals("dumpUserSuggestions")) {
				cRManager
						.DisplayInfoMessage(
								"[OBJECTIVE]: Dump users in a given category of the Twitter suggested user list to "
										+ TwitterCredentials.getOutputFile()
										+ ".", true);
				sbv = subCmdUpVector.SUGGESTION_SLUG;
			} else if (command.equals("dumpMemberSuggestions")) {
				cRManager.DisplayInfoMessage(
						"[OBJECTIVE]: Dump Members suggestions list to "
								+ TwitterCredentials.getOutputFile() + ".",
						true);
				sbv = subCmdUpVector.MEMBER_SUGGESTION;
			}

		}

		return sbv;
	}

	private subCmdUpVector showfriendship(Tlink tl, TsakException tsakExp)
			throws TsakException {

		subCmdUpVector sbv = null;
		opFileParam(tl, tsakExp);

		if (tl.gettSname() == null || tl.gettSname().trim().isEmpty()
				|| tl.gettTname() == null || tl.gettTname().trim().isEmpty()) {
			throwMissingParamException(tsakExp,
					exceptionVector.MISSING_SRC_TRG_USER_SN_ID);
		} else {
			cRManager.DisplayInfoMessage(
					"[OBJECTIVE]: Dump friendship link between "
							+ tl.gettSname() + " & " + tl.gettTname() + " to "
							+ TwitterCredentials.getOutputFile() + ".", true);
			TwitterCredentials.settSname(tl.gettSname());
			TwitterCredentials.settTname(tl.gettTname());
			sbv = subCmdUpVector.SHOW_FRNDSHIP;
		}

		return sbv;
	}

	private subCmdUpVector tusers(Tusers tsz, String cmd, TsakException tsakExp)
			throws TsakException {

		subCmdUpVector sbv = null;
		opFileParam(tsz, tsakExp);

		if (tsz.getInputFile() != null && !tsz.getInputFile().trim().isEmpty()) {
			TwitterCredentials.setInputFile(tsz.getInputFile());

			if (cmd.equals("dumpUsersLookup")) {
				cRManager.DisplayInfoMessage(
						"[OBJECTIVE]: Dump Looked up users to "
								+ TwitterCredentials.getOutputFile() + ".",
						true);
				sbv = subCmdUpVector.USERS_LOOKUP;
			} else if (cmd.equals("lookupFriendShip")) {
				cRManager
						.DisplayInfoMessage(
								"[OBJECTIVE]: Lookup friendship of authenticated user to provided Twitter users and dump to "
										+ TwitterCredentials.getOutputFile()
										+ ".", true);

				sbv = subCmdUpVector.LOOKUP_FRNDSHIP;
			}

			return sbv;
		} else {
			throwMissingParamException(tsakExp,
					exceptionVector.MISSING_INPUT_FILE);
			return sbv;
		}

	}

	private subCmdUpVector tstatus(Tstatus ts, String cmd, TsakException tsakExp)
			throws TsakException {

		subCmdUpVector sbv = null;
		opFileParam(ts, tsakExp);

		if (ts.getsID() != null && !ts.getsID().trim().isEmpty()) {
			TwitterCredentials.setsID(ts.getsID());
			if (cmd.equals("dumpStatus")) {
				cRManager.DisplayInfoMessage(
						"[OBJECTIVE]: Dump status with ID: "
								+ TwitterCredentials.getsID() + " to "
								+ TwitterCredentials.getOutputFile() + ".",
						true);
				sbv = subCmdUpVector.GET_STATUS_BY_ID;
			} else if (cmd.equals("dumpRetweeters")) {
				cRManager.DisplayInfoMessage("[OBJECTIVE]: Dump status "
						+ TwitterCredentials.getsID() + " Retweeters to "
						+ TwitterCredentials.getOutputFile() + ".", true);
				sbv = subCmdUpVector.GET_STATUS_RETWEETERS;
			}
			return sbv;
		} else {
			throwMissingParamException(tsakExp, exceptionVector.MISSING_SID);
			return sbv;
		}

	}

	private subCmdUpVector kwsreach(KWsearch kwsrch, String cmd,
			TsakException tsakExp) throws TsakException {

		opFileParam(kwsrch, tsakExp);
		subCmdUpVector sbv = null;

		if (kwsrch.getKeyWords() != null
				&& !kwsrch.getKeyWords().trim().isEmpty()) {
			TwitterCredentials.setKeyWords(kwsrch.getKeyWords());
			if (cmd.equals("dumpTweets")) {

				cRManager.DisplayInfoMessage(
						"[OBJECTIVE]: Dump Tweets containing '"
								+ TwitterCredentials.getKeyWords() + "' to "
								+ TwitterCredentials.getOutputFile() + ".",
						true);
				sbv = subCmdUpVector.SEARCH_TWEETS;
			} else if (cmd.equals("searchUsers")) {
				cRManager.DisplayInfoMessage(
						"[OBJECTIVE]: Dump searched users in relevance to '"
								+ TwitterCredentials.getKeyWords() + "' to "
								+ TwitterCredentials.getOutputFile() + ".",
						true);
				sbv = subCmdUpVector.SEARCH_USER;
			}

		} else {
			throwMissingParamException(tsakExp,
					exceptionVector.MISSING_SEARCH_KEYWORDS);
		}

		return sbv;
	}

	private void opFileParam(Object obj, TsakException tsakExp)
			throws TsakException {

		FDbase fdbase = (FDbase) obj;

		if (fdbase.getOutputFile() == null || fdbase.getOutputFile().isEmpty()) {

			throwMissingParamException(tsakExp,
					exceptionVector.MISSING_OUTPUT_FILE);

		} else {
			TwitterCredentials.setOutputFile(fdbase.getOutputFile());
		}

	}

	private subCmdUpVector tUserCred(Tuser tuser, String cmd,
			TsakException tsakExp) throws TsakException {

		subCmdUpVector subVector = null;

		opFileParam(tuser, tsakExp);

		// if neither screen Name nor ID provided
		if ((tuser.getuScreenName() == null && tuser.getuID() == null)
				|| (tuser.getuScreenName().isEmpty() && tuser.getuID()
						.isEmpty())) {

			throwMissingParamException(tsakExp,
					exceptionVector.BOTH_SCREEN_NAME_ID_MISSING);

			// if both screen Name and ID are provided
		} else if ((!(tuser.getuScreenName() == null) && !(tuser.getuID() == null))
				& (!tuser.getuScreenName().isEmpty() && !tuser.getuID()
						.isEmpty())) {

			throwMissingParamException(tsakExp,
					exceptionVector.BOTH_SCREEN_NAME_ID_GIVEN);

			// if screen Name is provided
		} else if ((!tuser.getuScreenName().isEmpty() && tuser.getuID()
				.isEmpty())
				|| (!(tuser.getuScreenName() == null) && tuser.getuID() == null)) {

			TwitterCredentials.setuScreenName(tuser.getuScreenName());
			if (cmd.equals("dumpFollowerIDs")) {
				cRManager.DisplayInfoMessage(
						"[OBJECTIVE]: Dump user "
								+ TwitterCredentials.getuScreenName()
								+ "'s Followers IDs to "
								+ TwitterCredentials.getOutputFile() + ".",
						true);
				subVector = subCmdUpVector.FLWRZ_DUMP_BY_SCREEN_NAME;
			} else if (cmd.equals("dumpFriendIDs")) {
				cRManager.DisplayInfoMessage(
						"[OBJECTIVE]: Dump user "
								+ TwitterCredentials.getuScreenName()
								+ "'s Friends IDs to "
								+ TwitterCredentials.getOutputFile() + ".",
						true);
				subVector = subCmdUpVector.FRNDZ_DUMP_BY_SCREEN_NAME;
			} else if (cmd.equals("dumpUserTimeLine")) {
				cRManager.DisplayInfoMessage(
						"[OBJECTIVE]: Dump user "
								+ TwitterCredentials.getuScreenName()
								+ "'s Timeline to "
								+ TwitterCredentials.getOutputFile() + ".",
						true);
				subVector = subCmdUpVector.USER_TIMELINE;
			} else if (cmd.equals("dumpFriendsList")) {
				cRManager.DisplayInfoMessage(
						"[OBJECTIVE]: Dump user "
								+ TwitterCredentials.getuScreenName()
								+ "'s Friends List to "
								+ TwitterCredentials.getOutputFile() + ".",
						true);
				subVector = subCmdUpVector.FRIENDS_LIST;
			} else if (cmd.equals("dumpFollowersList")) {
				cRManager.DisplayInfoMessage(
						"[OBJECTIVE]: Dump user "
								+ TwitterCredentials.getuScreenName()
								+ "'s Followers List to "
								+ TwitterCredentials.getOutputFile() + ".",
						true);
				subVector = subCmdUpVector.FOLLOWERS_LIST;
			} else if (cmd.equals("dumpUserLists")) {
				cRManager.DisplayInfoMessage("[OBJECTIVE]: Dump user "
						+ TwitterCredentials.getuScreenName() + "'s Lists to "
						+ TwitterCredentials.getOutputFile() + ".", true);
				subVector = subCmdUpVector.USER_LISTS;
			} else if (cmd.equals("dumpUserListMemberships")) {
				
				cRManager.DisplayInfoMessage("[OBJECTIVE]: Dump "
						+ TwitterCredentials.getuScreenName() + "'s Lists memberships to "
						+ TwitterCredentials.getOutputFile() + ".", true);
				subVector = subCmdUpVector.LISTS_MEMEBERSHIPS;
			} else if (cmd.equals("dumpUserListSubscriptions")) {
				
				cRManager.DisplayInfoMessage("[OBJECTIVE]: Dump "
						+ TwitterCredentials.getuScreenName() + "'s lists subscriptions to "
						+ TwitterCredentials.getOutputFile() + ".", true);
				subVector = subCmdUpVector.LIST_SUBSCRIPTIONS;
			}

			
			// if User ID is provided
		} else if ((tuser.getuScreenName().isEmpty() && !tuser.getuID()
				.isEmpty())
				|| (tuser.getuScreenName() == null && !(tuser.getuID() == null))) {

			TwitterCredentials.setuID(tuser.getuID());
			if (cmd.equals("dumpFollowerIDs")) {
				cRManager.DisplayInfoMessage("[OBJECTIVE]: Dump user "
						+ TwitterCredentials.getuID() + "'s Followers IDs to "
						+ TwitterCredentials.getOutputFile() + ".", true);
				subVector = subCmdUpVector.FLWRZ_DUMP_BY_ID;
			} else if (cmd.equals("dumpFriendIDs")) {
				subVector = subCmdUpVector.FRNDZ_DUMP_BY_ID;
				cRManager.DisplayInfoMessage("[OBJECTIVE]: Dump user "
						+ TwitterCredentials.getuID() + "'s Friends IDs to "
						+ TwitterCredentials.getOutputFile() + ".", true);
			} else if (cmd.equals("dumpUserTimeLine")) {
				cRManager.DisplayInfoMessage("[OBJECTIVE]: Dump user "
						+ TwitterCredentials.getuID() + "'s Timeline to "
						+ TwitterCredentials.getOutputFile() + ".", true);
				subVector = subCmdUpVector.USER_TIMELINE;
			} else if (cmd.equals("dumpFriendsList")) {
				cRManager.DisplayInfoMessage("[OBJECTIVE]: Dump user "
						+ TwitterCredentials.getuID() + "'s Friends List to "
						+ TwitterCredentials.getOutputFile() + ".", true);
				subVector = subCmdUpVector.FRIENDS_LIST;
			} else if (cmd.equals("dumpFollowersList")) {
				cRManager.DisplayInfoMessage("[OBJECTIVE]: Dump user "
						+ TwitterCredentials.getuID() + "'s Followers List to "
						+ TwitterCredentials.getOutputFile() + ".", true);
				subVector = subCmdUpVector.FOLLOWERS_LIST;
			} else if (cmd.equals("dumpUserLists")) {
				cRManager.DisplayInfoMessage("[OBJECTIVE]: Dump user "
						+ TwitterCredentials.getuID() + "'s Lists to "
						+ TwitterCredentials.getOutputFile() + ".", true);
				subVector = subCmdUpVector.USER_LISTS;
			}else if (cmd.equals("dumpUserListMemberships")) {
				
				cRManager.DisplayInfoMessage("[OBJECTIVE]: Dump "
						+ TwitterCredentials.getuID() + "'s Lists memberships to "
						+ TwitterCredentials.getOutputFile() + ".", true);
				subVector = subCmdUpVector.LISTS_MEMEBERSHIPS;
			}
		}

		return subVector;
	}

	public static boolean TwitterCredentialsAvailable() {

		if (TwitterCredentials.getConsumerKey().trim().isEmpty()
				|| TwitterCredentials.getConsumerSecretKey().trim().isEmpty()
				|| TwitterCredentials.getUserAccessToken().trim().isEmpty()
				|| TwitterCredentials.getUserAccessSecretToken().trim()
						.isEmpty()) {

			return false;
		}

		return true;
	}

	private void throwMissingParamException(TsakException tsakExp,
			exceptionVector expVec) throws TsakException {

		switch (expVec) {
		case MISSING_OUTPUT_FILE:

			tsakExp = new TsakException(
					"Missing output File name. e.g. myOutput.txt");
			break;
		case MISSING_SCREEN_NAME:
			break;
		case MISSING_ID:
			break;
		case BOTH_SCREEN_NAME_ID_GIVEN:
			tsakExp = new TsakException(
					"Error Command format. Please provide either Screen Name or ID.");
			break;
		case BOTH_SCREEN_NAME_ID_MISSING:
			tsakExp = new TsakException(
					"Missing User's Screen_Name/ID. Please provide either Target's Screen Name or ID.");
			break;
		case MISSING_SEARCH_KEYWORDS:
			tsakExp = new TsakException(
					"Missing Search Keywords. Please provide atleast one Keyword as search parameter.");
			break;
		case MISSING_SID:
			tsakExp = new TsakException(
					"Missing Status ID. Please provided one.");
			break;
		case MISSING_INPUT_FILE:

			tsakExp = new TsakException(
					"Missing INPUT file name. Please provided one.");
			break;
		case MISSING_LID:
			tsakExp = new TsakException("Missing List ID parameter.");
			break;
		case MISSING_PID:
			tsakExp = new TsakException(
					"Missing Place ID. Please provided one.");
			break;
		case MISSING_CORD_OR_PLACE:
			tsakExp = new TsakException(
					"Missing Geo Coordinates or Place Name. Please provided both of them.");
			break;
		case MISSING_CORD:
			tsakExp = new TsakException(
					"Missing Geo Coordinates. Please provided them.");
			break;

		case MISSING_SRC_TRG_USER_SN_ID:
			tsakExp = new TsakException(
					"Missing source/targer user screen name or id.");
			break;
		case MISSING_SLUG:
			tsakExp = new TsakException(
					"Missing Twitter category slug. i.e. family, sports etc");
			break;
		case MISSING_WOEID:
			tsakExp = new TsakException("Missing Twitter woeid (Where on earth ID). Please provide one.");
			break;
		default:
			break;
		}

		throw tsakExp;
	}

}
