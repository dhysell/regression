package repository.gw.enums;

public enum PaymentLocation {
	
	NexusPayment("Nexus Payment"),
	PolicyCenter("PolicyCenter"), 
	ContactManager("ContactManager"), 
	BillingCenter("BillingCenter"), 
	ClaimCenter("ClaimCenter"),
	WebSite("Web Site");
	String value;
	
	private PaymentLocation(String type){
		this.value = type;
	}
	
	public String getValue(){
		return this.value;
	}
	public static PaymentLocation getRandom() {
		return values()[(int) (Math.random() * values().length)];
	}

}
