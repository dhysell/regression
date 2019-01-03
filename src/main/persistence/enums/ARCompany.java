package persistence.enums;

public enum ARCompany {
	Commercial("Commercial"), 
	Personal("Personal");
	String value;
	
	private ARCompany(String type){
		value = type;
	}
	
	public String getValue(){
		return value;
	}
}
