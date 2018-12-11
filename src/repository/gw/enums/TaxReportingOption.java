package repository.gw.enums;

public enum TaxReportingOption {
	None("<none>"),
	SSN("Social Security Number"), 
	TIN("Tax ID Number");
	String value;
	
	private TaxReportingOption(String type){
		value = type;
	}
	
	public String getValue(){
		return value;
	}
}
