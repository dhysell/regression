package repository.gw.enums;

public enum LivestockDeductible {

	Ded0("0"), 
	Ded25("25"), 
	Ded50("50"), 
	Ded100("100"), 
	Ded250("250"), 
	Ded500("500"), 
	Ded1000("1,000");
	
	String value;
	
	private LivestockDeductible(String value){
		this.value = value;
	}
	
	public String getValue(){
		return this.value;
	}
	
}
