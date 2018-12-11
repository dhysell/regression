package repository.gw.enums;

public enum DocumentStatus {
	Approved("Approved"),
	Approving("Approving"),
	Draft("Draft"),
	Final("Final");
	String value;
	
	private DocumentStatus(String type){
		value = type;
	}
	
	public String getValue(){
		return value;
	}
}
