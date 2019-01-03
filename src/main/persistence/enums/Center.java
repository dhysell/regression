package persistence.enums;

public enum Center {
	PolicyCenter("PC"),
	BillingCenter("BC"), 
	ContactManager("AB"), 
	ClaimCenter("CC");
	
	String centerName;
	
	private Center(String centerName){
		this.centerName = centerName;
	}
	
	public String getValue(){
		return centerName;
	}
}
