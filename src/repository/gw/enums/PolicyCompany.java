package repository.gw.enums;

public enum PolicyCompany {
	Farm_Bureau("Farm Bureau Mutual", "001"), 
	Western_Community("Western Community", "008");
	String value;
	String suffix;
	
	private PolicyCompany(String type, String suffix){
		this.value = type;
		this.suffix = suffix;
	}
	
	public String getValue(){
		return value;
	}
	
	public String getSuffix(){
		return suffix;
	}
}
