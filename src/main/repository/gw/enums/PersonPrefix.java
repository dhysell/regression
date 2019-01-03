package repository.gw.enums;

public enum PersonPrefix {
	None("<none selected>"), 
	Mr("Mr."), 
	Mrs("Mrs."), 
	Ms("Mr."), 
	Dr("Dr.");
	String value;
	
	private PersonPrefix(String type){
		value = type;
	}
	
	public String getValue(){
		return value;
	}
}
