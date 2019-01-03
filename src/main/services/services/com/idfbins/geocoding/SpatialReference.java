package services.services.com.idfbins.geocoding;

import java.util.HashMap;
import java.util.Map;

public class SpatialReference {

	private Integer wkid;
	private Integer latestWkid;
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	public Integer getWkid() {
		return wkid;
	}

	public void setWkid(Integer wkid) {
		this.wkid = wkid;
	}

	public Integer getLatestWkid() {
		return latestWkid;
	}

	public void setLatestWkid(Integer latestWkid) {
		this.latestWkid = latestWkid;
	}

	public Map<String, Object> getAdditionalProperties() {
		return this.additionalProperties;
	}

	public void setAdditionalProperty(String name, Object value) {
		this.additionalProperties.put(name, value);
	}

}
