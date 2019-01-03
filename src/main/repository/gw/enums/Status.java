package repository.gw.enums;

public enum Status {
	Disbursed("Disbursed"), 
	Reversed("Reversed"),
	Applied("Applied"),
	All("All"),
	Closed("Closed"),
	Created("Created"),
	Requested("Requested"),
	Drafted("Drafted"),
    Canceled("Canceled"),
    Complete("Complete"),
	Awaiting_Approval("Awaiting Approval"),
	Open("Open");
	String value;
	
	private Status(String value){
		this.value = value;
	}
	
	public String getValue(){
		return value;
	}
	
	public static Status getEnumFromStringValue(String text){
        for(Status status: Status.values()){
            if(text.equalsIgnoreCase(status.value)){
                return status;
            }
        }
        return null;
    }
	
	
	
	
	
	
	
	
	
	
	
	
}
