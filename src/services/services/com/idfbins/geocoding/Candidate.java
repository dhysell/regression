package services.services.com.idfbins.geocoding;

import java.util.HashMap;
import java.util.Map;

public class Candidate {

	private String address;
	private Location location;
	private Double score;
	private services.services.com.idfbins.geocoding.Attributes attributes;
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public Double getScore() {
		return score;
	}

	public void setScore(Double score) {
		this.score = score;
	}

	public services.services.com.idfbins.geocoding.Attributes getAttributes() {
		return attributes;
	}

	public void setAttributes(Attributes attributes) {
		this.attributes = attributes;
	}

	public Map<String, Object> getAdditionalProperties() {
		return this.additionalProperties;
	}

	public void setAdditionalProperty(String name, Object value) {
		this.additionalProperties.put(name, value);
	}

}
