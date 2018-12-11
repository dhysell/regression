package repository.gw.enums;

public enum SR22FilingFee {
	
	Charged("Charged"),
	Reversed("Reversed");
	
	String value;
	
	
	private SR22FilingFee(String status){
		value = status;
	}
	
	public String getValue(){
		return value;
	}

}
