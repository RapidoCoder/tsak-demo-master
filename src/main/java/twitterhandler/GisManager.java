package twitterhandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import twitter4j.GeoLocation;
import twitter4j.GeoQuery;
import twitter4j.Location;
import twitter4j.Place;
import twitter4j.ResponseList;
import twitter4j.Trend;
import twitter4j.Trends;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import dto.ControlHandlers.LimitsEndPointsVector;
import dto.CrManager;
import dto.DDManager;
import dto.TsakException;

public class GisManager {

	Twitter twitter;
	DDManager ddManager;
	CrManager cRManager;
	LimitsManager lManager;
	Long timeStart, timeEnd;
	private GeoLocation location;
	private ResponseList<Place> places;
	private Map<String, Object> geoMap;
	private List<Map<String, Object>> containList;
	private Place[] containedWithinArray;
	private JSONObject jObj;
	private List <String> gisReturn;
	private Place place;
	private GeoQuery query;
	private ResponseList<Location> locations;
	private Map<String, Object> loc;
	private Map<String, Object> type;
	private Trends trends;
	private Map<String, Object> trendMap;


	public GisManager(Twitter twtr, DDManager ddm, CrManager crm,
			LimitsManager lmg) {

		ddManager = ddm;
		cRManager = crm;
		lManager = lmg;
		twitter = twtr;
		
		gisReturn = new ArrayList<String>();
	}

	public List <String> getSimilarPlaces() throws TsakException {

		cRManager.DisplayInfoMessage(
				"[INFO]: Checking Rate Limits Availibity.", true);

		int availableCalls = 0;
		int availableTime = 0;

		int lmts[] = cRManager.rateLimitAnalyzer(twitter, lManager,
				LimitsEndPointsVector.GEO_SIMILAR_PLACES);
		availableCalls = lmts[0];
		availableTime = lmts[1];

		cRManager.DisplayInfoMessage("[INFO]: " + availableCalls
				+ " Calls Available in " + availableTime + " Seconds.", true);

		try {

			if (availableCalls == 0) {
				throw new TsakException(
						"[ERROE]: Twitter Rate Limit Exceeded. Please try again after "
								+ availableTime + " Seconds.");
			}

			cRManager.DisplayInfoMessage(
					"[INFO]: Getting similar geo places....", true);

			timeStart = System.currentTimeMillis();
			
			location = new GeoLocation(
					Double.parseDouble(TwitterCredentials.getLatitude()),
					Double.parseDouble(TwitterCredentials.getLongitude()));
			
			String name = TwitterCredentials.getPlaceName();

			places = twitter.getSimilarPlaces(location,
					name, null, null);
			 
			if (places.size() == 0) {
              
				cRManager
						.DisplayInfoMessage(
								"[INFO]: No location associated with the specified condition",
								true);

			} else {
				 
				for (Place place : places) {

					geoMap = new HashMap<String, Object>();
					containList = new ArrayList<Map<String, Object>>();

					geoMap.put("id", place.getId());
					geoMap.put("name", place.getName());
					geoMap.put("country", place.getCountry());
					geoMap.put("country_code", place.getCountryCode());
					geoMap.put("full_name", place.getFullName());
					geoMap.put("type", place.getPlaceType());
					geoMap.put("street_address", place.getStreetAddress());

					containedWithinArray = place.getContainedWithIn();
					if (containedWithinArray != null
							&& containedWithinArray.length != 0) {

						for (Place containedWithinPlace : containedWithinArray) {

							Map<String, Object> sub_map = new HashMap<String, Object>();
							sub_map.put("id", containedWithinPlace.getId());
							sub_map.put("name",
									containedWithinPlace.getFullName());
							sub_map.put("country",
									containedWithinPlace.getCountry());
							sub_map.put("type",
									containedWithinPlace.getPlaceType());

							containList.add(sub_map);
						}

						geoMap.put("contained_within", containList);
					}
					
					jObj = new JSONObject (geoMap);
					
					gisReturn.add(jObj.toString());
					
					//ddManager.writeLine((new JSONObject(geoMap)).toString(),true);
				}
			}

			timeEnd = System.currentTimeMillis();
			cRManager.DisplayInfoMessage(
					"[INFO]: Dumped Data Successfully to "
							+ TwitterCredentials.getOutputFile() + " in "
							+ String.valueOf((timeEnd - timeStart) / 1000F)
							+ " seconds.", true);

		} catch (TwitterException e) {

			cRManager.DisplayInfoMessage("[ERROR]: " + e.getMessage(), true);
		}
		

		
		return gisReturn;
	}

