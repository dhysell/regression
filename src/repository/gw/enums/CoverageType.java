package repository.gw.enums;

public enum CoverageType {
	
	BroadForm("Broad Form"),
	SpecialForm("Special Form"),
	Peril_1("Peril 1"),
	Peril_1Thru9("Peril 1-9"),
	Peril_1Thru8("Peril 1-8"),
	None("<none>");
	String value;
	
	private CoverageType(String type){
		value = type;
	}
	
	public String getValue(){
		return value;
	}
}
