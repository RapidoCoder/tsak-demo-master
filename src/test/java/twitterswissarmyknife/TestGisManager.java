package twitterswissarmyknife;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import twitter4j.GeoLocation;
import twitter4j.GeoQuery;
import twitter4j.JSONObject;
import twitter4j.Location;
import twitter4j.Place;
import twitter4j.ResponseList;
import twitter4j.Trend;
import twitter4j.Trends;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitterhandler.GisManager;
import twitterhandler.LimitsManager;
import twitterhandler.TwitterCredentials;
import dto.ControlHandlers.LimitsEndPointsVector;
import dto.ControlHandlers.controlVectors;
import dto.CrManager;
import dto.DDManager;
import dto.TsakException;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ controlVectors.class, TwitterCredentials.class,
		GeoLocation.class})
public class TestGisManager {
	@Mock
	Twitter twitter;
	DDManager ddManager;
	CrManager cRManager;
	LimitsManager lManager;

	Place place;
	ResponseList<Place> places;
	Iterator<Place> placeIterator;

	Location location;
	ResponseList<Location> locations;
	Iterator<Location> locationIterator;
	
	
	
	Trends trends;
	Trend trend;

	@SuppressWarnings("unchecked")
	@Before
	public void setup() {
		twitter = Mockito.mock(Twitter.class);
		cRManager = Mockito.mock(CrManager.class);
		lManager = Mockito.mock(LimitsManager.class);
		ddManager = Mockito.mock(DDManager.class);
		PowerMockito.mockStatic(TwitterCredentials.class);

		places = Mockito.mock(ResponseList.class);
		place = Mockito.mock(Place.class);
		placeIterator = Mockito.mock(Iterator.class);

		locations = Mockito.mock(ResponseList.class);
		location = Mockito.mock(Location.class);
		locationIterator = Mockito.mock(Iterator.class);
		
		trends = Mockito.mock(Trends.class);
		trend = Mockito.mock(Trend.class);
		

	}

	@Test
	public void getSimilarPlaces() throws NumberFormatException, Exception {
		Map<String, Object> expectedMap = new HashMap<String, Object>();
		expectedMap.put("id", "1");
		expectedMap.put("name", "name");
		expectedMap.put("type", "type");
		expectedMap.put("street_address", "address");
		expectedMap.put("country_code", "123");
		expectedMap.put("country", "country");
		expectedMap.put("full_name", "fullname");
		JSONObject expectedJson = new JSONObject(expectedMap);

		List<String> expected = new ArrayList<String>();
		expected.add(expectedJson.toString());

		int lmts[] = { 15, 1000 };

		String name = "Washington DC";
		Mockito.when(
				cRManager.rateLimitAnalyzer(twitter, lManager,
						LimitsEndPointsVector.GEO_SIMILAR_PLACES)).thenReturn(
				lmts);
		Mockito.when(TwitterCredentials.getLatitude()).thenReturn("38.9047");
		Mockito.when(TwitterCredentials.getLongitude()).thenReturn("77.0164");
		GeoLocation geoLocation = new GeoLocation(
				Double.parseDouble("38.9047"), Double.parseDouble("77.0164"));

		Mockito.when(TwitterCredentials.getPlaceName()).thenReturn(name);
		Mockito.when(twitter.getSimilarPlaces(geoLocation, name, null, null))
				.thenReturn(places);

		Mockito.when(places.size()).thenReturn(1);

		Mockito.when(placeIterator.hasNext()).thenReturn(true, false);
		Mockito.when(placeIterator.next()).thenReturn(place);
		Mockito.when(places.iterator()).thenReturn(placeIterator);

		Mockito.when(place.getId()).thenReturn("1");
		Mockito.when(place.getName()).thenReturn("name");
		Mockito.when(place.getCountry()).thenReturn("country");
		Mockito.when(place.getCountryCode()).thenReturn("123");
		Mockito.when(place.getFullName()).thenReturn("fullname");
		Mockito.when(place.getPlaceType()).thenReturn("type");
		Mockito.when(place.getStreetAddress()).thenReturn("address");

		Mockito.when(place.getContainedWithIn()).thenReturn(null);

		GisManager gisManager = new GisManager(twitter, ddManager, cRManager,
				lManager);
		List<String> result = gisManager.getSimilarPlaces();
		assertEquals(result, expected);
	}

