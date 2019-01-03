package repository.gw.enums;

public enum InceptionDateSections {
	Policy("Policy"), 
	Property("Section I - Property"),
	Liability("Section II - General Liability"),
	Auto("Section III - Auto"),
	InlandMarine("Section IV - Inland Marine"),
	CPPProperty("Property"),
	CPPLiability("General Liability"),
	CPPAuto("Auto"),
	CPPInlandMarine("Inland Marine");
	
	String value;
	
	private InceptionDateSections(String type){
		value = type;
	}
	
	public String getValue(){
		return value;
	}
}
