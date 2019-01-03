package repository.gw.enums;

public enum ExistanceType {
	Required("Required"), 
	Electable("Electable"), 
	Suggested("Suggested");
	String value;
	
	private ExistanceType(String value){
		this.value = value;
	}
	
	public String getValue(){
		return this.value;
	}
}
