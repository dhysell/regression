package repository.gw.enums;

public enum QuoteType {
	FullApplication("Full Application"), 
	QuickQuote("Quick Quote");
	String value;
	
	private QuoteType(String type){
		value = type;
	}
	
	public String getValue(){
		return value;
	}
}
