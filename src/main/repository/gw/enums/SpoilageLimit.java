package repository.gw.enums;

public enum SpoilageLimit {
	Five000("5000"), 
	Ten000("10000"), 
	Fifteen000("15000"), 
	Twenty000("20000"), 
	TwentyFive000("25000"), 
	Thirty000("30000"), 
	Forty000("40000"), 
	FortyFive000("45000"), 
	Fifty000("50000");
	String value;
	
	private SpoilageLimit(String value){
		this.value = value;
	}
	
	public String getValue(){
		return this.value;
	}
}
