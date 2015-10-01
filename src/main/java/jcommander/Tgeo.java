package jcommander;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;

@Parameters
public class Tgeo extends FDbase{

	@Parameter(names = "-pid", description = "Twitter Place ID.")
	private String placeId = "";

	@Parameter(names = "-lat", description = "Geo Latitude.")
	private String latitude = "";

	@Parameter(names = "-long", description = "Geo Longitude.")
	private String longitude = "";
	
	@Parameter(names = "-pname", description = "Place Name.")
	private String placeName = "";
	
	public String getPlaceName() {
		return placeName;
	}

	public void setPlaceName(String placeName) {
		this.placeName = placeName;
	}

	public String getPlaceId() {
		return placeId;
	}

	public void setPlaceId(String placeId) {
		this.placeId = placeId;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}
}
