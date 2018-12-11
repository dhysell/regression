package repository.gw.generate.custom;

public class SquireLiablityCoverageIncidentalOccupancyItem {
	
	public SquireLiablityCoverageIncidentalOccupancyItem(String description, repository.gw.generate.custom.PolicyLocation location) {
		this.description = description;
		this.location = location;
	}

	private String description = null;
	private repository.gw.generate.custom.PolicyLocation location = null;
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}

	public repository.gw.generate.custom.PolicyLocation getLocation() {
		return location;
	}

	public void setLocation(PolicyLocation location) {
		this.location = location;
	}

}
