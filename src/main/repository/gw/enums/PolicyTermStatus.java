package repository.gw.enums;

public enum PolicyTermStatus {
	//Billing Center Statuses
	Expired("Expired"),
	InForce("In Force"),
	Future("Future"),
	
	//Policy Center Statuses
	Canceled("Canceled"),
	Scheduled("Scheduled");
	String value;
	
	private PolicyTermStatus(String type){
		value = type;
	}
	
	public String getValue(){
		return value;
	}
}
