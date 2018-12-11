package repository.gw.enums;

public enum Priority {
	Urgent("Urgent"), 
	High("High"), 
	Normal("Normal"), 
	Low("Low");
	String value;
	
	private Priority(String type){
		value = type;
	}
	
	public String getValue(){
		return value;
	}
	
	public static Priority getEnumFromStringValue(String text){
        for(Priority status: Priority.values()){
            if(text.equalsIgnoreCase(status.value)){
                return status;
            }
        }
        return null;
    }
}
