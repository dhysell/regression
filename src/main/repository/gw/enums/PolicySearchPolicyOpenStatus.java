package repository.gw.enums;

public enum PolicySearchPolicyOpenStatus {
	None("<none>"), 
	Closed("Closed"),
	Open("Open"), 
	Open_Locked("Open Locked");
	String value;
	
	private PolicySearchPolicyOpenStatus(String type){
		value = type;
	}
	
	public String getValue(){
		return value;
	}
}
