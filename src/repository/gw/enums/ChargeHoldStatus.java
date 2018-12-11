package repository.gw.enums;

public enum ChargeHoldStatus {
	Held("Held"), 
	Not_Held("Not Held");
	String value;
	
	private ChargeHoldStatus(String type){
		value = type;
	}
	
	public String getValue(){
		return value;
	}
}
