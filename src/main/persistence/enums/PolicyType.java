package persistence.enums;

public enum PolicyType {

	Squire("Squire"),
	Businessowners("BusinessOwners"), 
	CPP("CommercialPackage"), 
	StandardIM("PersonalInlandMarine"),
	StandardFL("Homeowners"),
	PersonalUmbrella("PersonalUmbrella");
	
	String dbValue;
	
	private PolicyType(String dbValue){
		this.dbValue = dbValue;
	}
	
	public String getDBValue(){
		return dbValue;
	}
	
	public static PolicyType getTypeFromDBValue(String dbValue) {
		PolicyType toReturn = null;
		for(PolicyType ptype : values()) {
			if(ptype.getDBValue().equals(dbValue)) {
				toReturn = ptype;
				break;
			}
		}
		return toReturn;
	}
	
}
