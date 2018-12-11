package repository.gw.enums;

public enum DelinquencyStatus {
	None("<none>"),
	Delinquent("Delinquent"),
	In_Good_Standing("In Good Standing");
	String value;
		
	private DelinquencyStatus(String type){
		value = type;
	}
	
	public String getValue(){
		return value;
	}
}
