package repository.gw.enums;

public enum SearchAccountOrPolicy {
	AccountNumber("Account Number"),
	PolicyNumber("Policy Number");
	String value;
	
	private SearchAccountOrPolicy(String type){
		value = type;
	}
	
	public String getValue(){
		return value;
	}
}
