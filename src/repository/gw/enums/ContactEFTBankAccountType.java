package repository.gw.enums;

public enum ContactEFTBankAccountType {
	Checking("Checking"), 
	Savings("Savings"), 
	Other("Other");
	String value;
	
	private ContactEFTBankAccountType(String type){
		value = type;
	}
	
	public String getValue(){
		return value;
	}
}
