package twitterhandler;

import java.io.IOException;

import twitter4j.IDs;
import twitter4j.ResponseList;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.User;
import dto.ControlHandlers.LimitsEndPointsVector;
import dto.ControlHandlers.subCmdUpVector;
import dto.CrManager;
import dto.DDManager;
import dto.TsakException;

// Friends Followers Manager
public class FFManager {

	Twitter twitter;
	DDManager ddManager;
	CrManager cRManager;
	LimitsManager lManager;
	IDs followerIds;
	IDs friendsIds;

	public FFManager(Twitter twtr, DDManager ddm, CrManager crm,
			LimitsManager lmg) {

		ddManager = ddm;
		cRManager = crm;
		lManager = lmg;
		twitter = twtr;
	}

	public ResponseList<User> dumpFFToFile(String tuser, subCmdUpVector sbv)
			throws IOException {

		ResponseList<User> ffretrn = null;

		try {
			long cursor = -1;

			if (sbv == subCmdUpVector.FLWRZ_DUMP_BY_ID
					|| sbv == subCmdUpVector.FLWRZ_DUMP_BY_SCREEN_NAME) {

				cRManager.DisplayInfoMessage(
						"[ACTION]: Dumping followers to output File.", true);

				long timeStart = 0, timeEnd = 0;

				do {

					if (sbv == subCmdUpVector.FLWRZ_DUMP_BY_ID) {
						followerIds = twitter.getFollowersIDs(
								Long.parseLong(tuser), cursor);
					} else {
						followerIds = twitter.getFollowersIDs(tuser, cursor);
					}

					cRManager.DisplayInfoMessage(
							"[INFO]: Checking Rate Limits Availibity.", true);

					int lmts[] = cRManager.rateLimitAnalyzer(twitter, lManager,
							LimitsEndPointsVector.FOLLOWERS_IDS);

					int availableCalls = lmts[0];
					int availableTime = lmts[1];

					cRManager.DisplayInfoMessage("[INFO]: " + availableCalls
							+ " Calls Available in " + availableTime
							+ " seconds.", true);

					if (availableCalls == 0) {
						throw new TsakException(
								"Call not available, please try later.");
					}

					timeStart = System.currentTimeMillis();

					ffretrn = twitter.lookupUsers(followerIds.getIDs());

				} while ((cursor = followerIds.getNextCursor()) != 0);

				timeEnd = System.currentTimeMillis();
				cRManager.DisplayInfoMessage(
						"[INFO]: Dumped followers successfully to "
								+ TwitterCredentials.getOutputFile() + " in "
								+ String.valueOf((timeEnd - timeStart) / 1000F)
								+ " seconds.", true);

			} else if (sbv == subCmdUpVector.FRNDZ_DUMP_BY_ID
					|| sbv == subCmdUpVector.FRNDZ_DUMP_BY_SCREEN_NAME) {

				cRManager.DisplayInfoMessage(
						"[ACTION]: Dumping followings to output File.", true);

				long timeStart = 0, timeEnd = 0;
				do {

					if (sbv == subCmdUpVector.FRNDZ_DUMP_BY_ID) {
						friendsIds = twitter.getFriendsIDs(
								Long.parseLong(tuser), cursor);
					} else {

						friendsIds = twitter.getFriendsIDs(
								String.valueOf(tuser), cursor);
					}

					cRManager.DisplayInfoMessage(
							"[INFO]: Checking Rate Limits Availibity.", true);

					int lmts[] = cRManager.rateLimitAnalyzer(twitter, lManager,
							LimitsEndPointsVector.FRIENDS_IDS);

					int availableCalls = lmts[0];
					int availableTime = lmts[1];

					cRManager.DisplayInfoMessage("[INFO]: " + availableCalls
							+ " Calls Available in " + availableTime
							+ " seconds.", true);

					if (availableCalls == 0) {
						throw new TsakException(
								"Call not available, please try later.");
					}

					timeStart = System.currentTimeMillis();

					ffretrn = twitter.lookupUsers(friendsIds.getIDs());

				} while ((cursor = friendsIds.getNextCursor()) != 0);

				timeEnd = System.currentTimeMillis();
				cRManager.DisplayInfoMessage(
						"[INFO]: Dumped friends successfully to "
								+ TwitterCredentials.getOutputFile() + " in "
								+ String.valueOf((timeEnd - timeStart) / 1000F)
								+ " seconds.", true);
			}

		} catch (TwitterException te) {

			if (te.getErrorCode() == 404) {
				cRManager.DisplayErrorMessage(
						"[ERROR]: Target User Doesn't Exist.", true);
			} else {
				cRManager.DisplayErrorMessage(
						"[ERROR]: Unable to dump followers." + te.getMessage(),
						true);
			}

		} catch (TsakException e) {

			cRManager.DisplayErrorMessage("[ERROR]: " + e.getMessage(), true);
		} finally {
		}

		return ffretrn;
	}

}