	public String getGeoLocationInfo() throws TsakException {

		cRManager.DisplayInfoMessage(
				"[INFO]: Checking Rate Limits Availibity.", true);

		int availableCalls = 0;
		int availableTime = 0;

		int lmts[] = cRManager.rateLimitAnalyzer(twitter, lManager,
				LimitsEndPointsVector.GEO_ID_PLACE_ID);
		availableCalls = lmts[0];
		availableTime = lmts[1];

		cRManager.DisplayInfoMessage("[INFO]: " + availableCalls
				+ " Calls Available in " + availableTime + " Seconds.", true);

		try {

			if (availableCalls == 0) {
				throw new TsakException(
						"[ERROE]: Twitter Rate Limit Exceeded. Please try again after "
								+ availableTime + " Seconds.");
			}

			cRManager.DisplayInfoMessage("[INFO]: Getting geo information....",
					true);

			timeStart = System.currentTimeMillis();

			place = twitter.getGeoDetails(TwitterCredentials.getPid());
			geoMap = new HashMap<String, Object>();

			geoMap.put("id", place.getId());
			geoMap.put("name", place.getName());
			geoMap.put("country", place.getCountry());
			geoMap.put("country_code", place.getCountryCode());
			geoMap.put("full_name", place.getFullName());
			geoMap.put("type", place.getPlaceType());
			geoMap.put("street_address", place.getStreetAddress());

			containList = new ArrayList<Map<String, Object>>();

			containedWithinArray = place.getContainedWithIn();

			if (containedWithinArray != null
					&& containedWithinArray.length != 0) {
				for (Place containedWithinPlace : containedWithinArray) {

					Map<String, Object> sub_map = new HashMap<String, Object>();
					sub_map.put("id", containedWithinPlace.getId());
					sub_map.put("name", containedWithinPlace.getFullName());
					sub_map.put("country", containedWithinPlace.getCountry());
					sub_map.put("type", containedWithinPlace.getPlaceType());

					containList.add(sub_map);
				}

				geoMap.put("contained_within", containList);
			}

			jObj = new JSONObject(geoMap);
			//ddManager.writeLine((new JSONObject(geoMap)).toString(), true);

			timeEnd = System.currentTimeMillis();
			cRManager.DisplayInfoMessage(
					"[INFO]: Geo details dumped successfully to "
							+ TwitterCredentials.getOutputFile() + " in "
							+ String.valueOf((timeEnd - timeStart) / 1000F)
							+ " seconds.", true);

		} catch (TwitterException e) {

			if (e.getErrorCode() == 404) {

				cRManager.DisplayInfoMessage("[INFO]: " + e.getMessage(), true);
			} else {

				cRManager
						.DisplayInfoMessage("[ERROR]: " + e.getMessage(), true);
			}
		}

		return jObj.toString();
	}

