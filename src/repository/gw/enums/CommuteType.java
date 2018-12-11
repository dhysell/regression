package repository.gw.enums;

public enum CommuteType {
	
	DrivesCompanyVehicle("Drives Company Vehicle"),
	NotApplicable("Not Applicable"),
	ParkAndRide("Park and Ride"),
	PersonalVehicle("Personal Vehicle"),
	WorkFromHome("Work From Home");
	String value;
	
	CommuteType(String commuteType) {
		value = commuteType;
	}
	
	public String getValue(){
		return value;
	}

	public static CommuteType getEnumFromStringValue(String text){
	    for(CommuteType type: CommuteType.values()){
	        if(text.equalsIgnoreCase(type.value)){
	            return type;
            }
        }
        return null;
    }

}
