package repository.gw.enums;

public enum PolicySearchPolicyProductType {
	None("<none>"), 
	Commercial_Property("Commercial Property"),
	Inland_Marine("Inland Marine"), 
	General_Liability("General Liability"),
	Business_Auto("Business Auto"),
	Personal_Auto("Personal Auto"),
	Business_Owners("Businessowners"),
	Home_Owners("Homeowners"),
	Commercial_Package("Commercial Package"),
	Squire("Squire"),
	Personal_Umbrella("Personal Umbrella");
	
	String value;
	
	private PolicySearchPolicyProductType(String type){
		value = type;
	}
	
	public String getValue(){
		return value;
	}
}