	public List<String> searchPlace() throws TsakException  {

		cRManager.DisplayInfoMessage("[INFO]: Checking Rate Limits Availibity.", true);

		int availableCalls = 0;
		int availableTime = 0;

		int lmts[] = cRManager.rateLimitAnalyzer(twitter, lManager,
				LimitsEndPointsVector.GEO_SEARCH);
		availableCalls = lmts[0];
		availableTime = lmts[1];

		cRManager.DisplayInfoMessage("[INFO]: " + availableCalls+ " Calls Available in " + availableTime + " Seconds.", true);

		try {

			if (availableCalls == 0) {
				throw new TsakException(
						"[ERROE]: Twitter Rate Limit Exceeded. Please try again after "
								+ availableTime + " Seconds.");
			}

			cRManager.DisplayInfoMessage("[INFO]: Searching Geo Location....", true);

			timeStart = System.currentTimeMillis();
			
			query = new GeoQuery(new GeoLocation(
					Double.parseDouble(TwitterCredentials.getLatitude()),
					Double.parseDouble(TwitterCredentials.getLongitude())));
			
			places = twitter.searchPlaces(query);

			if (places.size() == 0) {
				
				cRManager.DisplayInfoMessage("[INFO]: No location associated with the specified IP address or lat/lang", true);
				
			} else {
				
				for (Place place : places) {

					geoMap = new HashMap<String, Object>();

					geoMap.put("id", place.getId());
					geoMap.put("name", place.getName());
					geoMap.put("country", place.getCountry());
					geoMap.put("country_code", place.getCountryCode());
					geoMap.put("full_name", place.getFullName());
					geoMap.put("type", place.getPlaceType());
					geoMap.put("street_address", place.getStreetAddress());

					containList = new ArrayList<Map<String, Object>>();

					containedWithinArray = place.getContainedWithIn();

					if (containedWithinArray != null
							&& containedWithinArray.length != 0) {
						for (Place containedWithinPlace : containedWithinArray) {

							Map<String, Object> sub_map = new HashMap<String, Object>();
							sub_map.put("id", containedWithinPlace.getId());
							sub_map.put("name",
									containedWithinPlace.getFullName());
							sub_map.put("country",
									containedWithinPlace.getCountry());
							sub_map.put("type",
									containedWithinPlace.getPlaceType());

							containList.add(sub_map);
						}

						geoMap.put("contained_within", containList);
					}
					
					jObj = new JSONObject (geoMap);
					gisReturn.add(jObj.toString());
					
					//ddManager.writeLine((new JSONObject(geoMap)).toString(), true);

				}
			}

			timeEnd = System.currentTimeMillis();
			
			cRManager.DisplayInfoMessage(
					"[INFO]: Search completed, results dumped to "
							+ TwitterCredentials.getOutputFile() + " in "
							+ String.valueOf((timeEnd - timeStart) / 1000F)
							+ " seconds.", true);
			
		} catch (TwitterException e) {

			cRManager.DisplayInfoMessage("[ERROR]: " + e.getMessage(), true);
		} 

		return gisReturn;
	}
	
	
	//trends methods
	
	public List<String> getAvalilableTrends () throws TsakException {
		
		cRManager.DisplayInfoMessage("[INFO]: Checking Rate Limits Availibity.", true);

		int availableCalls = 0;
		int availableTime = 0;

		int lmts[] = cRManager.rateLimitAnalyzer(twitter, lManager,
				LimitsEndPointsVector.TRENDS_AVAILABLE);
		availableCalls = lmts[0];
		availableTime = lmts[1];

		cRManager.DisplayInfoMessage("[INFO]: " + availableCalls+ " Calls Available in " + availableTime + " Seconds.", true);

		try {

			if (availableCalls == 0) {
				throw new TsakException(
						"[ERROE]: Twitter Rate Limit Exceeded. Please try again after "
								+ availableTime + " Seconds.");
			}

			cRManager.DisplayInfoMessage("[INFO]: Getting available trends....", true);

			timeStart = System.currentTimeMillis();
			
			locations = twitter.getAvailableTrends();
			
			for (Location location : locations) {
				
				loc = new HashMap<String, Object>();
				type = new HashMap<String, Object>();

				
				loc.put("name", location.getName());
				loc.put("woeid", location.getWoeid());
				loc.put("country", location.getCountryName());
				loc.put("country_code", location.getCountryCode());
				loc.put("url", location.getURL());
				
				type.put("place_name", location.getPlaceName());
				type.put("place_code", location.getPlaceCode());
				
				loc.put("type", type);
				
				jObj = new JSONObject(loc);
				gisReturn.add(jObj.toString());
				
				//ddManager.writeLine((new JSONObject(loc)).toString(), true);
				
			}
			
			
			timeEnd = System.currentTimeMillis();
			
			cRManager.DisplayInfoMessage(
					"[INFO]: Available trends dumped successfully to "
							+ TwitterCredentials.getOutputFile() + " in "
							+ String.valueOf((timeEnd - timeStart) / 1000F)
							+ " seconds.", true);
			
		} catch (TwitterException e) {

			cRManager.DisplayInfoMessage("[ERROR]: " + e.getMessage(), true);
		} 
		
		return gisReturn;
	}
	
	
	
