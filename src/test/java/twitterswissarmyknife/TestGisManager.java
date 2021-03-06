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
@PrepareForTest({ GisManager.class, controlVectors.class,
		TwitterCredentials.class, GeoLocation.class, GeoQuery.class })
public class TestGisManager {
	int lmts[] = { 10, 500 };

	@Mock
	Twitter twitter;
	DDManager ddManager;
	CrManager cRManager;
	LimitsManager lManager;
	GeoLocation geoLocation;
	GeoQuery geoQuery;
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

		geoQuery = Mockito.mock(GeoQuery.class);
		places = Mockito.mock(ResponseList.class);
		place = Mockito.mock(Place.class);
		placeIterator = Mockito.mock(Iterator.class);
		geoLocation = Mockito.mock(GeoLocation.class);
		locations = Mockito.mock(ResponseList.class);
		location = Mockito.mock(Location.class);
		locationIterator = Mockito.mock(Iterator.class);
		trends = Mockito.mock(Trends.class);
		trend = Mockito.mock(Trend.class);
	}

	@Test
	public void getSimilarPlaces() throws NumberFormatException, Exception {

		Map<String, Object> expectedMap = new HashMap<String, Object>();
		expectedMap.put("id", "2379574");
		expectedMap.put("name", "chicago");
		expectedMap.put("type", "Town");
		expectedMap.put("street_address", "Chicago, IL, USA");
		expectedMap.put("country_code", "+1");
		expectedMap.put("country", "USA");
		expectedMap.put("full_name", "Chicago city, USA");
		JSONObject expectedJson = new JSONObject(expectedMap);
		List<String> expected = new ArrayList<String>();
		expected.add(expectedJson.toString());

		Mockito.when(
				cRManager.rateLimitAnalyzer(twitter, lManager,
						LimitsEndPointsVector.GEO_SIMILAR_PLACES)).thenReturn(
				lmts);
		Mockito.when(TwitterCredentials.getLatitude()).thenReturn("41.51");
		Mockito.when(TwitterCredentials.getLongitude()).thenReturn("87.39");
		PowerMockito
				.whenNew(GeoLocation.class)
				.withArguments(Double.parseDouble("41.51"),
						Double.parseDouble("87.39")).thenReturn(geoLocation);
		Mockito.when(TwitterCredentials.getPlaceName()).thenReturn("chicago");
		Mockito.when(
				twitter.getSimilarPlaces(geoLocation, "chicago", null, null))
				.thenReturn(places);

		Mockito.when(places.size()).thenReturn(1);
		Mockito.when(placeIterator.hasNext()).thenReturn(true, false);
		Mockito.when(placeIterator.next()).thenReturn(place);
		Mockito.when(places.iterator()).thenReturn(placeIterator);
		Mockito.when(place.getId()).thenReturn("2379574");
		Mockito.when(place.getName()).thenReturn("chicago");
		Mockito.when(place.getCountry()).thenReturn("USA");
		Mockito.when(place.getCountryCode()).thenReturn("+1");
		Mockito.when(place.getFullName()).thenReturn("Chicago city, USA");
		Mockito.when(place.getPlaceType()).thenReturn("Town");
		Mockito.when(place.getStreetAddress()).thenReturn("Chicago, IL, USA");
		Mockito.when(place.getContainedWithIn()).thenReturn(null);

		GisManager gisManager = new GisManager(twitter, ddManager, cRManager,
				lManager);
		List<String> result = gisManager.getSimilarPlaces();
		assertEquals(result, expected);
	}

	@Test
	public void getGeoLocationInfo() throws TwitterException, TsakException {

		Map<String, Object> expectedMap = new HashMap<String, Object>();
		expectedMap.put("id", "2379574");
		expectedMap.put("name", "chicago");
		expectedMap.put("type", "Town");
		expectedMap.put("street_address", "Chicago, IL, USA");
		expectedMap.put("country_code", "+1");
		expectedMap.put("country", "USA");
		expectedMap.put("full_name", "Chicago city, USA");
		JSONObject expectedJson = new JSONObject(expectedMap);
		String expected = expectedJson.toString();

		Mockito.when(
				cRManager.rateLimitAnalyzer(twitter, lManager,
						LimitsEndPointsVector.GEO_ID_PLACE_ID))
				.thenReturn(lmts);
		Mockito.when(twitter.getGeoDetails(TwitterCredentials.getPid()))
				.thenReturn(place);
		Mockito.when(place.getId()).thenReturn("2379574");
		Mockito.when(place.getName()).thenReturn("chicago");
		Mockito.when(place.getCountry()).thenReturn("USA");
		Mockito.when(place.getCountryCode()).thenReturn("+1");
		Mockito.when(place.getFullName()).thenReturn("Chicago city, USA");
		Mockito.when(place.getPlaceType()).thenReturn("Town");
		Mockito.when(place.getStreetAddress()).thenReturn("Chicago, IL, USA");
		Mockito.when(place.getContainedWithIn()).thenReturn(null);

		GisManager gisManager = new GisManager(twitter, ddManager, cRManager,
				lManager);
		String result = gisManager.getGeoLocationInfo();
		assertEquals(result, expected);
	}

	@Test
	public void searchPlace() throws Exception {

		Map<String, Object> expectedMap = new HashMap<String, Object>();
		expectedMap.put("id", "2379574");
		expectedMap.put("name", "chicago");
		expectedMap.put("type", "Town");
		expectedMap.put("street_address", "Chicago, IL, USA");
		expectedMap.put("country_code", "+1");
		expectedMap.put("country", "USA");
		expectedMap.put("full_name", "Chicago city, USA");
		JSONObject expectedJson = new JSONObject(expectedMap);
		List<String> expected = new ArrayList<String>();
		expected.add(expectedJson.toString());

		Mockito.when(
				cRManager.rateLimitAnalyzer(twitter, lManager,
						LimitsEndPointsVector.GEO_SEARCH)).thenReturn(lmts);
		Mockito.when(TwitterCredentials.getLatitude()).thenReturn("38.9047");
		Mockito.when(TwitterCredentials.getLongitude()).thenReturn("77.0164");
		PowerMockito
				.whenNew(GeoLocation.class)
				.withArguments(Double.parseDouble("38.9047"),
						Double.parseDouble("77.0164")).thenReturn(geoLocation);
		PowerMockito.whenNew(GeoQuery.class).withArguments(geoLocation)
				.thenReturn(geoQuery);
		Mockito.when(twitter.searchPlaces(geoQuery)).thenReturn(places);
		Mockito.when(places.size()).thenReturn(1);
		Mockito.when(placeIterator.hasNext()).thenReturn(true, false);
		Mockito.when(placeIterator.next()).thenReturn(place);
		Mockito.when(places.iterator()).thenReturn(placeIterator);
		Mockito.when(place.getId()).thenReturn("2379574");
		Mockito.when(place.getName()).thenReturn("chicago");
		Mockito.when(place.getCountry()).thenReturn("USA");
		Mockito.when(place.getCountryCode()).thenReturn("+1");
		Mockito.when(place.getFullName()).thenReturn("Chicago city, USA");
		Mockito.when(place.getPlaceType()).thenReturn("Town");
		Mockito.when(place.getStreetAddress()).thenReturn("Chicago, IL, USA");
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
		type.put("place_code", +312);
		type.put("place_name", "Town");
		expectedMap.put("woeid", 2379574);
		expectedMap.put("name", "Chicago");
		expectedMap.put("type", type);
		expectedMap.put("country_code", "+1");
		expectedMap.put("url", "http://urlForChicago.com");
		expectedMap.put("country", "USA");
		JSONObject expectedJson = new JSONObject(expectedMap);
		List<String> expected = new ArrayList<String>();
		expected.add(expectedJson.toString());

		Mockito.when(
				cRManager.rateLimitAnalyzer(twitter, lManager,
						LimitsEndPointsVector.TRENDS_AVAILABLE)).thenReturn(
				lmts);
		Mockito.when(twitter.getAvailableTrends()).thenReturn(locations);
		Mockito.when(locationIterator.hasNext()).thenReturn(true, false);
		Mockito.when(locationIterator.next()).thenReturn(location);
		Mockito.when(locations.iterator()).thenReturn(locationIterator);
		Mockito.when(location.getName()).thenReturn("Chicago");
		Mockito.when(location.getWoeid()).thenReturn(2379574);
		Mockito.when(location.getCountryName()).thenReturn("USA");
		Mockito.when(location.getCountryCode()).thenReturn("+1");
		Mockito.when(location.getURL()).thenReturn("http://urlForChicago.com");
		Mockito.when(location.getPlaceName()).thenReturn("Town");
		Mockito.when(location.getPlaceCode()).thenReturn(+312);

		GisManager gisManager = new GisManager(twitter, ddManager, cRManager,
				lManager);
		List<String> result = gisManager.getAvalilableTrends();
		assertEquals(result, expected);
	}

	@Test
	public void getPlaceTrends() throws NumberFormatException,
			TwitterException, TsakException {
		
		Map<String, Object> expectedMap = new HashMap<String, Object>();
		expectedMap.put("query", "chicago new trends");
		expectedMap.put("name", "chicago");
		expectedMap.put("url", "http://urlForChicago.com");
		JSONObject expectedJson = new JSONObject(expectedMap);
		List<String> expected = new ArrayList<String>();
		expected.add(expectedJson.toString());
		Mockito.when(
				cRManager.rateLimitAnalyzer(twitter, lManager,
						LimitsEndPointsVector.TRENDS_PLACE)).thenReturn(lmts);
		Mockito.when(TwitterCredentials.getWoeid()).thenReturn("2379574");
		Mockito.when(
				twitter.getPlaceTrends(Integer.parseInt(TwitterCredentials
						.getWoeid()))).thenReturn(trends);
		Mockito.when(trends.getTrends()).thenReturn(new Trend[] { trend });
		Mockito.when(trend.getName()).thenReturn("chicago");
		Mockito.when(trend.getQuery()).thenReturn("chicago new trends");
		Mockito.when(trend.getURL()).thenReturn("http://urlForChicago.com");

		GisManager gisManager = new GisManager(twitter, ddManager, cRManager,
				lManager);
		List<String> result = gisManager.getPlaceTrends();
		assertEquals(result, expected);
	}

	@Test
	public void getClosestTrends() throws NumberFormatException, Exception {
		
		Map<String, Object> expectedMap = new HashMap<String, Object>();
		Map<String, Object> type = new HashMap<String, Object>();
		type.put("place_code", +312);
		type.put("place_name", "Town");
		expectedMap.put("woeid", 2379574);
		expectedMap.put("name", "Chicago");
		expectedMap.put("type", type);
		expectedMap.put("country_code", "+1");
		expectedMap.put("url", "http://urlForChicago.com");
		expectedMap.put("country", "USA");
		JSONObject expectedJson = new JSONObject(expectedMap);
		List<String> expected = new ArrayList<String>();
		expected.add(expectedJson.toString());

		Mockito.when(
				cRManager.rateLimitAnalyzer(twitter, lManager,
						LimitsEndPointsVector.TRENDS_CLOSEST)).thenReturn(lmts);
		Mockito.when(TwitterCredentials.getLatitude()).thenReturn("38.9047");
		Mockito.when(TwitterCredentials.getLongitude()).thenReturn("77.0164");

		PowerMockito
				.whenNew(GeoLocation.class)
				.withArguments(Double.parseDouble("38.9047"),
						Double.parseDouble("77.0164")).thenReturn(geoLocation);
		Mockito.when(twitter.getClosestTrends(geoLocation)).thenReturn(
				locations);
		Mockito.when(locationIterator.hasNext()).thenReturn(true, false);
		Mockito.when(locationIterator.next()).thenReturn(location);
		Mockito.when(locations.iterator()).thenReturn(locationIterator);
		Mockito.when(location.getName()).thenReturn("Chicago");
		Mockito.when(location.getWoeid()).thenReturn(2379574);
		Mockito.when(location.getCountryName()).thenReturn("USA");
		Mockito.when(location.getCountryCode()).thenReturn("+1");
		Mockito.when(location.getURL()).thenReturn("http://urlForChicago.com");
		Mockito.when(location.getPlaceName()).thenReturn("Town");
		Mockito.when(location.getPlaceCode()).thenReturn(+312);

		GisManager gisManager = new GisManager(twitter, ddManager, cRManager,
				lManager);
		List<String> result = gisManager.getClosestTrends();
		assertEquals(result, expected);
	}
}
