package services.services.com.idfbins.geocoding;

import java.util.HashMap;
import java.util.Map;

public class Location {

	private Double x;
	private Double y;
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	public Double getX() {
		return x;
	}

	public void setX(Double x) {
		this.x = x;
	}

	public Double getY() {
		return y;
	}

	public void setY(Double y) {
		this.y = y;
	}

	public Map<String, Object> getAdditionalProperties() {
		return this.additionalProperties;
	}

	public void setAdditionalProperty(String name, Object value) {
		this.additionalProperties.put(name, value);
	}

}