	@Test
	public void getGeoLocationInfo() throws TwitterException, TsakException {

		Map<String, Object> expectedMap = new HashMap<String, Object>();
		expectedMap.put("id", "1");
		expectedMap.put("name", "name");
		expectedMap.put("type", "type");
		expectedMap.put("street_address", "address");
		expectedMap.put("country_code", "123");
		expectedMap.put("country", "country");
		expectedMap.put("full_name", "fullname");
		JSONObject expectedJson = new JSONObject(expectedMap);
		String expected = expectedJson.toString();
		int lmts[] = { 15, 1000 };
		Mockito.when(
				cRManager.rateLimitAnalyzer(twitter, lManager,
						LimitsEndPointsVector.GEO_ID_PLACE_ID))
				.thenReturn(lmts);
		Mockito.when(twitter.getGeoDetails(TwitterCredentials.getPid()))
				.thenReturn(place);
		Mockito.when(place.getId()).thenReturn("1");
		Mockito.when(place.getName()).thenReturn("name");
		Mockito.when(place.getCountry()).thenReturn("country");
		Mockito.when(place.getCountryCode()).thenReturn("123");
		Mockito.when(place.getFullName()).thenReturn("fullname");
		Mockito.when(place.getPlaceType()).thenReturn("type");
		Mockito.when(place.getStreetAddress()).thenReturn("address");

		Mockito.when(place.getContainedWithIn()).thenReturn(null);

		GisManager gisManager = new GisManager(twitter, ddManager, cRManager,
				lManager);
		String result = gisManager.getGeoLocationInfo();
		assertEquals(result, expected);
	}

	@Test
	public void searchPlace() throws TwitterException, TsakException {

		Map<String, Object> expectedMap = new HashMap<String, Object>();
		expectedMap.put("id", "1");
		expectedMap.put("name", "name");
		expectedMap.put("type", "type");
		expectedMap.put("street_address", "address");
		expectedMap.put("country_code", "123");
		expectedMap.put("country", "country");
		expectedMap.put("full_name", "fullname");
		JSONObject expectedJson = new JSONObject(expectedMap);
		List<String> expected = new ArrayList<String>();
		expected.add(expectedJson.toString());

		int lmts[] = { 10, 500 };
		Mockito.when(
				cRManager.rateLimitAnalyzer(twitter, lManager,
						LimitsEndPointsVector.GEO_SEARCH)).thenReturn(lmts);

		Mockito.when(TwitterCredentials.getLatitude()).thenReturn("38.9047");
		Mockito.when(TwitterCredentials.getLongitude()).thenReturn("77.0164");
		GeoQuery query = new GeoQuery(new GeoLocation(
				Double.parseDouble("38.9047"), Double.parseDouble("77.0164")));
		Mockito.when(twitter.searchPlaces(query)).thenReturn(places);
		Mockito.when(places.size()).thenReturn(1);
		Mockito.when(placeIterator.hasNext()).thenReturn(true, false);
		Mockito.when(placeIterator.next()).thenReturn(place);
		Mockito.when(places.iterator()).thenReturn(placeIterator);

		Mockito.when(place.getId()).thenReturn("1");
		Mockito.when(place.getName()).thenReturn("name");
		Mockito.when(place.getCountry()).thenReturn("country");
		Mockito.when(place.getCountryCode()).thenReturn("123");
		Mockito.when(place.getFullName()).thenReturn("fullname");
		Mockito.when(place.getPlaceType()).thenReturn("type");
		Mockito.when(place.getStreetAddress()).thenReturn("address");

		Mockito.when(place.getContainedWithIn()).thenReturn(null);

		GisManager gisManager = new GisManager(twitter, ddManager, cRManager,
				lManager);
		List<String> result = gisManager.searchPlace();
		assertEquals(result, expected);

	}

