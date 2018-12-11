package repository.gw.enums;

public enum ReinstateReason {
	None("<none>"),
	Other("Other"),
	Payment_Received("Payment received");
	String value;
	
	private ReinstateReason(String type){
		this.value = type;
	}
	
	public String getValue(){
		return this.value;
	}
}
