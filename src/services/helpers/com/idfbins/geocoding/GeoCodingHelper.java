package services.helpers.com.idfbins.geocoding;

import java.io.IOException;

import services.services.com.idfbins.geocoding.GeoCodedAddress;
import services.utils.JSONUtil;

public class GeoCodingHelper {
	
	private String baseUrl;
	
	public GeoCodingHelper(String baseUrl) {
		this.baseUrl = baseUrl;
	}
	
	public GeoCodedAddress getGeoCodedAddress(String street, String city, String stateAbbrev, String zip) throws IOException {
		street = street.replace(" ", "-");
		city = city.replace(" ", "-");
		
		String urlToTest = baseUrl + "/arcgis/rest/services/GeoCodingService/USA_StreetAddress/GeocodeServer/findAddressCandidates?Street=" + street + "&City=" + city + "&State=" + stateAbbrev + "&ZIP=" + zip + "&outFields=*&f=pjson";
		String jsonResponse = JSONUtil.getURLResponseAsString(urlToTest);
		GeoCodedAddress myGeoCodeAddrObj = (GeoCodedAddress) JSONUtil.convertJsonResponseToObj(jsonResponse, GeoCodedAddress.class);
		
		return myGeoCodeAddrObj;
	}
	
}
