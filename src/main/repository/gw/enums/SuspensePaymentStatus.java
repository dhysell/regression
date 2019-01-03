package repository.gw.enums;

public enum SuspensePaymentStatus {
	Reversed("Reversed"), 
	Applied("Applied"), 
	Disbured("Disbursed"), 
	Open("Open");
	String value;
	
	private SuspensePaymentStatus(String type){
		value = type;
	}
	
	public String getValue(){
		return value;
	}
}
