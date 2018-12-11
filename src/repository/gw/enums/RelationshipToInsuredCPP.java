package repository.gw.enums;

public enum RelationshipToInsuredCPP {
	NONE("<none>"),
	SelfPolicyholder("Self - Policyholder"),
	Spouse("Spouse"),
	Child("Child"),
	Employee("Employee"),
	SignificantOther("Significant Other"),
	Other("Other");
	String value;
	
	RelationshipToInsuredCPP(String relationship) {
		value = relationship;
	}
	
	public String getValue(){
		return value;
	}
}