	public List<String> getPlaceTrends () throws TsakException {
		
		cRManager.DisplayInfoMessage("[INFO]: Checking Rate Limits Availibity.", true);

		int availableCalls = 0;
		int availableTime = 0;

		int lmts[] = cRManager.rateLimitAnalyzer(twitter, lManager,
				LimitsEndPointsVector.TRENDS_PLACE);
		availableCalls = lmts[0];
		availableTime = lmts[1];

		cRManager.DisplayInfoMessage("[INFO]: " + availableCalls+ " Calls Available in " + availableTime + " Seconds.", true);

		try {

			if (availableCalls == 0) {
				throw new TsakException(
						"[ERROE]: Twitter Rate Limit Exceeded. Please try again after "
								+ availableTime + " Seconds.");
			}

			cRManager.DisplayInfoMessage("[INFO]: Getting "+TwitterCredentials.getWoeid()+"'s trends....", true);

			timeStart = System.currentTimeMillis();
			
			trends = twitter.getPlaceTrends(Integer.parseInt(TwitterCredentials.getWoeid()));
			
			for (Trend trend : trends.getTrends()) {
				
				trendMap = new HashMap<String, Object>();
				
				trendMap.put("name", trend.getName());
				trendMap.put("query", trend.getQuery());
				trendMap.put("url", trend.getURL());
				
				jObj = new JSONObject(trendMap);
				
				gisReturn.add(jObj.toString());
				
				//ddManager.writeLine((new JSONObject(trendMap)).toString(), true);
				
			}
			
			
			timeEnd = System.currentTimeMillis();
			
			cRManager.DisplayInfoMessage(
					"[INFO]: Available trends dumped successfully to "
							+ TwitterCredentials.getOutputFile() + " in "
							+ String.valueOf((timeEnd - timeStart) / 1000F)
							+ " seconds.", true);
			
		} catch (TwitterException e) {

			cRManager.DisplayInfoMessage("[ERROR]: " + e.getMessage(), true);
		} 
		
		return gisReturn;
	}
	
	public List<String> getClosestTrends () throws TsakException {
		
		cRManager.DisplayInfoMessage("[INFO]: Checking Rate Limits Availibity.", true);

		int availableCalls = 0;
		int availableTime = 0;

		int lmts[] = cRManager.rateLimitAnalyzer(twitter, lManager,
				LimitsEndPointsVector.TRENDS_CLOSEST);
		availableCalls = lmts[0];
		availableTime = lmts[1];

		cRManager.DisplayInfoMessage("[INFO]: " + availableCalls+ " Calls Available in " + availableTime + " Seconds.", true);

		try {

			if (availableCalls == 0) {
				throw new TsakException(
						"[ERROE]: Twitter Rate Limit Exceeded. Please try again after "
								+ availableTime + " Seconds.");
			}

			cRManager.DisplayInfoMessage("[INFO]: Getting {"+TwitterCredentials.getLatitude()+","+TwitterCredentials.getLongitude()+"}'s trends....", true);

			timeStart = System.currentTimeMillis();
			locations = twitter.getClosestTrends(new GeoLocation(Double.parseDouble(TwitterCredentials.getLatitude()), Double.parseDouble(TwitterCredentials.getLongitude())));

			for (Location location : locations) {
				
				loc = new HashMap<String, Object>();
				type = new HashMap<String, Object>();
				
				loc.put("name", location.getName());
				loc.put("woeid", location.getWoeid());
				loc.put("country", location.getCountryName());
				loc.put("country_code", location.getCountryCode());
				loc.put("url", location.getURL());
				
				type.put("place_name", location.getPlaceName());
				type.put("place_code", location.getPlaceCode());
				
				loc.put("type", type);
				
				jObj = new JSONObject(loc);
				
				gisReturn.add(jObj.toString());
				
				//ddManager.writeLine((new JSONObject(loc)).toString(), true);
				
			}
			
			timeEnd = System.currentTimeMillis();
			
			cRManager.DisplayInfoMessage(
					"[INFO]: Closest trends dumped successfully to "
							+ TwitterCredentials.getOutputFile() + " in "
							+ String.valueOf((timeEnd - timeStart) / 1000F)
							+ " seconds.", true);
			
		} catch (TwitterException e) {

			cRManager.DisplayInfoMessage("[ERROR]: " + e.getMessage(), true);
		} 
		
		return gisReturn;
	}

}
