package repository.ab.enums;

public enum MatchType {
	
	Exact("Exact"),
	Potential("Potential");
	
	String value;
	
	private MatchType(String type) {
		value = type;
	}
	
	public String getValue() {
		return value;
	}
}
