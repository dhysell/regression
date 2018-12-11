package repository.gw.generate.custom;

import repository.gw.enums.InlandMarineCPP;

public class LocationBlanketCoverage {
	
	private repository.gw.generate.custom.PolicyLocation location;
	private CPPCommercialProperty_Building property;
	
	private String limit = "1000";
	private InlandMarineCPP.RecepticalType recepticalType = InlandMarineCPP.RecepticalType.ClassC_1hours;
	
	private boolean forwardToHomeOffice = false;
	private InlandMarineCPP.PercentDuplicated percentDuplicated = InlandMarineCPP.PercentDuplicated.Atleast51Percent;
	
	private String manufaturer = "";
	private String label = "";
	
	private InlandMarineCPP.Issuer issuer = InlandMarineCPP.Issuer.NONE;
	
	private boolean exclusion_DuplicateRecords_CM_66_04 = false;

	public LocationBlanketCoverage(repository.gw.generate.custom.PolicyLocation location, CPPCommercialProperty_Building property) {
		this.location = location;
		this.property = property;
	}

	public repository.gw.generate.custom.PolicyLocation getLocation() {
		return location;
	}

	public void setLocation(PolicyLocation location) {
		this.location = location;
	}

	public CPPCommercialProperty_Building getProperty() {
		return property;
	}

	public void setProperty(CPPCommercialProperty_Building property) {
		this.property = property;
	}

	public String getLimit() {
		return limit;
	}

	public void setLimit(String limit) {
		this.limit = limit;
	}

	public InlandMarineCPP.RecepticalType getRecepticalType() {
		return recepticalType;
	}

	public void setRecepticalType(InlandMarineCPP.RecepticalType recepticalType) {
		this.recepticalType = recepticalType;
	}

	public boolean isForwardToHomeOffice() {
		return forwardToHomeOffice;
	}

	public void setForwardToHomeOffice(boolean forwardToHomeOffice) {
		this.forwardToHomeOffice = forwardToHomeOffice;
	}

	public InlandMarineCPP.PercentDuplicated getPercentDuplicated() {
		return percentDuplicated;
	}

	public void setPercentDuplicated(InlandMarineCPP.PercentDuplicated percentDuplicated) {
		this.percentDuplicated = percentDuplicated;
	}

	public String getManufaturer() {
		return manufaturer;
	}

	public void setManufaturer(String manufaturer) {
		this.manufaturer = manufaturer;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public InlandMarineCPP.Issuer getIssuer() {
		return issuer;
	}

	public void setIssuer(InlandMarineCPP.Issuer issuer) {
		this.issuer = issuer;
	}

	public boolean isExclusion_DuplicateRecords_CM_66_04() {
		return exclusion_DuplicateRecords_CM_66_04;
	}

	public void setExclusion_DuplicateRecords_CM_66_04(boolean exclusion_DuplicateRecords_CM_66_04) {
		this.exclusion_DuplicateRecords_CM_66_04 = exclusion_DuplicateRecords_CM_66_04;
	}
	

}











