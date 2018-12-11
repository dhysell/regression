package repository.gw.enums;

public enum LivestockType {
	Livestock("Livestock"),
	DeathOfLivestock("Death of Livestock"),
	FourH("4H");
	
	String value;
	
	private LivestockType(String value){
		this.value = value;
	}
	
	public String getValue(){
		return value;
	}
}
