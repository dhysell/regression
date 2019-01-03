package repository.gw.enums;

public enum AbAccountType {
	
	Squire("Squire"),
	Umbrella("Umbrella"),
	Standard_Fire("Standard Fire"),
	Standard_Liability("Standard Liability"),
	Standard_Inland_Marine("Standard Inland Marine"),
	BOP("BOP"),
	CPP("CPP"),
	Commercial_Excess("Excess"),
	Brokerage("Brokerage");	
	
	String value;
		
	private AbAccountType(String type){
		value = type;
	}
	
	public String getValue(){
		return value;
	}
	
	public static AbAccountType getEnumFromStringValue(String text){
		// check that text isn't empty before doing comparison. 
		if(text != null){
			for(AbAccountType type : AbAccountType.values()){
				if(text.equalsIgnoreCase(type.value)){
					return type;
				}
			}
		}
		return null; // text is null to begin with. 
	}
}
