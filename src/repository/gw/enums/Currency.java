package repository.gw.enums;

public enum Currency {	
	USD("USD");
	String value;
	
	Currency(String commuteType) {
		value = commuteType;
	}
	
	public String getValue(){
		return value;
	}	
}
