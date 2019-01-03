package repository.gw.enums;

public enum MembershipType {
	
	Regular("Regular"),
	Associate("Associate"),
	Other("Other");
	
	String value;
		
	private MembershipType(String type){
		value = type;
	}
	
	public String getValue(){
		return value;
	}
	
	public static MembershipType getEnumFromStringValue(String text){
		// check that text isn't empty before doing comparison. 
		if(text != null){
			for(MembershipType type : MembershipType.values()){
				if(text.equalsIgnoreCase(type.value)){
					return type;
				}
			}
		}
		return null; // text is null to begin with. 
	}
}
