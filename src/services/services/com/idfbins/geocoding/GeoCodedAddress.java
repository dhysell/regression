package services.services.com.idfbins.geocoding;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GeoCodedAddress {

	private services.services.com.idfbins.geocoding.SpatialReference spatialReference;
	private List<services.services.com.idfbins.geocoding.Candidate> candidates = new ArrayList<services.services.com.idfbins.geocoding.Candidate>();
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	public services.services.com.idfbins.geocoding.SpatialReference getSpatialReference() {
		return spatialReference;
	}

	public void setSpatialReference(SpatialReference spatialReference) {
		this.spatialReference = spatialReference;
	}

	public List<services.services.com.idfbins.geocoding.Candidate> getCandidates() {
		return candidates;
	}

	public void setCandidates(List<Candidate> candidates) {
		this.candidates = candidates;
	}

	public Map<String, Object> getAdditionalProperties() {
		return this.additionalProperties;
	}

	public void setAdditionalProperty(String name, Object value) {
		this.additionalProperties.put(name, value);
	}

}