	@Test
	public void getAvalilableTrends() throws TwitterException, TsakException {
		Map<String, Object> expectedMap = new HashMap<String, Object>();
		Map<String, Object> type = new HashMap<String, Object>();
		type.put("place_code", 123);
		type.put("place_name", "placename");
		expectedMap.put("woeid", 1);
		expectedMap.put("name", "name");
		expectedMap.put("type", type);
		expectedMap.put("country_code", "countrycode");
		expectedMap.put("url", "url");
		expectedMap.put("country", "countryname");
		
		JSONObject expectedJson = new JSONObject(expectedMap);
		List<String> expected = new ArrayList<String>();
		expected.add(expectedJson.toString());
		int lmts[] = { 10, 500 };
		Mockito.when(
				cRManager.rateLimitAnalyzer(twitter, lManager,
						LimitsEndPointsVector.TRENDS_AVAILABLE)).thenReturn(
				lmts);
		Mockito.when(twitter.getAvailableTrends()).thenReturn(locations);
		Mockito.when(locationIterator.hasNext()).thenReturn(true, false);
		Mockito.when(locationIterator.next()).thenReturn(location);
		Mockito.when(locations.iterator()).thenReturn(locationIterator);
		
		Mockito.when(location.getName()).thenReturn("name");
		Mockito.when(location.getWoeid()).thenReturn(1);
		Mockito.when(location.getCountryName()).thenReturn("countryname");
		Mockito.when(location.getCountryCode()).thenReturn("countrycode");
		Mockito.when(location.getURL()).thenReturn("url");
		Mockito.when(location.getPlaceName()).thenReturn("placename");
		Mockito.when(location.getPlaceCode()).thenReturn(123);
		
		
		GisManager gisManager = new GisManager(twitter, ddManager, cRManager,
				lManager);
		List<String> result = gisManager.getAvalilableTrends();
		assertEquals(result, expected);
		
	}
	
	@Test
	public void getPlaceTrends() throws NumberFormatException, TwitterException, TsakException{
		Map<String, Object> expectedMap = new HashMap<String, Object>();
		expectedMap.put("query", "query");
		expectedMap.put("name", "name");
		expectedMap.put("url", "url");
	
		
		JSONObject expectedJson = new JSONObject(expectedMap);
		List<String> expected = new ArrayList<String>();
		expected.add(expectedJson.toString());
		int lmts[] = { 10, 500 };
		Mockito.when(
				cRManager.rateLimitAnalyzer(twitter, lManager,
						LimitsEndPointsVector.TRENDS_PLACE)).thenReturn(
				lmts);
		Mockito.when(TwitterCredentials.getWoeid()).thenReturn("1");
		Mockito.when(twitter.getPlaceTrends(Integer.parseInt(TwitterCredentials.getWoeid()))).thenReturn(trends);
		Mockito.when(trends.getTrends()).thenReturn(new Trend[]{trend});
		
		Mockito.when(trend.getName()).thenReturn("name");
		Mockito.when(trend.getQuery()).thenReturn("query");
		Mockito.when(trend.getURL()).thenReturn("url");
		
		GisManager gisManager = new GisManager(twitter, ddManager, cRManager,
				lManager);
		List<String> result = gisManager.getPlaceTrends();
		assertEquals(result, expected);
	}
	
	@Test
	public void getClosestTrends() throws TwitterException, TsakException{
		Map<String, Object> expectedMap = new HashMap<String, Object>();
		Map<String, Object> type = new HashMap<String, Object>();
		type.put("place_code", 123);
		type.put("place_name", "placename");
		expectedMap.put("woeid", 1);
		expectedMap.put("name", "name");
		expectedMap.put("type", type);
		expectedMap.put("country_code", "countrycode");
		expectedMap.put("url", "url");
		expectedMap.put("country", "countryname");
		
		JSONObject expectedJson = new JSONObject(expectedMap);
		List<String> expected = new ArrayList<String>();
		expected.add(expectedJson.toString());
		int lmts[] = { 10, 500 };
		Mockito.when(
				cRManager.rateLimitAnalyzer(twitter, lManager,
						LimitsEndPointsVector.TRENDS_CLOSEST)).thenReturn(
				lmts);
		Mockito.when(TwitterCredentials.getLatitude()).thenReturn("38.9047");
		Mockito.when(TwitterCredentials.getLongitude()).thenReturn("77.0164");
		GeoLocation geoLocation = new GeoLocation(
				Double.parseDouble("38.9047"), Double.parseDouble("77.0164"));
		Mockito.when(twitter.getClosestTrends(geoLocation)).thenReturn(locations);
		Mockito.when(locationIterator.hasNext()).thenReturn(true, false);
		Mockito.when(locationIterator.next()).thenReturn(location);
		Mockito.when(locations.iterator()).thenReturn(locationIterator);
		
		Mockito.when(location.getName()).thenReturn("name");
		Mockito.when(location.getWoeid()).thenReturn(1);
		Mockito.when(location.getCountryName()).thenReturn("countryname");
		Mockito.when(location.getCountryCode()).thenReturn("countrycode");
		Mockito.when(location.getURL()).thenReturn("url");
		Mockito.when(location.getPlaceName()).thenReturn("placename");
		Mockito.when(location.getPlaceCode()).thenReturn(123);
		GisManager gisManager = new GisManager(twitter, ddManager, cRManager,
				lManager);
		List<String> result = gisManager.getClosestTrends();
		assertEquals(result, expected);
	}
}
