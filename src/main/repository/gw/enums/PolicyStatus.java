package repository.gw.enums;

public enum PolicyStatus {
	Open("Open"),
	Pending_Cancellation("Pending Cancellation"),
	Canceled("Canceled"),
	Pending_Rescind_Cancellation("Pending Rescind Cancellation"),
	Pending_Reinstatement("Pending Reinstatement");
	String value;
	
	private PolicyStatus(String type){
		value = type;
	}
	
	public String getValue(){
		return value;
	}
}
